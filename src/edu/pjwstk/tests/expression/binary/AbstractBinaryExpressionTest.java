package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.ISingleResult;
import edu.pjwstk.jps.visitor.ASTVisitor;
import org.junit.Before;

import java.util.Collection;
import java.util.Stack;

/**
 * Created by Jacek on 2014-03-28.
 */
public class AbstractBinaryExpressionTest {
    SBAStore store;
    Stack<IAbstractQueryResult> qres;IStoreRepository repository;ASTVisitor visitor;

    @Before
    public void BeforeTest() {
        store = new SBAStore();
        qres = new Stack<>();
        repository = new StoreRepository(store);
        visitor = new ConcreteASTVisitor(qres, repository);
    }

    protected ISingleResult[] getResults(Expression e) {
        e.accept(visitor);
        Collection<ISingleResult> elements = ((IBagResult) qres.pop()).getElements();
        return elements.toArray(new ISingleResult[elements.size()]);
    }
}
