package edu.pjwstk.demo.visitor;

import edu.pjwstk.demo.common.Query;
import edu.pjwstk.demo.datastore.*;
import edu.pjwstk.demo.interpreter.envs.ENVS;
import edu.pjwstk.demo.result.*;
import edu.pjwstk.demo.visitor.helpers.ArgumentResolver;
import edu.pjwstk.demo.visitor.helpers.Arguments;
import edu.pjwstk.demo.visitor.helpers.Operator;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.auxname.IAsExpression;
import edu.pjwstk.jps.ast.auxname.IGroupAsExpression;
import edu.pjwstk.jps.ast.binary.*;
import edu.pjwstk.jps.ast.terminal.*;
import edu.pjwstk.jps.ast.unary.*;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.interpreter.qres.IQResStack;
import edu.pjwstk.jps.result.*;
import edu.pjwstk.jps.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
    Implementacja ASTVisitora
     - odpowiada za wykonywanie wyrażeń (Expressions) i wrzucenie wyniku na stos (QRes)
     - implementacje wyrażeń nie mają żadnej logiki, są tylko "pojemnikami" na inne wyrażenia

    Postęp: 37/43 (86%)

    TODO:
     * OrderBy, CloseBy

    Uwaga:
     Implementacje nie muszą uwzględniać różnych typów danych wejściowych.
     Najpierw obsłużmy najprostsze scenariusze (np. double + double)
 */

public class ConcreteASTVisitor implements ASTVisitor {

    private final ENVS envs;

    private final IQResStack qres;
    private final IStoreRepository repository;
    private final ISBAStore store;

    public ConcreteASTVisitor(IQResStack qres,
                              IStoreRepository repository) {
        this.qres = qres;
        this.repository = repository;
        envs = new ENVS();
        store = repository.getStore();
        envs.init(store.getEntryOID(), store);
    }

    @Override
    public void visitAsExpression(IAsExpression expr) {
        IAbstractQueryResult result = getResult(expr.getInnerExpression());
        String name = expr.getAuxiliaryName();

        if (result instanceof ISingleResult) {
            qres.push(new BinderResult(result, name));
        }
        else if (result instanceof ICollectionResult){
            Collection<ISingleResult> elements;

            if (result instanceof IBagResult) {
                elements = ((IBagResult) result).getElements();
            }
            else if (result instanceof ISequenceResult) {
                elements = ((ISequenceResult) result).getElements();
            } else {
                // tu kiedyś wyrzucimy wyjątek
                return;
            }
            List<ISingleResult> binders = elements
                .stream()
                .map(x -> new BinderResult(x, name))
                .collect(Collectors.toList());

            qres.push(new BagResult(binders));
        }
    }

    @Override
    public void visitGroupAsExpression(IGroupAsExpression expr) {
        IAbstractQueryResult result = getResult(expr.getInnerExpression());
        String name = expr.getAuxiliaryName();

        qres.push(new BinderResult(result, name));
    }

    // All() jako odwrócone Any()
    @Override
    public void visitAllExpression(IForAllExpression expr) {
        IExpression condition = expr.getRightExpression();
        IBagResult collection = getBag(expr.getLeftExpression());

        IBooleanResult results = new BooleanResult(
            ! Query.any(collection.getElements(), x -> {
                qres.push(x);
                return !getBoolean(condition);
            }));

        qres.push(results);
    }

    @Override
    public void visitAndExpression(IAndExpression expr) {
        qres.push(new BooleanResult(
                getBoolean(expr.getLeftExpression())
             && getBoolean(expr.getRightExpression())
        ));
    }

    // Wykonuje prawe wyrażenie na każdym elemencie
    // Jeśli choć jeden spełnia warunek, pętla będzie przerwana
    @Override
    public void visitAnyExpression(IForAnyExpression expr) {
        IExpression condition = expr.getRightExpression();
        IBagResult collection = getBag(expr.getLeftExpression());

        BooleanResult results = new BooleanResult(
            Query.any(collection.getElements(), x -> {
                qres.push(x);
                return getBoolean(condition);
            }));

        qres.push(results);
    }

    @Override
    public void visitCloseByExpression(ICloseByExpression expr) {

    }

