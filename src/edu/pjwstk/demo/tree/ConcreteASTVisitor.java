package edu.pjwstk.demo.tree;

import edu.pjwstk.demo.datastore.*;
import edu.pjwstk.demo.expression.NameExpression;
import edu.pjwstk.demo.res.*;
import edu.pjwstk.demo.res.ReferenceResult;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.auxname.IAsExpression;
import edu.pjwstk.jps.ast.auxname.IGroupAsExpression;
import edu.pjwstk.jps.ast.binary.*;
import edu.pjwstk.jps.ast.terminal.*;
import edu.pjwstk.jps.ast.unary.*;
import edu.pjwstk.jps.datastore.*;
import edu.pjwstk.jps.result.*;
import edu.pjwstk.jps.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

interface Predicate<T> {
    boolean apply(T element);
}

interface Selector<T, TResult> {
    TResult select(T element);
}

public class ConcreteASTVisitor implements ASTVisitor {

    private final ISBAStore2 store;
    private final Stack<IAbstractQueryResult> qres;

    public ConcreteASTVisitor(ISBAStore2 store, Stack<IAbstractQueryResult> qres) {
        this.store = store;
        this.qres = qres;
    }

    @Override
    public void visitAsExpression(IAsExpression expr) {

    }

    @Override
    public void visitGroupAsExpression(IGroupAsExpression expr) {

    }

    @Override
    public void visitAllExpression(IForAllExpression expr) {

    }

    @Override
    public void visitAndExpression(IAndExpression expr) {

    }

    @Override
    public void visitAnyExpression(IForAnyExpression expr) {

    }

    @Override
    public void visitCloseByExpression(ICloseByExpression expr) {

    }

    @Override
    public void visitCommaExpression(ICommaExpression expr) {

    }

    @Override
    public void visitDivideExpression(IDivideExpression expr) {

    }

    private IBagResult map(IBagResult bag, Selector<ISingleResult, ISingleResult> selector){
        List<ISingleResult> results = new ArrayList<>();
        for (ISingleResult element : bag.getElements()) {
            results.add(
                selector.select(element)
            );
        }
        return new BagResult(results);
    }
    @Override
    public void visitDotExpression(IDotExpression expr) {
        expr.getLeftExpression().accept(this);
        IBagResult bag = (IBagResult)qres.pop();

        IExpression condition = expr.getRightExpression();
        if (condition instanceof NameExpression){
            condition.accept(this);
            StringResult name = (StringResult)qres.pop();
            IBagResult result = map(bag,
                // TODO: move to repository and replace with:
                //  x -> repository.getFieldId(objectId, fieldName)
                x -> {
                    // TODO: move to repository and replace with:
                    //  object = repository.get(id);
                    IOID id = ((IReferenceResult)x).getOIDValue();
                    IComplexObject object = (IComplexObject)store.retrieve(id);

                    // TODO: move to valueProvider and replace with:
                    //  field = valueProvider.valueByName(object, name);
                    ISBAObject field = firstChild(object,
                        y -> y.getName() == name.getValue()
                    );

                    ISBAObject o = store.retrieve(field.getOID());

                         if (o instanceof StringObject)  return new StringResult (((StringObject) o).getValue());
                    else if (o instanceof IntegerObject) return new IntegerResult(((IntegerObject)o).getValue());
                    else if (o instanceof DoubleObject)  return new DoubleResult (((DoubleObject) o).getValue());
                    else if (o instanceof BooleanObject) return new BooleanResult(((BooleanObject)o).getValue());
                    else return new ReferenceResult(o.getOID());
                }
            );
            qres.push(result);
        }
        // TODO: else if (expression instanceof BooleanExpression) { ... }

    }

    @Override
    public void visitEqualsExpression(IEqualsExpression expr) {

    }

    @Override
    public void visitGreaterThanExpression(IGreaterThanExpression expr) {

    }

    @Override
    public void visitGreaterOrEqualThanExpression(IGreaterOrEqualThanExpression expr) {

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

    }

    @Override
    public void visitLessThanExpression(ILessThanExpression expr) {

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

    }

    @Override
    public void visitOrderByExpression(IOrderByExpression expr) {

    }

    @Override
    public void visitOrExpression(IOrExpression expr) {

    }

    @Override
    public void visitPlusExpression(IPlusExpression expr) {

    }

    @Override
    public void visitUnionExpression(IUnionExpression expr) {

    }

    private IBagResult where(IBagResult bag, Predicate<ISingleResult> predicate){
        List<ISingleResult> results = new ArrayList<>();
        for (ISingleResult element : bag.getElements()) {
            if (predicate.apply(element)) {
                results.add(element);
            }
        }
        return new BagResult(results);
    }
    private ISBAObject firstChild(IComplexObject object, Predicate<ISBAObject> predicate){
        for (IOID id : object.getChildOIDs()) {
            ISBAObject child = store.retrieve(id);
            if (predicate.apply(child)) {
                return child;
            }
        }
        return null;
    }
    @Override
    public void visitWhereExpression(IWhereExpression expr) {
        expr.getLeftExpression().accept(this);
        IStringResult bagName = (IStringResult)qres.pop();

        // TODO: get bag by bagName
        //  using binder?
        IBagResult bag = store.getFakeBag(bagName.getValue());
                //new BagResult(new ArrayList<>());

        IExpression condition = expr.getRightExpression();
        if (condition instanceof NameExpression){
            condition.accept(this);
            StringResult name = (StringResult)qres.pop();
            IBagResult result = where(bag,
                // TODO: move to repository and replace with:
                //  x -> repository.get<BooleanResult>(objectId, fieldName)
                x -> {
                    // TODO: move to repository and replace with:
                    //  object = repository.get(id);
                    IOID id = ((IReferenceResult)x).getOIDValue();
                    IComplexObject object = (IComplexObject)store.retrieve(id);

                    // TODO: move to valueProvider and replace with:
                    //  field = valueProvider.valueByName(object, name);
                    ISBAObject field = firstChild(object,
                        y -> y.getName() == name.getValue()
                    );

                    return ((BooleanObject)field).getValue();
                }
            );
            qres.push(result);
        }
        // TODO: else if (expression instanceof BooleanExpression) { ... }

    }

    @Override
    public void visitXORExpression(IXORExpression expr) {

    }

    @Override
    public void visitBooleanTerminal(IBooleanTerminal expr) {

    }

    @Override
    public void visitDoubleTerminal(IDoubleTerminal expr) {

    }

    @Override
    public void visitIntegerTerminal(IIntegerTerminal expr) {
        qres.push(new IntegerResult(expr.getValue()));
    }

    @Override
    public void visitNameTerminal(INameTerminal expr) {
        qres.push(new StringResult(expr.getName()));
    }

    @Override
    public void visitStringTerminal(IStringTerminal expr) {
        qres.push(new StringResult(expr.getValue()));
    }

    @Override
    public void visitBagExpression(IBagExpression expr) {

    }

    @Override
    public void visitCountExpression(ICountExpression expr) {

    }

    @Override
    public void visitExistsExpression(IExistsExpression expr) {

    }

    @Override
    public void visitMaxExpression(IMaxExpression expr) {

    }

    @Override
    public void visitMinExpression(IMinExpression expr) {

    }

    @Override
    public void visitNotExpression(INotExpression expr) {

    }

    @Override
    public void visitStructExpression(IStructExpression expr) {

    }

    @Override
    public void visitSumExpression(ISumExpression expr) {

    }

    @Override
    public void visitUniqueExpression(IUniqueExpression expr) {

    }

    @Override
    public void visitAvgExpression(IAvgExpression expr) {

    }
}
