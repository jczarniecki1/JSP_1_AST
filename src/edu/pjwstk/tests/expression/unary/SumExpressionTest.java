package edu.pjwstk.tests.expression.unary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.CommaExpression;
import edu.pjwstk.demo.expression.binary.DotExpression;
import edu.pjwstk.demo.expression.terminal.DoubleExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.demo.expression.unary.SumExpression;
import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;
import edu.pjwstk.jps.result.IDoubleResult;
import edu.pjwstk.jps.result.IIntegerResult;
import edu.pjwstk.tests.expression.AbstractExpressionTest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SumExpressionTest extends AbstractExpressionTest {

    @Before
    public void BeforeTest(){
        super.InitStore();

        List<Person> persons = new ArrayList<>();

        persons.add(new Person("Marcin","Lewandowski",  20, true,  new Address("Gdańsk")));
        persons.add(new Person("Jan","Kowalski",        21, true,  new Address("Łódź")));
        persons.add(new Person("Tomasz","Kowalski",     33, true,  new Address("Łódź")));

        store.addJavaCollection(persons, "Person");
        super.InitVisitor();
    }

    @Test
    public void shouldGiveCorrectValue() throws Exception {
        Expression e =
            new SumExpression(
                new BagExpression(
                    new CommaExpression(
                        new IntegerExpression(1),
                        new IntegerExpression(9)
                    )
                )
            );
        e.accept(visitor);
        IIntegerResult result = (IIntegerResult)qres.pop();

        assertEquals(10, result.getValue(), 0.0000001);
    }

    @Test
    public void shouldGiveCorrectValue_2() throws Exception {
        Expression e =
            new SumExpression(
                new DotExpression(
                    new NameExpression("Person"),
                    new NameExpression("age")
                )
            );
        e.accept(visitor);
        IIntegerResult result = (IIntegerResult)qres.pop();

        assertEquals(74, result.getValue(), 0.0000001);
    }

    @Test
    public void shouldGiveCorrectValue_3() throws Exception {
        Expression e =
            new SumExpression(
                new BagExpression(
                    new CommaExpression(
                        new IntegerExpression(1),
                        new DoubleExpression(9.0)
                    )
                )
            );
        e.accept(visitor);
        IDoubleResult result = (IDoubleResult)qres.pop();

        assertEquals(10, result.getValue(), 0.0000001);
    }
}
