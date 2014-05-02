package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.DotExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DotExpressionTest extends AbstractBinaryExpressionTest {

    @Before
    public void BeforeTest(){
        super.InitStore();

        List<Person> persons = new ArrayList<>();

        persons.add(new Person("Marcin","Lewandowski",  20, true,  new Address("Gdańsk")));
        persons.add(new Person("Jan","Kowalski",        21, true,  new Address("Łódź")));
        persons.add(new Person("Piotr","Jankowski",     20, true,  new Address("Gdańsk")));

        store.addJavaCollection(persons, "Person");
        super.InitVisitor();
    }

    @Test
    public void shouldGiveCorrectField() throws Exception {
        Expression e = new DotExpression(
                new NameExpression("Person"),
                new NameExpression("firstName")
            );
        e.accept(visitor);

        assertEquals("bag(ref(\"Marcin\"),ref(\"Jan\"),ref(\"Piotr\"))", qres.pop().toString());
    }
}
