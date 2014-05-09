package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.auxname.AsExpression;
import edu.pjwstk.demo.expression.binary.JoinExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Małgorzata on 09.05.14.
 */
public class JoinExpressionTest extends AbstractBinaryExpressionTest {

    @Test
    public void shouldGiveStructOfSimple() throws Exception {

        //1 join 2
        Expression e = new JoinExpression(
                new IntegerExpression(1),
                new IntegerExpression(2)
        );
        e.accept(visitor);

        assertEquals("struct(1,2)", qres.pop().toString());
    }

    @Test
    public void shouldGiveStructOfBinderAndSimple() throws Exception {

        //(1 as n) join n
        Expression e = new JoinExpression(
                new AsExpression(
                        new IntegerExpression(1),"n"),
                new NameExpression("n")
        );
        e.accept(visitor);

        assertEquals("struct(<n,1>,1)", qres.pop().toString());
    }

    @Test
    public void shouldGiveBinder() throws Exception {

        //(emp) join (married)
        Expression e = new JoinExpression(
                new NameExpression("emp"),
                new NameExpression("merried")
        );
        e.accept(visitor);

        assertEquals("bag(struct(ref(emp1), true), struct(ref(emp2), true))", qres.pop().toString());
    }
}
