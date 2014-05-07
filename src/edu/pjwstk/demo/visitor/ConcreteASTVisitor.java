package edu.pjwstk.demo.visitor;

import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.interpreter.envs.ENVS;
import edu.pjwstk.demo.result.*;
import edu.pjwstk.demo.visitor.helpers.ArgumentResolver;
import edu.pjwstk.demo.visitor.helpers.Arguments;
import edu.pjwstk.demo.visitor.helpers.Operator;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.auxname.IAsExpression;
import edu.pjwstk.jps.ast.auxname.IAuxiliaryNameExpression;
import edu.pjwstk.jps.ast.auxname.IGroupAsExpression;
import edu.pjwstk.jps.ast.binary.*;
import edu.pjwstk.jps.ast.terminal.*;
import edu.pjwstk.jps.ast.unary.*;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.interpreter.qres.IQResStack;
import edu.pjwstk.jps.result.*;
import edu.pjwstk.jps.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
    Implementacja ASTVisitora
     - odpowiada za wykonywanie wyrażeń (Expressions)
       i wrzucenie wyniku na stos (QRes)

    Postęp: 41/43 (93%)

    TODO:
     * OrderBy, CloseBy
     * Poprawki dla Comma, Join
 */

public class ConcreteASTVisitor implements ASTVisitor {

    private final ENVS envs;

    private final IQResStack qres;
    private final IStoreRepository repository;
    private final ISBAStore store;

    public ConcreteASTVisitor(IQResStack qres, IStoreRepository repository) {

        // Zapisanie podanego qres i repozytorium
        // Inicjalizacja ENVS
        //   (wymaga SBAStore, więc repository ma jedną nadmiarową metodę)

        this.qres = qres;
        this.repository = repository;
        envs = new ENVS();
        store = repository.getStore();
        envs.init(store.getEntryOID(), store);
    }

    @Override
    public void visitAsExpression(IAsExpression expr) {
        Arguments arguments = getArgumentsForAuxiliaryExpression(Operator.AS, expr);
        Collection<ISingleResult> elements = arguments.getAsCollection();

        String name = expr.getAuxiliaryName();

        if (elements.size() == 1) {
            ISingleResult result = elements.iterator().next();

            qres.push(new BinderResult(result, name));
        }
        else {
            List<ISingleResult> binders = elements
                .stream()
                .map(x -> new BinderResult(x, name))
                .collect(Collectors.toList());

            qres.push(new BagResult(binders));
        }
    }

    @Override
    public void visitGroupAsExpression(IGroupAsExpression expr) {
        Arguments arguments = getArgumentsForAuxiliaryExpression(Operator.GROUP_AS, expr);

        IAbstractQueryResult result = arguments.get();
        String name = expr.getAuxiliaryName();

        qres.push(new BinderResult(result, name));
    }

    @Override
    public void visitAllExpression(IForAllExpression expr) {

        Arguments arguments = getArgumentsForBinaryExpressionWithCondition(Operator.FOR_ALL, expr.getLeftExpression());
        IExpression condition = expr.getRightExpression();

        IBooleanResult results = new BooleanResult(
            arguments.getAsCollection().stream()
                .allMatch(x -> {
                    envs.push(envs.nested(x, store));
                    boolean result = getBoolean(condition);
                    envs.pop();
                    return result;
                }));

        qres.push(results);
    }

    @Override
    public void visitAndExpression(IAndExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.AND, expr);

