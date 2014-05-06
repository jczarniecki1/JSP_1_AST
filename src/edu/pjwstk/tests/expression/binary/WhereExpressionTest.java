package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.DotExpression;
import edu.pjwstk.demo.expression.binary.WhereExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WhereExpressionTest extends AbstractBinaryExpressionTest {

    @Before
    public void BeforeTest(){
        super.InitStore();

        List<Person> people = new ArrayList<>();

        people.add(new Person("Marcin", "Lewandowski", 20, false, new Address("Gdańsk")));
        people.add(new Person("Jan", "Kowalski", 21, true, new Address("Łódź")));
        people.add(new Person("Piotr", "Jankowski", 20, false, new Address("Gdańsk")));

        store.addJavaCollection(people, "Person");
        super.InitVisitor();
    }

    @Test
    public void shouldFilterCollectionCorrectly() throws Exception {
        Expression e = new DotExpression(
                new WhereExpression(
                    new NameExpression("Person"),
                    new NameExpression("married")
                ),
                new NameExpression("firstName")
            );
        e.accept(visitor);

        assertEquals("bag(ref(\"Jan\"))", qres.pop().toString());
    }

}
