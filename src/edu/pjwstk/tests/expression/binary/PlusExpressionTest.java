package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.mt_jc.expression.Expression;
import edu.pjwstk.mt_jc.expression.binary.PlusExpression;
import edu.pjwstk.mt_jc.expression.terminal.BooleanExpression;
import edu.pjwstk.mt_jc.expression.terminal.DoubleExpression;
import edu.pjwstk.mt_jc.expression.terminal.IntegerExpression;
import edu.pjwstk.mt_jc.expression.terminal.StringExpression;
import edu.pjwstk.mt_jc.result.StringResult;
import edu.pjwstk.jps.result.IDoubleResult;
import edu.pjwstk.jps.result.IIntegerResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlusExpressionTest extends AbstractBinaryExpressionTest {

    @Test
    public void shouldGiveStringFor2Strings() throws Exception {
        Expression e = new PlusExpression(
                new StringExpression("Dzień"),
                new StringExpression(" dobry")
        );
        e.accept(visitor);
        StringResult result = (StringResult)qres.pop();

        assertEquals(result.getValue(),"Dzień dobry");
    }

    @Test
    public void shouldGiveStringForStringAndInteger() throws Exception {
        Expression e = new PlusExpression(
                new StringExpression("Dzień "),
                new IntegerExpression(3)
        );
        e.accept(visitor);
        StringResult result = (StringResult)qres.pop();

        assertEquals(result.getValue(),"Dzień 3");
    }

    @Test
    public void shouldGiveStringForStringAndDouble() throws Exception {
        Expression e = new PlusExpression(
                new StringExpression("zł "),
                new DoubleExpression(2.55)
        );
        e.accept(visitor);
        StringResult result = (StringResult)qres.pop();

        assertEquals(result.getValue(),"zł 2.55");
    }

    @Test
    public void shouldGiveStringForBooleanAndString() throws Exception {
        Expression e = new PlusExpression(
                new BooleanExpression(true),
                new StringExpression("Ala")
        );
        e.accept(visitor);
        StringResult result = (StringResult)qres.pop();

        assertEquals(result.getValue(),"trueAla");
    }

    @Test
    public void shouldGiveCorrectValue() throws Exception {
        Expression e = new PlusExpression(
                new IntegerExpression(1),
                new IntegerExpression(1)
            );
        e.accept(visitor);
        IIntegerResult result = (IIntegerResult)qres.pop();

        assertEquals(2, result.getValue(), 0.0000001);
    }

    @Test
    public void shouldGiveCorrectValueForNotInteger() throws Exception {
        Expression e = new PlusExpression(
                new IntegerExpression(1),
                new DoubleExpression(1.0)
        );
        e.accept(visitor);
        IDoubleResult result = (IDoubleResult)qres.pop();

        assertEquals(2.0, result.getValue(), 0.0000001);
    }
}
