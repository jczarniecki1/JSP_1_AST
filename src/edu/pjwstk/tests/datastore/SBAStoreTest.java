package edu.pjwstk.tests.datastore;

import edu.pjwstk.demo.DatastorePrinter;
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
import edu.pjwstk.demo.model.CD;
import edu.pjwstk.demo.model.Transakcja;
import edu.pjwstk.demo.model.Wytwornia;
import edu.pjwstk.demo.result.DoubleResult;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.visitor.ASTVisitor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class SBAStoreTest {

    private static QResStack qres = new QResStack();
    private static SBAStore store;
    private static ASTVisitor visitor;
    private static List<CD> cds;
    private static List<Wytwornia> wytwornie;

    private void createCollection() {
        Wytwornia wytwornia;

        cds = new ArrayList<>();
        wytwornie = new ArrayList<>();

        CD cd;
        wytwornia = new Wytwornia("Universal Music Group", "USA");
        wytwornie.add(wytwornia);
        cd = new CD(wytwornia,"Back To Black","Emy Winehouse",2007, 37.99,1);
        cd.addTransakcja(new Transakcja("05-01-2014",2,36.50));
        cd.addTransakcja(new Transakcja("07-01-2014",5,37.50));
        cd.addTransakcja(new Transakcja("08-02-2014", 4, 37.88));
        cds.add(cd);
        cd = new CD(wytwornia,"Brothers In Arms","Dire Straits",1996, 35.99,2);
        cd.addTransakcja(new Transakcja("05-02-2014",2,35.55));
        cd.addTransakcja(new Transakcja("07-03-2014",5,35.99));
        cds.add(cd);

        cd = new CD(wytwornia,"Symphonica","George Michael",2014, 36.99,3);
        cd.addTransakcja(new Transakcja("15-03-2014",2,36.99));
        cd.addTransakcja(new Transakcja("17-04-2014",5,35.99));
        cds.add(cd);

        wytwornia = new Wytwornia("Sony Music", "USA");
        wytwornie.add(wytwornia);
        cd = new CD(wytwornia,"The Best Of Sade","Sade",2000, 23.99,4);
        cd.addTransakcja(new Transakcja("15-02-2014", 5, 23.50));
        cd.addTransakcja(new Transakcja("17-03-2014",5,23.50));
        cd.addTransakcja(new Transakcja("18-04-2014", 3, 24.88));
        cds.add(cd);

        cd = new CD(wytwornia,"Ten","Pearl Jam",1992, 23.99,5);
        cd.addTransakcja(new Transakcja("11-02-2014",2,23.50));
        cd.addTransakcja(new Transakcja("12-03-2014",4,23.50));
        cd.addTransakcja(new Transakcja("18-04-2014",2,24.88));
        cds.add(cd);

        wytwornia = new Wytwornia("Warner Music Poland", "Polska");
        wytwornie.add(wytwornia);
        cd = new CD(wytwornia,"Confessions On A Dance Floor","Madonna",2005, 62.49,6);
        cd.addTransakcja(new Transakcja("11-02-2014",5,62.50));
        cd.addTransakcja(new Transakcja("13-03-2014",1,63.99));
        cd.addTransakcja(new Transakcja("18-04-2014",2,62.88));
        cds.add(cd);

    }

    public void SetNewContext() {
        IStoreRepository repository = StoreRepository.getInstance();
        visitor = new ConcreteASTVisitor(qres, repository);
    }

    @Before
    public void Context(){
        store = (SBAStore)SBAStore.getInstance();
        store.reset();
    }

    @Test
    public void databaseAfterLoadingInvalidXMLShouldBeEmpty() throws Exception {

        System.out.println("Expected exception in the Test:");
        store.loadXML("res/example_invalid.xml");
        SetNewContext();
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
        SetNewContext();

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

    @Test
    public void shouldBeAbleToLoadCollectionOfJavaObjectsWithoutFailure() throws Exception {

        try {
            createCollection();
            store.addJavaCollection(cds, "cds");
            SetNewContext();
            DatastorePrinter.PrintDatabase(store);

        } catch (Exception e){
            fail("expected: no exceptions");
        }
    }

    @Test
    public void javaCollectionLoadedToDatabaseShouldBeQueryable() throws Exception {

        createCollection();
        store.addJavaCollection(cds, "CD");
        SetNewContext();

        IExpression ex = new AvgExpression(
            new DotExpression(
                new DotExpression(
                    new WhereExpression(
                        new NameExpression("CD"),
                        new EqualsExpression(
                            new NameExpression("id"),
                            new IntegerExpression(2)
                        )
                    ),
                    new NameExpression("sprzedaz")
                ),
                new NameExpression("cenaSprzedazy")
            )
        );

        ex.accept(visitor);
        DoubleResult result = (DoubleResult) qres.pop();

        assertEquals(35.77, result.getValue(), 0.000002);
    }
}
