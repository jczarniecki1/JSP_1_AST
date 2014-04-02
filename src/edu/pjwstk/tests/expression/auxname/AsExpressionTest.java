package edu.pjwstk.tests.expression.auxname;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.auxname.GroupAsExpression;
import edu.pjwstk.demo.expression.binary.CommaExpression;
import edu.pjwstk.demo.expression.terminal.BooleanExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.terminal.StringExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;
import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.IBinderResult;
import edu.pjwstk.jps.result.IIntegerResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AsExpressionTest extends AbstractAuxiliaryNameExpressionTest{
    @Before
    public void BeforeTest(){
        super.BeforeTest();

        List<Person> persons = new ArrayList<>();

        persons.add(new Person("Marcin","Lewandowski",  20, true,  new Address("Gdańsk")));
        persons.add(new Person("Jan","Kowalski",        21, true,  new Address("Łódź")));

        store.addJavaCollection(persons, "Person");
    }

    @Test
    public void shouldBindNameToEachIntegerInBag() throws Exception {

        Expression e = new GroupAsExpression(
                new BagExpression(
                    new CommaExpression(
                        new IntegerExpression(12),
                        new IntegerExpression(14)
                    )
                ),
                "testName1"
            );

        IBinderResult[] results = getBinders(e);

        assertEquals(2, results.length);

        assertTrue(results[0].getValue() instanceof IIntegerResult);
        assertEquals(new Integer(12), ((IIntegerResult) results[0].getValue()).getValue());
        assertEquals("testName1", results[0].getName());

        assertTrue(results[1].getValue() instanceof IIntegerResult);
        assertEquals(new Integer(14), ((IIntegerResult) results[1].getValue()).getValue());
        assertEquals("testName1", results[1].getName());
    }

    @Test
    public void shouldBindNameToEachBagInBag() throws Exception {

        Expression e = new GroupAsExpression(
                new BagExpression(
                    new CommaExpression(
                        new BagExpression(
                            new CommaExpression(
                                new IntegerExpression(12),
                                new IntegerExpression(14)
                            )
                        ),
                        new BagExpression(
                            new CommaExpression(
                                new StringExpression("Tom"),
                                new BooleanExpression(false)
                            )
                        )
                    )
                ),
                "testName1"
            );
        IBinderResult[] results = getBinders(e);

        assertEquals(2, results.length);

        assertTrue(results[0].getValue() instanceof IBagResult);
        assertEquals("testName1", results[0].getName());

        assertTrue(results[1].getValue() instanceof IBagResult);
        assertEquals("testName1", results[1].getName());
    }
}
