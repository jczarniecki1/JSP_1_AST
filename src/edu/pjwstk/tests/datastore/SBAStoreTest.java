package edu.pjwstk.tests.datastore;

import edu.pjwstk.demo.datastore.ComplexObject;
import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.expression.binary.DotExpression;
import edu.pjwstk.demo.expression.binary.EqualsExpression;
import edu.pjwstk.demo.expression.binary.WhereExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import edu.pjwstk.demo.expression.unary.AvgExpression;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.result.DoubleResult;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.visitor.ASTVisitor;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SBAStoreTest {

    private static QResStack qres = new QResStack();
    private static ISBAStore store = new SBAStore();
    private static ASTVisitor visitor;

    @Before
    public void Context() {
        IStoreRepository repository = new StoreRepository(store);
        visitor = new ConcreteASTVisitor(qres, repository);
    }

    @Test
    public void databaseAfterLoadingInvalidXMLShouldBeEmpty() throws Exception {

        System.out.println("Expected exception in the Test:");
        store.loadXML("res/example_invalid.xml");
        List<IOID> rootChildren = ((ComplexObject) store.retrieve(store.getEntryOID())).getChildOIDs();
        assertEquals(0, rootChildren.size());
    }

    @Test
    public void shouldBeAbleToLoadSimpleXMLWithoutFailure() throws Exception {

        try {
            store.loadXML("res/example.xml");
        } catch (Exception e){
            fail("expected: no exceptions");
        }
    }

    @Test
    public void databaseAfterLoadXMLShouldBeQueryable() throws Exception {

        store.loadXML("res/baza.xml");

        IExpression ex = new AvgExpression(
            new DotExpression(
                new DotExpression(
                    new WhereExpression(
                        new NameExpression("CD"),
                        new EqualsExpression(
                            new NameExpression("ID"),
                            new IntegerExpression(2)
                        )
                    ),
                    new NameExpression("TRANSAKCJA")
                ),
                new NameExpression("CENA_SPRZEDAZY")
            )
        );

        ex.accept(visitor);
        DoubleResult result = (DoubleResult) qres.pop();

        assertEquals(9.386666, result.getValue(), 0.000001);
    }
}
