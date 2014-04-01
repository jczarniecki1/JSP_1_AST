package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.DotExpression;
import edu.pjwstk.demo.expression.binary.WhereExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;
import edu.pjwstk.jps.result.ISingleResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class WhereExpressionTest extends AbstractBinaryExpressionTest {

    @Before
    public void BeforeTest(){
        super.BeforeTest();

        List<Person> persons = new ArrayList<>();

        persons.add(new Person("Marcin","Lewandowski",  20, false,  new Address("Gdańsk")));
        persons.add(new Person("Jan","Kowalski",        21, true,  new Address("Łódź")));
        persons.add(new Person("Piotr","Jankowski",     20, false,  new Address("Gdańsk")));

        store.addJavaCollection(persons, "Person");
    }

    @Test
    public void shouldFilterCollectionCorrectly() throws Exception {
        Expression e = new DotExpression(
                new WhereExpression(
                    new NameExpression("Person"),
                    new NameExpression("Married")
                ),
                new NameExpression("FirstName")
            );

        ISingleResult[] results = getResultsFromBag(e);
        ISingleResult[] expectedResults = getArrayOfResults("Jan");

        assertArrayEquals(expectedResults, results);
    }

}
