package edu.pjwstk.demo.visitor;

import edu.pjwstk.demo.common.Query;
import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.expression.binary.EqualsExpression;
import edu.pjwstk.demo.expression.unary.CountExpression;
import edu.pjwstk.demo.expression.unary.NotExpression;
import edu.pjwstk.demo.expression.unary.SumExpression;
import edu.pjwstk.demo.result.*;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.auxname.IAsExpression;
import edu.pjwstk.jps.ast.auxname.IGroupAsExpression;
import edu.pjwstk.jps.ast.binary.*;
import edu.pjwstk.jps.ast.terminal.*;
import edu.pjwstk.jps.ast.unary.*;
import edu.pjwstk.jps.result.*;
import edu.pjwstk.jps.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/*
    Implementacja ASTVisitora
     - odpowiada za wykonywanie wyrażeń (Expressions) i wrzucenie wyniku na stos (QRes)
     - implementacje wyrażeń nie mają żadnej logiki, są tylko "pojemnikami" na inne wyrażenia

    Postęp: 26/43 (60%)

    TODO:
     * operacje arytmetyczne,
     * operacje na zbiorach,
     * rzutowanie (... as ...)
     * Equals, Struct
     * poprawka w CommaExpression

    Uwaga:
     Implementacje nie muszą uwzględniać różnych typów danych wejściowych.
     Najpierw obsłużmy najprostsze scenariusze (np. double + double)
 */

public class ConcreteASTVisitor implements ASTVisitor {

    private final Stack<IAbstractQueryResult> qres;
    private final IStoreRepository repository;

    public ConcreteASTVisitor(Stack<IAbstractQueryResult> qres,
                              IStoreRepository repository) {
        this.qres = qres;
        this.repository = repository;
    }

    @Override
    public void visitAsExpression(IAsExpression expr) {

    }

    @Override
    public void visitGroupAsExpression(IGroupAsExpression expr) {

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
    @Override
    public void visitCommaExpression(ICommaExpression expr) {

        List<ISingleResult> list = new ArrayList<>();

        expr.getLeftExpression().accept(this);
        ISingleResult leftResult = (ISingleResult) qres.pop();
        list.add(leftResult);

        expr.getRightExpression().accept(this);
        ISingleResult rightResult = (ISingleResult) qres.pop();
        list.add(rightResult);

        qres.push(new BagResult(list));
    }

    @Override
    public void visitDivideExpression(IDivideExpression expr) {
    }

    // Wykonanie prawego wyrażenia na każdym elemencie kolekcji wejsciowej
    // daje kolekcję wynikową
    @Override
    public void visitDotExpression(IDotExpression expr) {
        IExpression selection = expr.getRightExpression();

        expr.getLeftExpression().accept(this);
        IBagResult collection = (IBagResult)qres.pop();

        IBagResult results = new BagResult(
            Query.select(collection.getElements(), x -> {
                qres.push(x);
                selection.accept(this);
                return (ISingleResult) qres.pop();
            }));

        qres.push(results);
    }

    @Override
    public void visitEqualsExpression(IEqualsExpression expr) {

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

    }

    @Override
    public void visitIntersectExpression(IIntersectExpression expr) {

    }

    @Override
    public void visitJoinExpression(IJoinExpression expr) {

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

    }

    @Override
    public void visitMinusSetExpression(IMinusSetExpression expr) {

    }

    @Override
    public void visitModuloExpression(IModuloExpression expr) {

    }

    @Override
    public void visitMultiplyExpression(IMultiplyExpression expr) {

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

    }

    @Override
    public void visitUnionExpression(IUnionExpression expr) {

    }

    // Zachowanie analogiczne do DotExpression, ale kolekcja wynikowa
    // budowana jest z elementów kolekcji wejściowej
    @Override
    public void visitWhereExpression(IWhereExpression expr) {

        IExpression condition = expr.getRightExpression();

        expr.getLeftExpression().accept(this);
        IBagResult collection = (IBagResult)qres.pop();

        IBagResult results =
            Query.where(collection, x -> {
                qres.push(x);
                condition.accept(this);
                IBooleanResult result = (IBooleanResult) qres.pop();
                return result.getValue();
            });

        qres.push(results);
    }

    @Override
    public void visitXORExpression(IXORExpression expr) {

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

        if (qres.empty())
        {
            result = new BagResult(repository.getCollection(name));
            qres.push(result);
        }
        else
        {
            IAbstractQueryResult input = qres.pop();
            if (input instanceof IBagResult)
            {
                Collection collection = ((IBagResult) input).getElements();
                List values = Query.select(collection, x -> repository.getField((IReferenceResult) x, name));
                result = new BagResult(values);
            }
            else
            {
                result = repository.getField(((IReferenceResult)input), name);
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
        else
        {
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

        IExpression innerExpression = expr.getInnerExpression();
        double sum = getDouble(new SumExpression(innerExpression));
        double count = getDouble(new CountExpression(innerExpression));

        qres.push(new DoubleResult(sum / count));
    }

    // Szybkie wyciąganie wartości z wyrażenia i rzutownie na double
    private double getDouble(IExpression expression) {
        expression.accept(this);
        return (double)((ISimpleResult)qres.pop()).getValue();
    }

    // Szybkie wyciąganie wartości z wyrażenia i rzutownie na boolean
    private boolean getBoolean(IExpression expression) {
        expression.accept(this);
        return (boolean)((ISimpleResult)qres.pop()).getValue();
    }
}
