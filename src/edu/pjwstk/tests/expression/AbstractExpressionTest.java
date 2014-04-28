package edu.pjwstk.tests.expression;

import edu.pjwstk.demo.common.lambda.Selector;
import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.result.BooleanResult;
import edu.pjwstk.demo.result.DoubleResult;
import edu.pjwstk.demo.result.IntegerResult;
import edu.pjwstk.demo.result.StringResult;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.jps.interpreter.qres.IQResStack;
import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.ISingleResult;
import edu.pjwstk.jps.visitor.ASTVisitor;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Collection;

public class AbstractExpressionTest {
    protected SBAStore store;
    protected IQResStack qres;
    protected IStoreRepository repository;
    protected ASTVisitor visitor;

    @SafeVarargs
    protected final <T> ISingleResult[] getArrayOfResults(T... objects) {

        if (objects.length == 0) return null;

        if (objects[0] instanceof String){
            return getArrayOfResults(x -> new StringResult((String)x), objects);
        }
        if (objects[0] instanceof Integer){
            return getArrayOfResults(x -> new IntegerResult((Integer)x), objects);
        }
        if (objects[0] instanceof Double){
            return getArrayOfResults(x -> new DoubleResult((Double)x), objects);
        }
        if (objects[0] instanceof Boolean){
            return getArrayOfResults(x -> new BooleanResult((Boolean)x), objects);
        }
        else return null;
    }

    @SafeVarargs
    protected final <T> ISingleResult[] getArrayOfResults(Selector<T, ISingleResult> selector, T... objects) {
        ArrayList<ISingleResult> expectedResultList = new ArrayList<>();
        for (T o : objects){
            expectedResultList.add(selector.select(o));
        }
        return expectedResultList.toArray(new ISingleResult[expectedResultList.size()]);
    }

    @Before
    public void InitStore() {
        store = new SBAStore();
    }

    @Before
    public void InitVisitor() {
        qres = new QResStack();

        repository = new StoreRepository(store);
        visitor = new ConcreteASTVisitor(qres, repository);
    }

    protected ISingleResult[] getResultsFromBag(Expression e) {

        e.accept(visitor);

        IBagResult bagResult = (IBagResult) qres.pop();
        Collection<ISingleResult> elements = bagResult.getElements();

        return elements.toArray(new ISingleResult[elements.size()]);
    }
}
