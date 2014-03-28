package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.ISingleResult;
import org.junit.Before;

import java.util.Collection;
import java.util.Stack;

public class AbstractBinaryExpressionTest extends edu.pjwstk.tests.expression.AbstractExpressionTest {

    @Before
    public void BeforeTest() {

        store = new SBAStore();
        qres = new Stack<>();

        repository = new StoreRepository(store);
        visitor = new ConcreteASTVisitor(qres, repository);
    }

    protected ISingleResult[] getResults(Expression e) {

        e.accept(visitor);

        IBagResult bagResult = (IBagResult) qres.pop();
        Collection<ISingleResult> elements = bagResult.getElements();

        return elements.toArray(new ISingleResult[elements.size()]);
    }

}