    // TODO: Tego się nie da utrzymać. Jeśli nie można tego uprościć, to zróbmy oddzielną klasę CommaExpressionEvaluator
    @Override
    public void visitCommaExpression(ICommaExpression expr) {

        List<ISingleResult> list = new ArrayList<>();
        List<ISingleResult> listLeft = new ArrayList<>();
        List<ISingleResult> listRight = new ArrayList<>();
        IBagResult leftBag = new BagResult();
        IBagResult rightBag = new BagResult();
        IStructResult leftStruct;
        IStructResult rightStruct;

        expr.getLeftExpression().accept(this);
        IAbstractQueryResult leftResult = qres.pop();
        if (leftResult instanceof ISingleResult) {
            if (leftResult instanceof IStructResult) {
                leftStruct = (IStructResult)  leftResult;
                for (ISingleResult structElement : leftStruct.elements()) {
                    list.add(structElement);
                    listLeft.add(structElement);
                }
            } else {
                list.add((ISingleResult) leftResult);
            }
        } else if (leftResult instanceof ICollectionResult) {
            leftBag = (IBagResult) leftResult;
            for (ISingleResult bagElement : leftBag.getElements()) {
                list.add(bagElement);
            }
        }

        expr.getRightExpression().accept(this);

        IAbstractQueryResult rightResult = qres.pop();
        if (rightResult instanceof ISingleResult) {
            if (rightResult instanceof IStructResult) {
                rightStruct = (IStructResult) rightResult;
                for (ISingleResult structElement : rightStruct.elements()) {
                    list.add(structElement);
                    listRight.add(structElement);
                }
            } else {
            list.add((ISingleResult) rightResult);
            }
        } else if (rightResult instanceof ICollectionResult) {
            rightBag = (IBagResult) rightResult;
            for (ISingleResult bagElement : rightBag.getElements()) {
                list.add(bagElement);
            }
        }


        if (leftResult instanceof ICollectionResult || rightResult instanceof ICollectionResult) {
            list.clear();
            // z lewej bag
            if (leftResult instanceof ICollectionResult) {
                if (rightResult instanceof ISingleResult) {
                    if (rightResult instanceof IStructResult) {
                        // z prawej struktura
                        for (ISingleResult bagElement : leftBag.getElements()) {
                            List<ISingleResult> bagStruct = new ArrayList<>();

                            if (bagElement instanceof ISimpleResult) {
                                bagStruct.add(bagElement);
                                bagStruct.addAll(((IStructResult) rightResult).elements());
                            }
                            list.add(new StructResult(bagStruct));
                        }

                    } else {
                        //
                        for (ISingleResult bagElement : leftBag.getElements()) {
                            List<ISingleResult> bagStruct = new ArrayList<>();

                            if (bagElement instanceof ISingleResult) {
                                bagStruct.add(bagElement);
                                bagStruct.add((ISingleResult) rightResult);
                            }
                            list.add(new StructResult(bagStruct));
                        }
                    }

                } else {
                    // z prawej też bag
                    for (ISingleResult leftBagElement : leftBag.getElements()) {
                        for (ISingleResult rightBagElement : rightBag.getElements()) {
                            List<ISingleResult> bagStruct = new ArrayList<>();
                            if (leftBagElement instanceof ISimpleResult) {
                                bagStruct.add(leftBagElement);
                                bagStruct.add(rightBagElement);
                            }
                            list.add(new StructResult(bagStruct));
                        }
                    }
                }
            } else {
            // z prawej bag
                if (leftResult instanceof ISingleResult) {
                    if (leftResult instanceof IStructResult) {
                        // z lewej struktura
                        for (ISingleResult bagElement : rightBag.getElements()) {
                            List<ISingleResult> bagStruct = new ArrayList<>();

                            if (bagElement instanceof ISingleResult) {
                                bagStruct.addAll(((IStructResult) leftResult).elements());
                                bagStruct.add(bagElement);
                            }
                            list.add(new StructResult(bagStruct));
                        }

                    } else {
                        // z lewej IReference lub IBinder lub ISimple
                        for (ISingleResult bagElement : rightBag.getElements()) {
                            List<ISingleResult> bagStruct = new ArrayList<>();

                            if (bagElement instanceof ISingleResult) {
                                bagStruct.add((ISingleResult) leftResult);
                                bagStruct.add(bagElement);
                            }
                            list.add(new StructResult(bagStruct));
                        }
                    }
                }
            }
            qres.push(new BagResult(list));
        }
        else {
            qres.push(new StructResult(list));
        }
    }