        qres.push(new BooleanResult(
                arguments.firstAsBoolean()
             && arguments.secondAsBoolean())
        );
    }

    @Override
    public void visitAnyExpression(IForAnyExpression expr) {

        Arguments arguments = getArgumentsForBinaryExpressionWithCondition(Operator.FOR_ANY, expr.getLeftExpression());
        IExpression condition = expr.getRightExpression();

        IBooleanResult results = new BooleanResult(
            arguments.getAsCollection().stream()
                .anyMatch(x -> {
                    envs.push(envs.nested(x, store));
                    boolean result = getBoolean(condition);
                    envs.pop();
                    return result;
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

    @Override
    public void visitDotExpression(IDotExpression expr) {

        // Wykonanie prawego wyrażenia na każdym elemencie kolekcji wejsciowej
        // daje kolekcję wynikową

        Arguments arguments = getArgumentsForBinaryExpressionWithCondition(Operator.DOT, expr.getLeftExpression());
        IExpression selection = expr.getRightExpression();

        Collection<ISingleResult> results =
            arguments.getAsCollection().stream()
                .flatMap(x -> {
                    envs.push(envs.nested(x, store));                       // Wrzuć obiekt na ENVS
                    selection.accept(this);                                 // Wykonaj wyrażenie po prawej
                    IAbstractQueryResult result = qres.pop();               // Sciągnij wynik
                    envs.pop();                                             // Zdejmij z ENVS

                    return streamResult(result);
                })
                .filter(x -> x != null)
                .collect(Collectors.toList());

        qres.push(new BagResult(results));
    }

    @Override
    public void visitEqualsExpression(IEqualsExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.EQUALS, expr);

        boolean equals = arguments.firstValue().equals(arguments.secondValue());

        qres.push(new BooleanResult(equals));
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
        Arguments arguments = getArgumentsForBinaryExpression(Operator.IN, expr);

        Collection<ISingleResult> leftCollection  = arguments.firstAsCollection();
        Collection<ISingleResult> rightCollection = arguments.secondAsCollection();

        boolean isIN =
            // każdy element lewej kolekcji
            leftCollection.stream().allMatch(x ->
                // ... występuje w prawej kolekcji
                rightCollection.stream().anyMatch(x::equals));

        qres.push(new BooleanResult(isIN));
    }

    @Override
    public void visitIntersectExpression(IIntersectExpression expr) {

        // Dlaczego u licha IIntersectExpression nie dziedziczy po IBinaryExpression ???
        // TODO: wyrzucić getArgumentsForIntersectExpression i zastąpić wersją dla IBinaryExpression

        Arguments arguments = getArgumentsForIntersectExpression(Operator.INTERSECT, expr);

        Collection<ISingleResult> leftCollection  = arguments.firstAsCollection();
        Collection<ISingleResult> rightCollection = arguments.secondAsCollection();

        Collection<ISingleResult> result =
            leftCollection.stream().filter(x ->
                rightCollection.stream().anyMatch(x::equals))
            .distinct()
            .collect(Collectors.toList());

        qres.push(new BagResult(result));
    }

    @Override
    public void visitJoinExpression(IJoinExpression expr) {

        // TODO: Poprawić JOIN

        Arguments arguments = getArgumentsForBinaryExpression(Operator.JOIN, expr);

        Collection<ISingleResult> leftCollection  = arguments.firstAsCollection();
        Collection<ISingleResult> rightCollection = arguments.secondAsCollection();

        List<ISingleResult> results = new ArrayList<>(leftCollection);
        results.addAll(rightCollection);

        qres.push(new BagResult(results));
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
        Arguments arguments = getArgumentsForBinaryExpression(Operator.MINUS_SET, expr);

        Collection<ISingleResult> scope = arguments.firstAsCollection();
        Collection<ISingleResult> scopeToSubstract = arguments.secondAsCollection();

        Collection<ISingleResult> result = scope.stream()
                .filter(x -> !scopeToSubstract.stream().anyMatch(x::equals))
                .distinct()
                .collect(Collectors.toList());

        if (result.isEmpty())
        {
            qres.push(BagResult.Empty());
        }
        else if (arguments.firstIsCollection)
        {
            qres.push(new BagResult(result));
        }
        else
        {
            ISingleResult firstValue = result.iterator().next();
            qres.push(firstValue);
        }
    }

    @Override
    public void visitModuloExpression(IModuloExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.MODULO, expr);

        if (!arguments.mixedTypes && arguments.firstIsInteger) {
            // return Integer
            qres.push(new IntegerResult(arguments.firstInteger() % arguments.secondInteger()));
        }
        else {
            // return Double
            qres.push(new DoubleResult(arguments.firstAsDouble() % arguments.secondAsDouble()));
        }
    }

    @Override
    public void visitMultiplyExpression(IMultiplyExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.MULTIPLY, expr);

        if (!arguments.mixedTypes && arguments.firstIsInteger) {
            // return Integer
            qres.push(new IntegerResult(arguments.firstInteger() * arguments.secondInteger()));
        }
        else {
            // return Double
            qres.push(new DoubleResult(arguments.firstAsDouble() * arguments.secondAsDouble()));
        }
    }

    @Override
    public void visitNotEqualsExpression(INotEqualsExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.NOT_EQUALS, expr);

        qres.push(new BooleanResult(! arguments.firstValue().equals(arguments.secondValue())));
    }

    @Override
    public void visitOrderByExpression(IOrderByExpression expr) {

    }

    @Override
    public void visitOrExpression(IOrExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.OR, expr);

        qres.push(new BooleanResult(
            arguments.firstAsBoolean()
         || arguments.secondAsBoolean())
        );
    }

    @Override
    public void visitPlusExpression(IPlusExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.PLUS, expr);

        if (!arguments.mixedTypes) {
            if (arguments.firstIsInteger) {
                // return Integer
                qres.push(new IntegerResult(arguments.firstInteger() + arguments.secondInteger()));
            } else if (arguments.firstIsString) {
                // return String
                qres.push(new StringResult(arguments.firstString() + arguments.secondString()));
            } else if (arguments.firstIsBoolean) {
                // return Boolean
                qres.push(new BooleanResult(arguments.firstBoolean() && arguments.secondBoolean()));
            }
            else {
                // return Double
                qres.push(new DoubleResult(arguments.firstAsDouble() + arguments.secondAsDouble()));
            }
        }
        else {
            if (arguments.firstIsString || arguments.secondIsString) {
                // return String
                qres.push(new StringResult(arguments.firstAsString() + arguments.secondAsString()));
            } else if (! (arguments.firstIsDouble || arguments.secondIsDouble)) {
                // return Integer
                qres.push(new IntegerResult(arguments.firstAsInteger() + arguments.secondAsInteger()));
            } else {
                // return Double
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

    @Override
    public void visitWhereExpression(IWhereExpression expr) {

        // Zachowanie analogiczne do DotExpression, ale kolekcja wynikowa
        // budowana jest z elementów kolekcji wejściowej

        Arguments arguments = getArgumentsForBinaryExpressionWithCondition(Operator.WHERE, expr.getLeftExpression());
        IExpression condition = expr.getRightExpression();

        Collection<ISingleResult> results =
            arguments.getAsCollection().stream()
                .filter(x -> {
                    envs.push(envs.nested(x, store));              // Wrzuć obiekt na ENVS
                    boolean result = getBoolean(condition);        // Wykonaj wyrażenie z prawej i ściągnij wynik
                    envs.pop();                                    // Zdejmij obiekt z ENVS

                    return result;
                })
                .collect(Collectors.toList());

        qres.push(new BagResult(results));
    }

    @Override
    public void visitXORExpression(IXORExpression expr) {
        Arguments arguments = getArgumentsForBinaryExpression(Operator.XOR, expr);

        qres.push(new BooleanResult(
            arguments.firstAsBoolean()
          ^ arguments.secondAsBoolean()
        ));

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

        // NameExpression operuje na stosie ENVS
        //  - nie odwołuje się już więcej do SBAStore

        String name = expr.getName();
        Collection<ISingleResult> valuesFromENVS = envs.bind(name).getElements();
        IAbstractQueryResult result;

        if (valuesFromENVS.isEmpty()) {
            result = BagResult.Empty();
        }
        else if (valuesFromENVS.size() == 1){
            // first (and only) value from ENVS
            result = valuesFromENVS.iterator().next();
        }
        else result = new BagResult(getWithoutBinders(valuesFromENVS));

        qres.push(result);
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

        int count = arguments.getAsCollection().size();
        qres.push(new IntegerResult(count));
    }

    @Override
    public void visitExistsExpression(IExistsExpression expr) {
        Arguments arguments = getArgumentsForUnaryExpression(Operator.EXISTS, expr);
        Collection<ISingleResult> elements = arguments.getAsCollection();

        boolean exists = ! (elements.isEmpty()
            || (elements.size() == 1 && elements.iterator().next() == null)
        );

        qres.push(new BooleanResult(exists));
    }

    @Override
    public void visitMaxExpression(IMaxExpression expr) {

        // Oblicza oddzielnie maxInt i maxDouble
        // żeby wiedzieć jaki typ zwrócić

        Arguments arguments = getArgumentsForUnaryExpression(Operator.MAX, expr);
        Collection<ISingleResult> elements = arguments.getAsCollection();

        if (elements.size() == 0) {
            qres.push(BagResult.Empty());
            return;
        }
        Double maxDouble = Double.MIN_VALUE;
        Integer maxInt = Integer.MIN_VALUE;
        for (ISingleResult item : elements){
            if (item instanceof IIntegerResult) maxInt = Math.max(maxInt, ((IIntegerResult) item).getValue());
            if (item instanceof IDoubleResult)  maxDouble = Math.max(maxDouble, ((IDoubleResult) item).getValue());
        }

        if (maxDouble > maxInt.doubleValue())
            qres.push(new DoubleResult(maxDouble));
        else
            qres.push(new IntegerResult(maxInt));
    }

    @Override
    public void visitMinExpression(IMinExpression expr) {

        // Oblicza oddzielnie minInt i minDouble
        // żeby wiedzieć jaki typ zwrócić

        Arguments arguments = getArgumentsForUnaryExpression(Operator.MIN, expr);
        Collection<ISingleResult> elements = arguments.getAsCollection();

        if (elements.size() == 0) {
            qres.push(BagResult.Empty());
            return;
        }
        Double minDouble = Double.MAX_VALUE;
        Integer minInt = Integer.MAX_VALUE;
        for (ISingleResult item : elements){
            if (item instanceof IIntegerResult) minInt = Math.min(minInt, ((IIntegerResult) item).getValue());
            if (item instanceof IDoubleResult)  minDouble = Math.min(minDouble, ((IDoubleResult) item).getValue());
        }

        if (minDouble < minInt.doubleValue())
            qres.push(new DoubleResult(minDouble));
        else
            qres.push(new IntegerResult(minInt));
    }

    @Override
    public void visitNotExpression(INotExpression expr) {
        Arguments arguments = getArgumentsForUnaryExpression(Operator.NOT, expr);

        qres.push(new BooleanResult(! arguments.getBoolean()));
    }

    @Override
    public void visitStructExpression(IStructExpression expr) {
        IExpression innerExpression = expr.getInnerExpression();
        IAbstractQueryResult result = getResult(innerExpression);

        // Jeśli na wejściu jest StructExpression albo CommaExpression, to zignoruj
        if (innerExpression instanceof IStructExpression || innerExpression instanceof ICommaExpression) {
            qres.push(result);
        } else {
            qres.push(new StructResult(Arrays.asList((ISingleResult)result)));
        }
    }

    @Override
    public void visitSumExpression(ISumExpression expr) {

        // Sumuje oddzielnie sumInt i sumDouble
        // żeby wiedzieć jaki typ zwrócić

        Arguments arguments = getArgumentsForUnaryExpression(Operator.SUM, expr);
        Collection<ISingleResult> elements = arguments.getAsCollection();

        Double sumDouble = 0.0;
        Integer sumInt = 0;
        for (ISingleResult item : elements){
            if (item instanceof IReferenceResult){
                IAbstractQueryResult resultInStore = repository.get((IReferenceResult) item);
                if (resultInStore instanceof IIntegerResult) sumInt += ((IIntegerResult) resultInStore).getValue();
                else sumDouble += getDouble(resultInStore);
            }
            else {
                if (item instanceof IIntegerResult) sumInt += ((IIntegerResult) item).getValue();
                else sumDouble += getDouble(item);
            }
        }
        if (sumDouble != 0.0){
            qres.push(new DoubleResult(sumDouble + sumInt.doubleValue()));
        }
        else qres.push(new IntegerResult(sumInt));
    }

    @Override
    public void visitUniqueExpression(IUniqueExpression expr) {

        // Zwraca kolekcję unikalnych wartości z baga

        Arguments arguments = getArgumentsForUnaryExpression(Operator.UNIQUE, expr);
        Collection<ISingleResult> elements = arguments.getAsCollection();

        Collection<ISingleResult> uniqueElements = new ArrayList<>();
        for (ISingleResult e :elements) {
            if (uniqueElements.size()==0 || !uniqueElements.contains(e) )
                uniqueElements.add(e);
        }

        qres.push(new BagResult(uniqueElements));
    }

    @Override
    public void visitAvgExpression(IAvgExpression expr) {

        Arguments arguments = getArgumentsForUnaryExpression(Operator.AVG, expr);
        Collection<ISingleResult> elements = arguments.getAsCollection();

        if (elements.isEmpty()) {
            qres.push(BagResult.Empty());
            return;
        }
        if (elements.size() == 1) {
            qres.push(arguments.get());
            return;
        }
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

    //
    // Szybkie wyciąganie wartości z walidacją typów
    //
    private Arguments getArgumentsForAuxiliaryExpression(Operator operator, IAuxiliaryNameExpression expr) {
        expr.getInnerExpression().accept(this);
        IAbstractQueryResult result = qres.pop();

        return ArgumentResolver.getArguments(operator, result);
    }

    private Arguments getArgumentsForUnaryExpression(Operator operator, IUnaryExpression expr) {
        expr.getInnerExpression().accept(this);
        IAbstractQueryResult result = qres.pop();

        return ArgumentResolver.getArguments(operator, result);
    }

    private Arguments getArgumentsForBinaryExpression(Operator operator, IBinaryExpression expr) {
        expr.getLeftExpression().accept(this);
        IAbstractQueryResult leftResult = qres.pop();
        expr.getRightExpression().accept(this);
        IAbstractQueryResult rightResult = qres.pop();

        return ArgumentResolver.getArguments(operator, leftResult, rightResult);
    }

    private Arguments getArgumentsForBinaryExpressionWithCondition(Operator operator, IExpression leftExpression) {
        leftExpression.accept(this);
        IAbstractQueryResult leftResult = qres.pop();

        return ArgumentResolver.getArguments(operator, leftResult);
    }

    private Arguments getArgumentsForIntersectExpression(Operator operator, IIntersectExpression expr) {
        expr.getLeftExpression().accept(this);
        IAbstractQueryResult leftResult = qres.pop();
        expr.getRightExpression().accept(this);
        IAbstractQueryResult rightResult = qres.pop();

        return ArgumentResolver.getArguments(operator, leftResult, rightResult);
    }

    //
    // Szybkie wyciąganie wartości z wyrażenia i rzutownie na Double/Boolean
    //
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

    private boolean getBoolean(IExpression expression) {
        expression.accept(this);
        IAbstractQueryResult queryResult = qres.pop();

        // Jeśli to bag, to pobierz pierwszy element
        if (queryResult instanceof IBagResult){
            Collection<ISingleResult> elements = ((IBagResult) queryResult).getElements();
            if (elements.size() > 0) {
                queryResult = elements.iterator().next();
            } else {
                return false;
            }
        }
        // Dereferencja
        if (queryResult instanceof IReferenceResult){
            queryResult = repository.get((IReferenceResult) queryResult);
        }
        // Jak teraz to jest wartość prosta, to zwróć ją
        if (queryResult instanceof ISimpleResult) {
            return (boolean)((ISimpleResult) queryResult).getValue();
        }
        else return false;
    }

    private IAbstractQueryResult getResult(IExpression expression) {
        expression.accept(this);
        return qres.pop();
    }

    // Streaming wartości
    private Stream<ISingleResult> streamResult(IAbstractQueryResult result) {
        if (result instanceof IBagResult)
        {
            return ((IBagResult) result).getElements().stream();
        }
        else if (result instanceof ISequenceResult)
        {
            return ((ISequenceResult) result).getElements().stream();
        }
        else
        {
            return (new ArrayList<>(Arrays.asList((ISingleResult) result))).stream();
        }
    }

    // Pozbycie się binderów z kolekcji
    private List<ISingleResult> getWithoutBinders(Collection<ISingleResult> elements) {
        return elements
            .stream()
            .map(this::unbindValue)
            .collect(Collectors.toList());
    }

    // Pozbycie się bindera z wyniku
    private ISingleResult unbindValue(ISingleResult value) {
        if (value instanceof IBinderResult) {
            return (ISingleResult)((IBinderResult) value).getValue();
        }
        else return value;
    }
}
