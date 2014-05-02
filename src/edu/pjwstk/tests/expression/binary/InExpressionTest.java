package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.CommaExpression;
import edu.pjwstk.demo.expression.binary.InExpression;
import edu.pjwstk.demo.expression.terminal.DoubleExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.jps.result.IBooleanResult;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Małgorzata on 31.03.14.
 */
public class InExpressionTest extends AbstractBinaryExpressionTest {

    @Test
    public void shouldGiveTrueFor2Integers() throws Exception {
        Expression e = new InExpression(
                new IntegerExpression(1),
                new IntegerExpression(1)
        );
        e.accept(visitor);
        IBooleanResult result = (IBooleanResult)qres.pop();

        assertEquals(true, result.getValue());

    }

    @Test
    public void shouldGiveTrueFor2Doubles() throws Exception {
        Expression e = new InExpression(
                new DoubleExpression(1.0),
                new DoubleExpression(1.0)
        );
        e.accept(visitor);
        IBooleanResult result = (IBooleanResult)qres.pop();

        assertEquals(true, result.getValue());
    }

    @Test
    public void shouldGiveFalseFor2Integers() throws Exception {
        Expression e = new InExpression(
                new IntegerExpression(2),
                new IntegerExpression(1)
        );
        e.accept(visitor);
        IBooleanResult result = (IBooleanResult)qres.pop();

        assertEquals(false,result.getValue());

    }

    @Test
    public void shouldGiveFalseFor2Doubles() throws Exception {
        Expression e = new InExpression(
                new DoubleExpression(2.0),
                new DoubleExpression(1.0)
        );
        e.accept(visitor);
        IBooleanResult result = (IBooleanResult)qres.pop();

        assertEquals(false,result.getValue());

    }

    @Test
    public void shouldGiveTrueForIntegers() throws Exception {
        Expression e = new InExpression(
                new IntegerExpression(1),
                new BagExpression(
                    new CommaExpression(
                    new IntegerExpression(2),
                    new IntegerExpression(1)
                    )
                )
        );
        e.accept(visitor);
        IBooleanResult result = (IBooleanResult)qres.pop();

        assertEquals(true,result.getValue());

    }

    @Test
    public void shouldGiveFalseForIntegers() throws Exception {
        Expression e = new InExpression(
                new IntegerExpression(3),
                new BagExpression(
                        new CommaExpression(
                                new IntegerExpression(2),
                                new IntegerExpression(1)
                        )
                )
        );
        e.accept(visitor);
        IBooleanResult result = (IBooleanResult)qres.pop();

        assertEquals(false,result.getValue());

    }
}
