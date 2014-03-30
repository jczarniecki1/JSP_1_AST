package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.PlusExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.jps.result.IDoubleResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlusExpressionTest extends AbstractBinaryExpressionTest {

    @Test
    public void shouldGiveCorrectValue() throws Exception {
        Expression e = new PlusExpression(
                new IntegerExpression(1),
                new IntegerExpression(1)
            );
        e.accept(visitor);
        IDoubleResult result = (IDoubleResult)qres.pop();

        assertEquals(result.getValue(),2.0, 0.0000001);
    }
}
