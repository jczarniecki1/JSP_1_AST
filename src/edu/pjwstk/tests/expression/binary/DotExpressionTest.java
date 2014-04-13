package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.mt_jc.expression.Expression;
import edu.pjwstk.mt_jc.expression.binary.DotExpression;
import edu.pjwstk.mt_jc.expression.terminal.NameExpression;
import edu.pjwstk.mt_jc.model.Address;
import edu.pjwstk.mt_jc.model.Person;
import edu.pjwstk.jps.result.ISingleResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class DotExpressionTest extends AbstractBinaryExpressionTest {

    @Before
    public void BeforeTest(){
        super.BeforeTest();

        List<Person> persons = new ArrayList<>();

        persons.add(new Person("Marcin","Lewandowski",  20, true,  new Address("Gdańsk")));
        persons.add(new Person("Jan","Kowalski",        21, true,  new Address("Łódź")));
        persons.add(new Person("Piotr","Jankowski",     20, true,  new Address("Gdańsk")));

        store.addJavaCollection(persons, "Person");
    }

    @Test
    public void shouldGiveCorrectField() throws Exception {
        Expression e = new DotExpression(
                new NameExpression("Person"),
                new NameExpression("FirstName")
            );

        ISingleResult[] results = getResultsFromBag(e);

        ISingleResult[] expectedResults = getArrayOfResults("Marcin","Jan","Piotr");

        assertArrayEquals(expectedResults, results);
    }
}
