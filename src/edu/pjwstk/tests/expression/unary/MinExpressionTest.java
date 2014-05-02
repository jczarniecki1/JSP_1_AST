package edu.pjwstk.tests.expression.unary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.CommaExpression;
import edu.pjwstk.demo.expression.terminal.DoubleExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.demo.expression.unary.MinExpression;
import edu.pjwstk.jps.result.IDoubleResult;
import edu.pjwstk.jps.result.IIntegerResult;
import edu.pjwstk.tests.expression.AbstractExpressionTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinExpressionTest extends AbstractExpressionTest {

    @Test
    public void shouldGiveCorrectValue() throws Exception {
        Expression e =
            new MinExpression(
                new BagExpression(
                    new CommaExpression(
                        new IntegerExpression(1),
                        new IntegerExpression(9)
                    )
                )
            );
        e.accept(visitor);
        IIntegerResult result = (IIntegerResult)qres.pop();

        assertEquals(1, result.getValue(), 0.0000001);
    }

    @Test
    public void shouldGiveCorrectValueForMixedTypes() throws Exception {
        Expression e =
            new MinExpression(
                new BagExpression(
                    new CommaExpression(
                        new DoubleExpression(9.0),
                        new IntegerExpression(9)
                    )
                )
            );
        e.accept(visitor);
        IIntegerResult result = (IIntegerResult)qres.pop();

        assertEquals(9, result.getValue(), 0.0000001);
    }

    @Test
    public void shouldGiveCorrectValueForDouble() throws Exception {
        Expression e =
            new MinExpression(
                new BagExpression(
                    new CommaExpression(
                        new DoubleExpression(9.9),
                        new DoubleExpression(19.1)
                    )
                )
            );
        e.accept(visitor);
        IDoubleResult result = (IDoubleResult)qres.pop();

        assertEquals(9.9, result.getValue(), 0.0000001);
    }
}