    @Override
    public void visitDivideExpression(IDivideExpression expr) {

        Arguments arguments = getArgumentsForBinaryExpression(Operator.DIVIDE, expr);

        if (!arguments.mixedTypes && arguments.firstIsInteger) {
            if (arguments.secondInteger() != 0) {
                // if (a % b == 0)
                if (arguments.firstInteger() % arguments.secondInteger() == 0) {
                    // return integer
                    qres.push(new IntegerResult(arguments.firstInteger() / arguments.secondInteger()));
                }
                // return double
                else qres.push(new DoubleResult(arguments.firstAsDouble() / arguments.secondAsDouble()));
            }
        }
        else {
            if (arguments.secondAsDouble() != 0.0) {
                // return double
                qres.push(new DoubleResult(arguments.firstAsDouble() / arguments.secondAsDouble()));
            }
            else qres.push(new StringResult("NaN"));
        }
    }

    // Wykonanie prawego wyrażenia na każdym elemencie kolekcji wejsciowej
    // daje kolekcję wynikową
    @Override
    public void visitDotExpression(IDotExpression expr) {
        IExpression selection = expr.getRightExpression();
        IBagResult collection;

        expr.getLeftExpression().accept(this);
        IAbstractQueryResult bagOrReference = qres.pop();
        if (bagOrReference  instanceof IBagResult){
            collection = (IBagResult)bagOrReference;
        } else {
            ArrayList<ISingleResult> list = new ArrayList<>();
            list.add((ISingleResult)bagOrReference);
            collection = new BagResult(list);
        }

        IBagResult results = new BagResult(
            collection
                .getElements()
                .stream()
                .flatMap(x -> {
                    envs.push(envs.nested(x, store));
                    selection.accept(this);
                    IAbstractQueryResult result = qres.pop();
                    envs.pop();
                    if (result instanceof IBagResult)
                    {
                        return ((IBagResult) result).getElements().stream();
                    }
                    else
                    {
                        List<ISingleResult> elements = new ArrayList<>();
                        elements.add((ISingleResult) result);
                        return elements.stream();
                    }
                })
                .filter(x -> x != null)
                .collect(Collectors.toList())
        );

        if (results.getElements().size() == 1 && ! (bagOrReference instanceof IBagResult)) {
            qres.push(results.getElements().iterator().next());
        }else {
            qres.push(results);
        }

    }

    @Override
    public void visitEqualsExpression(IEqualsExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.EQUALS, expr);

