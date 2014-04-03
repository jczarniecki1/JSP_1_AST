package edu.pjwstk.demo.visitor;

import edu.pjwstk.demo.common.Query;
import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.auxname.AsExpression;
import edu.pjwstk.demo.expression.binary.EqualsExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.demo.expression.unary.NotExpression;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.result.*;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.auxname.IAsExpression;
import edu.pjwstk.jps.ast.auxname.IGroupAsExpression;
import edu.pjwstk.jps.ast.binary.*;
import edu.pjwstk.jps.ast.terminal.*;
import edu.pjwstk.jps.ast.unary.*;
import edu.pjwstk.jps.interpreter.qres.IQResStack;
import edu.pjwstk.jps.result.*;
import edu.pjwstk.jps.visitor.ASTVisitor;

import java.util.*;
import java.util.stream.Collectors;

/*
    Implementacja ASTVisitora
     - odpowiada za wykonywanie wyrażeń (Expressions) i wrzucenie wyniku na stos (QRes)
     - implementacje wyrażeń nie mają żadnej logiki, są tylko "pojemnikami" na inne wyrażenia

    Postęp: 37/43 (86%)

    TODO:
     * rzutowanie (... as ...)
     * OrderBy, Exists
     * poprawka w CommaExpression

    Uwaga:
     Implementacje nie muszą uwzględniać różnych typów danych wejściowych.
     Najpierw obsłużmy najprostsze scenariusze (np. double + double)
 */

public class ConcreteASTVisitor implements ASTVisitor {

    private final IQResStack qres;
    private final IStoreRepository repository;

    public ConcreteASTVisitor(IQResStack qres,
                              IStoreRepository repository) {
        this.qres = qres;
        this.repository = repository;
    }

    @Override
    public void visitAsExpression(IAsExpression expr) {

        expr.getInnerExpression().accept(this);
        IAbstractQueryResult result = qres.pop();
        String name = expr.getAuxiliaryName();
        if (result instanceof ISingleResult) {
            qres.push(new BinderResult(result, name));
        }
        if (result instanceof ICollectionResult){
            List<ISingleResult> binders = new ArrayList<>();
            IBagResult bagResults = (IBagResult) result;
            for (ISingleResult bagResult : bagResults.getElements()){
                binders.add(new BinderResult(bagResult,name));
            }
            qres.push(new BagResult(binders));
        }
    }

    @Override
    public void visitGroupAsExpression(IGroupAsExpression expr) {
        // TODO Zaimplementować GroupAS
        expr.getInnerExpression().accept(this);
        IAbstractQueryResult result = qres.pop();
        String name = expr.getAuxiliaryName();
        qres.push(new BinderResult(result, name));
    }

