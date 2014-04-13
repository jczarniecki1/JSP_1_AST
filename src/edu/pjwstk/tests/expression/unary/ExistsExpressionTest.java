package edu.pjwstk.tests.expression.unary;

import edu.pjwstk.mt_jc.expression.Expression;
import edu.pjwstk.mt_jc.expression.binary.*;
import edu.pjwstk.mt_jc.expression.terminal.IntegerExpression;
import edu.pjwstk.mt_jc.expression.terminal.NameExpression;
import edu.pjwstk.mt_jc.expression.unary.BagExpression;
import edu.pjwstk.mt_jc.expression.unary.ExistsExpression;
import edu.pjwstk.mt_jc.model.Address;
import edu.pjwstk.mt_jc.model.Person;
import edu.pjwstk.jps.result.IBooleanResult;
import edu.pjwstk.tests.expression.AbstractExpressionTest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExistsExpressionTest extends AbstractExpressionTest {

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
    public void shouldGiveCorrectValue() throws Exception {
        Expression e =
            new ExistsExpression(
                new BagExpression(
                    new CommaExpression(
                        new IntegerExpression(1),
                        new IntegerExpression(9)
                    )
                )
            );
        e.accept(visitor);
        IBooleanResult result = (IBooleanResult)qres.pop();

        assertEquals(true, result.getValue());
    }

    @Test
    public void shouldGiveCorrectValue_2() throws Exception {
        Expression e =
            new ExistsExpression(
                new DotExpression(
                    new WhereExpression(
                        new NameExpression("Person"),
                        new LessThanExpression(
                            new NameExpression("Age"),
                            new IntegerExpression(20)
                        )
                    ),
                    new NameExpression("FirstName")
                )
            );
        e.accept(visitor);
        IBooleanResult result = (IBooleanResult)qres.pop();

        assertEquals(true, result.getValue());
    }

    @Test
    public void shouldGiveCorrectValue_3() throws Exception {
        Expression e =
            new ExistsExpression(
                new DotExpression(
                    new WhereExpression(
                        new NameExpression("Person"),
                        new GreaterThanExpression(
                            new NameExpression("Age"),
                            new IntegerExpression(100)
                        )
                    ),
                    new NameExpression("FirstName")
                )
            );
        e.accept(visitor);
        IBooleanResult result = (IBooleanResult)qres.pop();

        assertEquals(false, result.getValue());
    }
}
