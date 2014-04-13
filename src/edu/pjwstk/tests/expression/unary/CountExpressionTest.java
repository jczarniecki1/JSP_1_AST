package edu.pjwstk.tests.expression.unary;

import edu.pjwstk.mt_jc.expression.Expression;
import edu.pjwstk.mt_jc.expression.binary.CommaExpression;
import edu.pjwstk.mt_jc.expression.terminal.IntegerExpression;
import edu.pjwstk.mt_jc.expression.unary.BagExpression;
import edu.pjwstk.mt_jc.expression.unary.CountExpression;
import edu.pjwstk.jps.result.IIntegerResult;
import edu.pjwstk.tests.expression.AbstractExpressionTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountExpressionTest extends AbstractExpressionTest {

    @Test
    public void shouldGiveCorrectValue() throws Exception {
        Expression e =
            new CountExpression(
                new BagExpression(
                    new CommaExpression(
                        new IntegerExpression(1),
                        new IntegerExpression(9)
                    )
                )
            );
        e.accept(visitor);
        IIntegerResult result = (IIntegerResult)qres.pop();

        assertEquals(2, result.getValue(), 0.0000001);
    }
}