        qres.push(new BooleanResult(arguments.firstValue().equals(arguments.secondValue())));
    }

    @Override
    public void visitGreaterThanExpression(IGreaterThanExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.GREATER, expr);

        qres.push(new BooleanResult(arguments.firstAsDouble() > arguments.secondAsDouble()));
    }

    @Override
    public void visitGreaterOrEqualThanExpression(IGreaterOrEqualThanExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.GREATER_OR_EQUAL, expr);

        qres.push(new BooleanResult(arguments.firstAsDouble() >= arguments.secondAsDouble()));
    }

    @Override
    public void visitInExpression(IInExpression expr) {
        /*
        bag(2,3) in bag(1,2,3)	'true
        1 in 1	                'true
        (1,2) in (2,3)	        'false
        (1,2) in (1,2)	        'true
         */
        BagResult collectionLeft;
        BagResult collectionRight;
        IAbstractQueryResult leftResult = getResult(expr.getLeftExpression());
        IAbstractQueryResult rightResult = getResult(expr.getRightExpression());

        if (leftResult instanceof BagResult) {
            collectionLeft = (BagResult)leftResult;
        } else  {
            collectionLeft = new BagResult(Arrays.asList((ISingleResult)leftResult));
        }

        if (rightResult instanceof BagResult) {
            collectionRight = (BagResult)rightResult;
        } else {
            collectionRight = new BagResult(Arrays.asList((ISingleResult)rightResult));
        }

        Collection<ISingleResult> scope = collectionRight.getElements();

        boolean isIN =
            // nieprawda, że istnieje element w lewej kolekcji, który...
            !Query.any(collectionLeft.getElements(), x ->
                // ...nie występuje w prawej kolekcji
                !Query.any(scope, y -> {
                    if (x instanceof IReferenceResult || y instanceof IReferenceResult){
                        if (x instanceof IReferenceResult && y instanceof IReferenceResult) {
                            return ((IReferenceResult)x).getOIDValue().equals(((IReferenceResult)y).getOIDValue());
                        }
                        else return false;
                    }
                    Object left = ((ISimpleResult) y).getValue();
                    Object right = ((ISimpleResult) x).getValue();
                    return left.equals(right);
                }));
        qres.push(new BooleanResult(isIN));
    }

    @Override
    public void visitIntersectExpression(IIntersectExpression expr) {
        IBagResult collectionLeft = getBag(expr.getRightExpression());
        IBagResult collectionRight = getBag(expr.getRightExpression());

        Collection<ISingleResult> scope = collectionRight.getElements();

        boolean isIntersect = Query.any(collectionLeft.getElements(), x ->
                Query.any(scope, y -> {
                    Object left = ((ISimpleResult) y).getValue();
                    Object right = ((ISimpleResult) x).getValue();
                    if (left instanceof Integer) left = ((Integer)left).doubleValue();
                    if (right instanceof Integer) right = ((Integer)right).doubleValue();
                    return left.equals(right);
                }));
        qres.push(new BooleanResult(isIntersect));
    }

    @Override
    public void visitJoinExpression(IJoinExpression expr) {
        IBagResult collectionLeft = getBag(expr.getLeftExpression());
        IBagResult collectionRight = getBag(expr.getRightExpression());

        Collection<ISingleResult> joinResult = new ArrayList<>(collectionLeft.getElements());
        joinResult.addAll(collectionRight.getElements());

        qres.push(new BagResult(joinResult));
    }

    @Override
    public void visitLessOrEqualThanExpression(ILessOrEqualThanExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.LESS_OR_EQUAL, expr);

        qres.push(new BooleanResult(arguments.firstAsDouble() <= arguments.secondAsDouble()));
    }

    @Override
    public void visitLessThanExpression(ILessThanExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.LESS, expr);

        qres.push(new BooleanResult(arguments.firstAsDouble() < arguments.secondAsDouble()));
    }

    @Override
    public void visitMinusExpression(IMinusExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.MINUS, expr);

        if (!arguments.mixedTypes && arguments.firstIsInteger) {
            qres.push(new IntegerResult(arguments.firstInteger() - arguments.secondInteger()));
        }
        else {
            qres.push(new DoubleResult(arguments.firstAsDouble() - arguments.secondAsDouble()));
        }
    }

    @Override
    public void visitMinusSetExpression(IMinusSetExpression expr) {

        IBagResult collectionLeft = getBag(expr.getLeftExpression());
        IBagResult collectionRight = getBag(expr.getRightExpression());

        Collection<ISingleResult> scope = collectionLeft.getElements();
        Collection<ISingleResult> scopeToSubstract = collectionRight.getElements();

        Collection<ISingleResult> result =
                scope
                .stream()
                .filter(x -> !Query.any(scopeToSubstract, y -> {
                    Object left = ((ISimpleResult) y).getValue();
                    Object right = ((ISimpleResult) x).getValue();
                    if (left instanceof Integer) left = ((Integer)left).doubleValue();
                    if (right instanceof Integer) right = ((Integer)right).doubleValue();
                    return left.equals(right);
                }))
                .collect(Collectors.toList());

        qres.push(new BagResult(result));
    }

    @Override
    public void visitModuloExpression(IModuloExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.MODULO, expr);

        qres.push(new IntegerResult(arguments.firstInteger() % arguments.secondInteger()));
    }

    @Override
    public void visitMultiplyExpression(IMultiplyExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.MULTIPLY, expr);

        if (!arguments.mixedTypes && arguments.firstIsInteger) {
            qres.push(new IntegerResult(arguments.firstInteger() * arguments.secondInteger()));
        }
        else {
            qres.push(new DoubleResult(arguments.firstAsDouble() * arguments.secondAsDouble()));
        }
    }

    @Override
    public void visitNotEqualsExpression(INotEqualsExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.NOT_EQUALS, expr);

        qres.push(new BooleanResult(!arguments.firstValue().equals(arguments.secondValue())));
    }

    @Override
    public void visitOrderByExpression(IOrderByExpression expr) {

    }

    @Override
    public void visitOrExpression(IOrExpression expr) {
        qres.push(new BooleanResult(
                getBoolean(expr.getLeftExpression())
             || getBoolean(expr.getRightExpression())
        ));
    }

    @Override
    public void visitPlusExpression(IPlusExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.PLUS, expr);

        if (!arguments.mixedTypes) {
            if (arguments.firstIsInteger) {
                qres.push(new IntegerResult(arguments.firstInteger() + arguments.secondInteger()));
            } else if (arguments.firstIsString) {
                qres.push(new StringResult(arguments.firstString() + arguments.secondString()));
            } else if (arguments.firstIsBoolean) {
                qres.push(new BooleanResult(arguments.firstBoolean() && arguments.secondBoolean()));
            }
            else {
                qres.push(new DoubleResult(arguments.firstAsDouble() + arguments.secondAsDouble()));
            }
        }
        else {
            if (arguments.firstIsString || arguments.secondIsString) {
                qres.push(new StringResult(arguments.firstAsString() + arguments.secondAsString()));
            } else if (! (arguments.firstIsDouble || arguments.secondIsDouble)) {
                qres.push(new IntegerResult(arguments.firstAsInteger() + arguments.secondAsInteger()));
            } else {
                qres.push(new DoubleResult(arguments.firstAsDouble() + arguments.secondAsDouble()));
            }
        }
    }

    @Override
    public void visitUnionExpression(IUnionExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.UNION, expr);

        Collection<ISingleResult> results = arguments.firstAsCollection();
        results.addAll(arguments.secondAsCollection());

        qres.push(new BagResult(results));
    }

    // Zachowanie analogiczne do DotExpression, ale kolekcja wynikowa
    // budowana jest z elementów kolekcji wejściowej
    @Override
    public void visitWhereExpression(IWhereExpression expr) {

        IExpression condition = expr.getRightExpression();

        IAbstractQueryResult left = getResult(expr.getLeftExpression());
        IBagResult collection = (left instanceof IBagResult)
            ? (IBagResult) left
            : new BagResult(Arrays.asList((ISingleResult) left));

        IBagResult results =
            new BagResult(
                collection.getElements()
                    .stream()
                    .filter(x -> {
                        boolean result;
                        envs.push(envs.nested(x, store));
                        IAbstractQueryResult queryResult = getResult(condition);
                        if (queryResult instanceof IBagResult) {
                            ISingleResult firstElement = ((IBagResult) queryResult).getElements().iterator().next();
                            result = getBoolean(firstElement);
                        }
                        else result = getBoolean((ISingleResult) queryResult);
                        envs.pop();
                        return result;
                    })
                    .collect(Collectors.toList())
            );

        qres.push(results);
    }

    @Override
    public void visitXORExpression(IXORExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.XOR, expr);

        qres.push(new IntegerResult(arguments.firstInteger() ^ arguments.secondInteger()));
    }

    @Override
    public void visitBooleanTerminal(IBooleanTerminal expr) {
        qres.push(new BooleanResult(expr.getValue()));
    }

    @Override
    public void visitDoubleTerminal(IDoubleTerminal expr) {
        qres.push(new DoubleResult(expr.getValue()));
    }

    @Override
    public void visitIntegerTerminal(IIntegerTerminal expr) {
        qres.push(new IntegerResult(expr.getValue()));
    }

    @Override
    public void visitNameTerminal(INameTerminal expr) {
        String name = expr.getName();
        IBagResult bagFromENVS = envs.bind(name);
        Collection<ISingleResult> values = bagFromENVS.getElements();
        IAbstractQueryResult result;

        if (values.size() == 0) {
            result = new BagResult();
        }
        else if (values.size() == 1){
            ISingleResult firstValue = values.iterator().next();

            if (firstValue instanceof IBinderResult) {
                IBinderResult firstBinder = (IBinderResult) firstValue;
                result = firstBinder.getValue();
            }
//            else if (firstValue instanceof IReferenceResult) {
//                IReferenceResult firstReference = (IReferenceResult) firstValue;
//                ISBAObject o = store.retrieve(firstReference.getOIDValue());
//                result = ISBAObjectToIAbstractQueryResult(o);
//            }
            else {
                result = firstValue;
            }
        }
        else {
            Collection<ISingleResult> collect = values.stream()
                    .map(x -> {
                        if (x instanceof IBinderResult){
                            return (ISingleResult)((IBinderResult)x).getValue();
                        }
                        else {
                            return x;
                        }
                    })
                    .collect(Collectors.toList());
            result = new BagResult(collect);
        };
        qres.push(result);
    }

    private static IAbstractQueryResult ISBAObjectToIAbstractQueryResult(ISBAObject o) {
             if (o instanceof StringObject)   return new StringResult(((StringObject) o).getValue());
        else if (o instanceof IntegerObject)  return new IntegerResult(((IntegerObject) o).getValue());
        else if (o instanceof DoubleObject)   return new DoubleResult(((DoubleObject) o).getValue());
        else if (o instanceof BooleanObject)  return new BooleanResult(((BooleanObject) o).getValue());
        else return new ReferenceResult(o.getOID());
    }

    @Override
    public void visitStringTerminal(IStringTerminal expr) {
        qres.push(new StringResult(expr.getValue()));
    }

    @Override
    public void visitBagExpression(IBagExpression expr) {
        IAbstractQueryResult result = getResult(expr.getInnerExpression());

        if (result instanceof IBagResult)
        {
            qres.push(result);
        }
        else if (result instanceof IStructResult)
        {
            qres.push(new BagResult(((IStructResult) result).elements()));
        }
        else
        {
            qres.push(new BagResult(Arrays.asList((ISingleResult)result)));
        }
    }

    @Override
    public void visitCountExpression(ICountExpression expr) {
        expr.getInnerExpression().accept(this);
        Arguments arguments = ArgumentResolver.getArguments(Operator.COUNT, qres.pop());

        int count = arguments.getElements().size();

        qres.push(new IntegerResult(count));
    }

    @Override
    public void visitExistsExpression(IExistsExpression expr) {
        boolean exists = false;
        try {
            expr.getInnerExpression().accept(this);
            IAbstractQueryResult queryResult = qres.pop();
            if (queryResult instanceof IBagResult) {
                exists = ((IBagResult)queryResult).getElements().size() > 0;
            }
            else if (queryResult != null){
                exists = true;
            }

        } catch(Exception e){
           // TODO: Złapmy tu tylko jakieś własne InvalidNameException
        }
        qres.push(new BooleanResult(exists));
    }

    @Override
    public void visitMaxExpression(IMaxExpression expr) {
        IBagResult collection = getBag(expr.getInnerExpression());

        double max = Integer.MIN_VALUE;
        for (ISingleResult item : collection.getElements()){
            if (item instanceof IIntegerResult) max = Math.max(max, (double) ((IIntegerResult) item).getValue());
            if (item instanceof IDoubleResult)  max = Math.max(max, ((IDoubleResult) item).getValue());
        }

        qres.push(new DoubleResult(max));
    }

    @Override
    public void visitMinExpression(IMinExpression expr) {
        IBagResult collection = getBag(expr.getInnerExpression());

        double min = Integer.MAX_VALUE;
        for (ISingleResult item : collection.getElements()){
            if (item instanceof IIntegerResult) min = Math.min(min, (double)((IIntegerResult)item).getValue());
            if (item instanceof IDoubleResult)  min = Math.min(min, ((IDoubleResult)item).getValue());
        }

        qres.push(new DoubleResult(min));
    }

    @Override
    public void visitNotExpression(INotExpression expr) {
        qres.push(new BooleanResult(
            ! getBoolean(expr.getInnerExpression())
        ));
    }

    @Override
    public void visitStructExpression(IStructExpression expr) {
        IExpression innerExpression = expr.getInnerExpression();
        IAbstractQueryResult result = getResult(innerExpression);

        if (innerExpression instanceof IStructExpression || innerExpression instanceof ICommaExpression) {
            qres.push(result);
        } else {
            qres.push(new StructResult(Arrays.asList((ISingleResult)result)));
        }
    }

    @Override
    public void visitSumExpression(ISumExpression expr) {
        expr.getInnerExpression().accept(this);
        Arguments arguments = ArgumentResolver.getArguments(Operator.SUM, qres.pop());
        Collection<ISingleResult> elements = arguments.getElements();

        double sum = 0;
        for (ISingleResult item : elements){
            if (item instanceof IReferenceResult){
                sum += getDouble(repository.get((IReferenceResult) item));
            }
            else sum += getDouble(item);
        }

        qres.push(new DoubleResult(sum));
    }

    @Override
    public void visitUniqueExpression(IUniqueExpression expr) {

    }

    @Override
    public void visitAvgExpression(IAvgExpression expr) {
        expr.getInnerExpression().accept(this);
        Arguments arguments = ArgumentResolver.getArguments(Operator.AVG, qres.pop());
        Collection<ISingleResult> elements = arguments.getElements();

        double sum = 0;
        for (ISingleResult item : elements){
            if (item instanceof IReferenceResult){
                sum += getDouble(repository.get((IReferenceResult) item));
            }
            else sum += getDouble(item);
        }
        double count = elements.size();

        qres.push(new DoubleResult(sum / count));
    }

    /*
     * ***************** Dodatkowe metody *************************
     */

    // Szybkie wyciąganie wartości z walidacją typów
    private Arguments getArgumentsForBinaryExpression(Operator operator, IBinaryExpression expr) {
        expr.getLeftExpression().accept(this);
        IAbstractQueryResult leftResult = qres.pop();
        expr.getRightExpression().accept(this);
        IAbstractQueryResult rightResult = qres.pop();

        return ArgumentResolver.getArguments(operator, leftResult, rightResult);
    }

    // Szybkie wyciąganie wartości z wyrażenia i rzutownie na double
    private double getDouble(IAbstractQueryResult result) {
        try {
            if (result instanceof IIntegerResult) {
                return ((IIntegerResult)result).getValue().doubleValue();
            }
            else if (result instanceof IBagResult) {
                Collection<ISingleResult> elements = ((IBagResult)result).getElements();
                if (elements.size() == 1) {
                    return getDouble(elements.iterator().next());
                } else {
                    throw new Exception("Collection is empty or has more than one element");
                }
            }
            else {
                return ((DoubleResult)result).getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
            // TODO: Nie powinniśmy zwracać nic
        }
    }

    // Szybkie wyciąganie wartości z wyrażenia i rzutownie na int
    private int getInteger(IExpression expression) {
        expression.accept(this);
        return (int)((ISimpleResult)qres.pop()).getValue();
    }

    // Szybkie wyciąganie wartości z wyrażenia i rzutownie na String
    private String getString(IExpression expression) {
        expression.accept(this);
        return ((IStringResult)qres.pop()).getValue();
    }

    // Szybkie wyciąganie wartości z wyrażenia i rzutownie na boolean
    private boolean getBoolean(IExpression expression) {
        expression.accept(this);
        return (boolean)((ISimpleResult)qres.pop()).getValue();
    }

    // Szybkie rzutownie na boolean
    private boolean getBoolean(ISingleResult result) {
        if (result instanceof IReferenceResult){
            IAbstractQueryResult value = repository.get((IReferenceResult) result);
            return (boolean)((ISimpleResult) value).getValue();
        }
        return (boolean)((ISimpleResult)result).getValue();
    }

    // Szybkie ściąganie rezultatu ze stosu bez rzutowania
    private IAbstractQueryResult getResult(IExpression expression) {
        expression.accept(this);
        return qres.pop();
    }

    // Szybkie ściąganie Baga ze stosu
    private IBagResult getBag(IExpression expression) {
        expression.accept(this);
        return (IBagResult)qres.pop();
    }
}
