package edu.pjwstk.tests.expression.auxname;


import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.auxname.AsExpression;
import edu.pjwstk.demo.expression.binary.*;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import edu.pjwstk.demo.expression.terminal.StringExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;
import edu.pjwstk.jps.result.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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
    public void shouldBindNameToInteger() throws Exception {
        Expression e = new AsExpression(
                            new IntegerExpression(12),
                            "liczba"
                        );
        e.accept(visitor);
        IAbstractQueryResult result = qres.pop();
        assertEquals("binder(name=\"liczba\",value=12)", result.toString());
    }

    @Test
    public void shouldBindNameToString() throws Exception {
        Expression e = new AsExpression(
                new StringExpression("dwanaście"),
                "liczba"
        );
        e.accept(visitor);
        IAbstractQueryResult result = qres.pop();
        assertEquals("binder(name=\"liczba\",value=\"dwanaście\")", result.toString());
    }

    @Test
    public void shouldBindNameToIntegerTerminal() throws Exception {

        Expression e = new AsExpression(
                new IntegerExpression(12),
                "testName1"
            );
        IBinderResult[] results = getBinders(e);

        IIntegerResult resultValue = (IIntegerResult)(results[0].getValue());
        String resultName = results[0].getName();

        assertEquals(resultValue.getValue(),new Integer(12));
        assertEquals(resultName, "testName1");
    }

    @Test
    public void shouldBindNameToCollection() throws Exception {

        Expression e = new AsExpression(
                new NameExpression("Person"),
                "testName1"
            );
        IBinderResult[] results = getBinders(e);

        IBagResult resultValue = (IBagResult)(results[0].getValue());
        String resultName = results[0].getName();

        assertEquals(resultValue.getElements().size(), 2);
        assertEquals(resultName, "testName1");
    }

    @Test
    public void shouldBeAbleToUseBinding_1() throws Exception {

        Expression e =
            new DotExpression(
                new JoinExpression(
                    new NameExpression("Person"),
                    new BagExpression(
                        new CommaExpression(
                            new AsExpression(
                                new StringExpression("Zuzanna"),
                                "FirstName"
                            ),
                            new AsExpression(
                                new StringExpression("Nowakowska"),
                                "LastName"
                            )
                        )
                    )
                ),
                new NameExpression("FirstName")
            );

        ISingleResult[] results = getResultsFromBag(e);
        ISingleResult[] expectedResults = getArrayOfResults("Jan", "Marcin", "Zuzanna");

        assertArrayEquals(expectedResults, results);
    }

    @Test
    public void shouldBeAbleToUseBinding_2() throws Exception {

        Expression e =
            new DotExpression(
                new WhereExpression(
                    new JoinExpression(
                        new NameExpression("Person"),
                        new BagExpression(
                            new CommaExpression(
                                new AsExpression(
                                    new IntegerExpression(29),
                                    "Age"
                                ),
                                new AsExpression(
                                    new StringExpression("Nowakowska"),
                                    "LastName"
                                )
                            )
                        )
                    ),
                    new GreaterThanExpression(
                        new NameExpression("Age"),
                        new IntegerExpression(20)
                    )
                ),
                new NameExpression("FirstName")
            );

        ISingleResult[] results = getResultsFromBag(e);
        ISingleResult[] expectedResults = getArrayOfResults("Zuzanna", "Jan");

        assertArrayEquals(expectedResults, results);
    }
}