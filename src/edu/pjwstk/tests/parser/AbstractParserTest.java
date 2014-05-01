package edu.pjwstk.tests.parser;

import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.parser.ExpressionParser;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.interpreter.qres.IQResStack;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.visitor.ASTVisitor;
import org.junit.Before;

import static org.junit.Assert.fail;

public abstract class AbstractParserTest {
    protected IQResStack qres;
    protected ISBAStore store;
    protected IStoreRepository repository;
    protected ASTVisitor visitor;
    protected ExpressionParser parser;

    public abstract void initData();

    @Before
    public void initContext(){

        qres = new QResStack();
        store = new SBAStore();
        repository = new StoreRepository(store);

        initData();

        visitor = new ConcreteASTVisitor(qres, repository);
    }

    protected IAbstractQueryResult SolveQuery(String query) {

        try {
            parser = new ExpressionParser(query);
            parser.user_init();
            parser.parse();

            Expression expression = parser.RESULT;

            expression.accept(visitor);

            return qres.pop();
        }
        catch (Exception e) {
            fail("Failed to run query");
            return null;
        }
    }
}