    // All() jako odwrócone Any()
    @Override
    public void visitAllExpression(IForAllExpression expr) {
        IExpression condition = expr.getRightExpression();

        expr.getLeftExpression().accept(this);
        IBagResult collection = (IBagResult)qres.pop();

        IBooleanResult results = new BooleanResult(
            ! Query.any(collection.getElements(), x -> {
                qres.push(x);
                condition.accept(this);
                return ! ((IBooleanResult) qres.pop()).getValue();
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

        expr.getLeftExpression().accept(this);
        IBagResult collection = (IBagResult)qres.pop();

        BooleanResult results = new BooleanResult(
            Query.any(collection.getElements(), x -> {
                qres.push(x);
                condition.accept(this);
                IBooleanResult result = (IBooleanResult) qres.pop();
                return result.getValue();
            }));

        qres.push(results);
    }

    @Override
    public void visitCloseByExpression(ICloseByExpression expr) {

    }

    // TODO: To ma zwracać StructResult, a nie BagResult (CW2-QRES.pdf)
    // w CW2-QRES chyba ta informacja, że operator comma tworzy struktury był mylący,
    // bo przykłady poniżej pokazują, że jednak jak w wyrażeniu są bagi to na wyjściu
    // wychodzą bagi struktur.
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
        double left = getDouble(expr.getLeftExpression());
        double right = getDouble(expr.getRightExpression());
        if (right != 0) {
            qres.push(new DoubleResult(left / right));
        }
        else qres.push(new StringResult("NaN"));
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
            list.add((IReferenceResult) bagOrReference);
            collection = new BagResult(list);
        }

        IBagResult results = new BagResult(
            collection
                .getElements()
                .stream()
                .map(x -> {
                    qres.push(x);
                    selection.accept(this);
                    IAbstractQueryResult result = qres.pop();
                    if (result instanceof IBagResult) {
                        Collection<ISingleResult> elements = ((IBagResult) result).getElements();
                        return elements.size() > 0
                            ? elements.iterator().next()
                            : null;
                    }
                    else
                        return (ISingleResult) result;
                })
                .filter(x -> x != null)
                .collect(Collectors.toList())
        );

        qres.push(results);
    }

    @Override
    public void visitEqualsExpression(IEqualsExpression expr) {
        String left = getString(expr.getLeftExpression());
        String right = getString(expr.getRightExpression());
        qres.push(new BooleanResult(left.equals(right)));
    }

    @Override
    public void visitGreaterThanExpression(IGreaterThanExpression expr) {
        double left = getDouble(expr.getLeftExpression());
        double right = getDouble(expr.getRightExpression());

        qres.push(new BooleanResult(left > right));
    }

    @Override
    public void visitGreaterOrEqualThanExpression(IGreaterOrEqualThanExpression expr) {
        double left = getDouble(expr.getLeftExpression());
        double right = getDouble(expr.getRightExpression());

        qres.push(new BooleanResult(left >= right));

    }

    @Override
    public void visitInExpression(IInExpression expr) {
        /*
        Uwaga 1: sugerowana implementacja ma działać na referencjach, nie na wartościach;
        przykładowo dwa łańcuchy str1 = „abc” i str2 = ”abc” są z punktu widzenia
        tych operatorów różne od siebie bo są różnymi obiektami.
         */
        BagResult collectionLeft;
        BagResult collectionRight;
        Expression leftExpr = (Expression) expr.getLeftExpression();
        leftExpr.accept(this);
        IAbstractQueryResult leftResult = qres.pop();

        Expression rightExpr = (Expression) expr.getRightExpression();
        rightExpr.accept(this);
        IAbstractQueryResult rightResult = qres.pop();

        if (leftResult instanceof BagResult) {
            collectionLeft = (BagResult)leftResult;
        } else  {

            leftExpr = new BagExpression(leftExpr);
            leftExpr.accept(this);
            leftResult = qres.pop();
            collectionLeft = (BagResult)leftResult;
        }


        if (rightResult instanceof BagResult) {
            // jeżeli na wejściu jest Bag
            collectionRight = (BagResult)rightResult;
        } else {

                rightExpr = new BagExpression(rightExpr);
                rightExpr.accept(this);
                rightResult = qres.pop();
                collectionRight = (BagResult)rightResult;
        }


        Collection<ISingleResult> scope = collectionRight.getElements();


        boolean isIN = !Query.any(collectionLeft.getElements(), x ->
                !Query.any(scope, y -> {
                    Object left = ((ISimpleResult) y).getValue();
                    Object right = ((ISimpleResult) x).getValue();

                    if (left instanceof Integer) left = ((Integer)left).doubleValue();
                    if (right instanceof Integer) right = ((Integer)right).doubleValue();
                    return left.equals(right);
                }));
        qres.push(new BooleanResult(isIN));
    }

    @Override
    public void visitIntersectExpression(IIntersectExpression expr) {
        expr.getLeftExpression().accept(this);
        IBagResult collectionLeft = (IBagResult)qres.pop();


        expr.getRightExpression().accept(this);
        IBagResult collectionRight = (IBagResult)qres.pop();

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
        expr.getLeftExpression().accept(this);
        IBagResult collectionLeft = (IBagResult)qres.pop();


        expr.getRightExpression().accept(this);
        IBagResult collectionRight = (IBagResult)qres.pop();

        Collection<ISingleResult> joinResult = new ArrayList<>(collectionLeft.getElements());
        joinResult.addAll(collectionRight.getElements());

        qres.push(new BagResult(joinResult));
    }

    @Override
    public void visitLessOrEqualThanExpression(ILessOrEqualThanExpression expr) {
        double left = getDouble(expr.getLeftExpression());
        double right = getDouble(expr.getRightExpression());

        qres.push(new BooleanResult(left <= right));

    }

    @Override
    public void visitLessThanExpression(ILessThanExpression expr) {
        double left = getDouble(expr.getLeftExpression());
        double right = getDouble(expr.getRightExpression());

        qres.push(new BooleanResult(left < right));
    }

    @Override
    public void visitMinusExpression(IMinusExpression expr) {
        double left = getDouble(expr.getLeftExpression());
        double right = getDouble(expr.getRightExpression());
        qres.push(new DoubleResult(left-right));
    }

    @Override
    public void visitMinusSetExpression(IMinusSetExpression expr) {

        expr.getLeftExpression().accept(this);
        IBagResult collectionLeft = (IBagResult)qres.pop();

        expr.getRightExpression().accept(this);
        IBagResult collectionRight = (IBagResult)qres.pop();

        Collection<ISingleResult> scope = collectionLeft.getElements();
        Collection<ISingleResult> scopeToSubstract = collectionRight.getElements();

        Collection<ISingleResult> result = new ArrayList<>();
        result.addAll(
                scope
                .stream()
                .filter(x -> !Query.any(scopeToSubstract, y -> {
                    Object left = ((ISimpleResult) y).getValue();
                    Object right = ((ISimpleResult) x).getValue();
                    if (left instanceof Integer) left = ((Integer)left).doubleValue();
                    if (right instanceof Integer) right = ((Integer)right).doubleValue();
                    return left.equals(right);
                }))
                .collect(Collectors.toList())
        );

        qres.push(new BagResult(result));
    }

    //TODO: Potrzebujemy testów do tego
    @Override
    public void visitModuloExpression(IModuloExpression expr) {
        double left = getDouble(expr.getLeftExpression());
        double right = getDouble(expr.getRightExpression());
        qres.push(new DoubleResult(left % right));
    }

    @Override
    public void visitMultiplyExpression(IMultiplyExpression expr) {
        double left = getDouble(expr.getLeftExpression());
        double right = getDouble(expr.getRightExpression());
        qres.push(new DoubleResult(left * right));
    }

    @Override
    public void visitNotEqualsExpression(INotEqualsExpression expr) {
        new NotExpression(
            new EqualsExpression(expr.getLeftExpression(), expr.getRightExpression())
        ).accept(this);
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
        expr.getLeftExpression().accept(this);
        IAbstractQueryResult leftValue = qres.pop();
        expr.getRightExpression().accept(this);
        IAbstractQueryResult rightValue = qres.pop();

        if (leftValue instanceof IntegerResult
         && rightValue instanceof IntegerResult) {
            qres.push(new IntegerResult(((IntegerResult) leftValue).getValue()+((IntegerResult) rightValue).getValue()));
        } else if (leftValue instanceof StringResult || rightValue instanceof StringResult) {
            qres.push(new StringResult(leftValue.toString() + rightValue.toString()));
        } else {
            qres.push(new DoubleResult(getDouble(leftValue) + getDouble(rightValue)));
        }
    }

    // TODO: A co powinno się stać dla (1, 1, 1, 3) union (2, 2, 4) ? (1, 3, 2, 4) ?
    @Override
    public void visitUnionExpression(IUnionExpression expr) {
        expr.getLeftExpression().accept(this);
        IBagResult collectionLeft = (IBagResult)qres.pop();

        expr.getRightExpression().accept(this);
        IBagResult collectionRight = (IBagResult)qres.pop();

        Collection<ISingleResult> scope = collectionLeft.getElements();
        Collection<ISingleResult> unionResult = new ArrayList<>(scope);
        unionResult.addAll(
                collectionRight.getElements()
                .stream()
                .filter(x -> !Query.any(scope, y -> {
                    Object left = ((ISimpleResult) y).getValue();
                    Object right = ((ISimpleResult) x).getValue();
                    if (left instanceof Integer) left = ((Integer)left).doubleValue();
                    if (right instanceof Integer) right = ((Integer)right).doubleValue();
                    return left.equals(right);
                }))
                .collect(Collectors.toList())
        );

        qres.push(new BagResult(unionResult));
    }

    // Zachowanie analogiczne do DotExpression, ale kolekcja wynikowa
    // budowana jest z elementów kolekcji wejściowej
    @Override
    public void visitWhereExpression(IWhereExpression expr) {

        IExpression condition = expr.getRightExpression();

        expr.getLeftExpression().accept(this);
        IAbstractQueryResult left = qres.pop();
        IBagResult collection = (left instanceof IBagResult)
            ? (IBagResult) left
            : new BagResult(Arrays.asList((ISingleResult) left));

        IBagResult results =
            Query.where(collection, x -> {
                IBooleanResult result;
                qres.push(x);
                condition.accept(this);
                IAbstractQueryResult queryResult = qres.pop();
                if (queryResult instanceof IBagResult) {
                    result = (IBooleanResult)((IBagResult)queryResult).getElements().iterator().next();
                }
                else result = (IBooleanResult) queryResult;
                return result.getValue();
            });

        qres.push(results);
    }

    @Override
    public void visitXORExpression(IXORExpression expr) {
        int left = getInteger(expr.getLeftExpression());
        int right = getInteger(expr.getRightExpression());
        qres.push(new IntegerResult(left ^ right));
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

    // Jeśli jest coś na stosie, to wybierz obiekty z Bag-a, albo z kolekcji
    // danej referencją (IReferenceResult)
    // w przeciwnym wypadku zdobądź obiekty z bazy (repository.getCollection())
    @Override
    public void visitNameTerminal(INameTerminal expr) {
        IAbstractQueryResult result;
        String name = expr.getName();

        IAbstractQueryResult lastValue = qres.pop();
        if (lastValue == null)
        {
            result = new BagResult(repository.getCollection(name));
            qres.push(result);
        }
        else
        {
            IAbstractQueryResult input = lastValue;
            if (input instanceof IBagResult)
            {
                Collection collection = ((IBagResult) input).getElements();
                List values = Query.select(collection, x -> repository.getField((IReferenceResult) x, name));
//                var values = collection.map(function(i, x){
//                    return repository.getField((IReferenceResult) x, name);
//                });
                result = new BagResult(values);
            }
            else
            {
                result = repository.getFields(((IReferenceResult) input), name);
            }
            qres.push(result);
        }
    }

    @Override
    public void visitStringTerminal(IStringTerminal expr) {
        qres.push(new StringResult(expr.getValue()));
    }

    @Override
    public void visitBagExpression(IBagExpression expr) {
        expr.getInnerExpression().accept(this);
        IAbstractQueryResult result = qres.pop(); 
        if (result instanceof IBagResult)
        {
            qres.push(result);
        }
        else if (result instanceof IStructResult){
            Collection<ISingleResult> list = new ArrayList<>();
            for( ISingleResult element: ((IStructResult) result).elements()) {
                list.add(element);
            }
            qres.push(new BagResult(list));
        } else  {
            Collection<ISingleResult> list = new ArrayList<>();
            list.add((ISingleResult)result);
            qres.push(new BagResult(list));
        }
    }

    @Override
    public void visitCountExpression(ICountExpression expr) {

        expr.getInnerExpression().accept(this);
        IBagResult collection = (IBagResult)qres.pop();

        int count = collection.getElements().size();

        qres.push(new IntegerResult(count));
    }

    @Override
    public void visitExistsExpression(IExistsExpression expr) {

    }

    @Override
    public void visitMaxExpression(IMaxExpression expr) {
        expr.getInnerExpression().accept(this);
        IBagResult collection = (IBagResult)qres.pop();

        double max = Integer.MIN_VALUE;
        for (ISingleResult item : collection.getElements()){
            if (item instanceof IIntegerResult) max = Math.max(max, (double) ((IIntegerResult) item).getValue());
            if (item instanceof IDoubleResult)  max = Math.max(max, ((IDoubleResult) item).getValue());
        }

        qres.push(new DoubleResult(max));
    }

    @Override
    public void visitMinExpression(IMinExpression expr) {
        expr.getInnerExpression().accept(this);
        IBagResult collection = (IBagResult)qres.pop();

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
        //TODO Zaimplementować StructExpression
        expr.getInnerExpression().accept(this);
        IAbstractQueryResult result = qres.pop();
        if (result instanceof IStructResult)
        {
            qres.push(result);
        } else  {
            List<ISingleResult> list = new ArrayList<>();
            list.add((ISingleResult)result);
            qres.push(new StructResult(list));
        }


    }

    // Dla uproszczenia rzutujemy wszystko na double
    @Override
    public void visitSumExpression(ISumExpression expr) {
        expr.getInnerExpression().accept(this);
        IBagResult collection = (IBagResult)qres.pop();

        double sum = 0;
        for (ISingleResult item : collection.getElements()){
            sum += (double)((ISimpleResult)item).getValue();
        }

        qres.push(new DoubleResult(sum));
    }

    @Override
    public void visitUniqueExpression(IUniqueExpression expr) {

    }

    @Override
    public void visitAvgExpression(IAvgExpression expr) {

        expr.getInnerExpression().accept(this);
        IBagResult collection = (IBagResult)qres.pop();
        double sum = Query.aggregate(collection.getElements(), 0.0,
                (current, x) -> current + ((DoubleResult) x).getValue());
        double count = collection.getElements().size();

        qres.push(new DoubleResult(sum / count));
    }

    // Szybkie wyciąganie wartości z wyrażenia i rzutownie na double
    private double getDouble(IExpression expression) {
        expression.accept(this);
        return getDouble(qres.pop());
    }
    // Szybkie wyciąganie wartości z wyrażenia i rzutownie na double
    private double getDouble(IAbstractQueryResult result) {
        if (result instanceof IntegerResult) {
            return ((IntegerResult)result).getValue().doubleValue();
        }
        else {
            return ((DoubleResult)result).getValue();
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
}
