package edu.pjwstk.tests.expression;

import edu.pjwstk.demo.common.lambda.Selector;
import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.result.BooleanResult;
import edu.pjwstk.demo.result.DoubleResult;
import edu.pjwstk.demo.result.IntegerResult;
import edu.pjwstk.demo.result.StringResult;
import edu.pjwstk.jps.interpreter.qres.IQResStack;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.result.ISingleResult;
import edu.pjwstk.jps.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.Stack;

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
}
