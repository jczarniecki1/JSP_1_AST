package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.MinusExpression;
import edu.pjwstk.demo.expression.terminal.DoubleExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.jps.result.IDoubleResult;
import edu.pjwstk.jps.result.IIntegerResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinusExpressionTest extends AbstractBinaryExpressionTest {

    @Test
    public void shouldGiveCorrectValue() throws Exception {
        Expression e = new MinusExpression(
                new IntegerExpression(4),
                new IntegerExpression(1)
            );
        e.accept(visitor);
        IIntegerResult result = (IIntegerResult)qres.pop();

        assertEquals(3, (long)result.getValue());
    }

    @Test
    public void shouldGiveCorrectValueForMixedTypes() throws Exception {
        Expression e = new MinusExpression(
                new DoubleExpression(4.1),
                new IntegerExpression(1)
            );
        e.accept(visitor);
        IDoubleResult result = (IDoubleResult)qres.pop();

        assertEquals(3.1, result.getValue(), 0.0000001);
    }

    @Test
    public void shouldGiveCorrectValueForNestedExpressions() throws Exception {
        Expression e =
            new MinusExpression(
                new IntegerExpression(-11),
                new MinusExpression(
                    new IntegerExpression(-11),
                    new MinusExpression(
                        new IntegerExpression(-11),
                        new DoubleExpression(-1.1)
                    )
                )
            );
        e.accept(visitor);
        IDoubleResult result = (IDoubleResult)qres.pop();

        assertEquals(-9.9, result.getValue(), 0.0000001);
    }
}