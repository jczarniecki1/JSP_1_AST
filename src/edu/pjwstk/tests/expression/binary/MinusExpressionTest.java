package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.MinusExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.jps.result.IDoubleResult;
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
        IDoubleResult result = (IDoubleResult)qres.pop();

        assertEquals(result.getValue(),3.0, 0.0000001);
    }
}