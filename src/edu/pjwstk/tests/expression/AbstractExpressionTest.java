package edu.pjwstk.tests.expression;

import edu.pjwstk.mt_jc.common.lambda.Selector;
import edu.pjwstk.mt_jc.datastore.IStoreRepository;
import edu.pjwstk.mt_jc.datastore.SBAStore;
import edu.pjwstk.mt_jc.datastore.StoreRepository;
import edu.pjwstk.mt_jc.expression.Expression;
import edu.pjwstk.mt_jc.interpreter.qres.QResStack;
import edu.pjwstk.mt_jc.result.BooleanResult;
import edu.pjwstk.mt_jc.result.DoubleResult;
import edu.pjwstk.mt_jc.result.IntegerResult;
import edu.pjwstk.mt_jc.result.StringResult;
import edu.pjwstk.mt_jc.visitor.ConcreteASTVisitor;
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
    public void BeforeTest() {

        store = new SBAStore();
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
