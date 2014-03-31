package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.PlusExpression;
import edu.pjwstk.demo.expression.terminal.DoubleExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.jps.result.IDoubleResult;
import edu.pjwstk.jps.result.IIntegerResult;
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
        IIntegerResult result = (IIntegerResult)qres.pop();

        assertEquals(result.getValue(),2, 0.0000001);
    }

    @Test
    public void shouldGiveCorrectValueForNotInteger() throws Exception {
        Expression e = new PlusExpression(
                new IntegerExpression(1),
                new DoubleExpression(1.0)
        );
        e.accept(visitor);
        IDoubleResult result = (IDoubleResult)qres.pop();

        assertEquals(result.getValue(),2.0, 0.0000001);
    }
}
