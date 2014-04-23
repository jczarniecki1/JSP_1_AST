package edu.pjwstk.tests.expression.unary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.CommaExpression;
import edu.pjwstk.demo.expression.terminal.DoubleExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.unary.AvgExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.jps.result.IDoubleResult;
import edu.pjwstk.tests.expression.AbstractExpressionTest;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
public class AvgExpressionTest extends AbstractExpressionTest {

    @Test
    public void shouldGiveCorrectValue() throws Exception {
        Expression e =
            new AvgExpression(
                new BagExpression(
                    new CommaExpression(
                        new IntegerExpression(1),
                        new IntegerExpression(9)
                    )
                )
            );
        e.accept(visitor);
        IDoubleResult result = (IDoubleResult )qres.pop();

        assertEquals(5.0, result.getValue(), 0.0000001);
    }

    @Test
    public void shouldGiveCorrectValue_2() throws Exception {
        Expression e =
            new AvgExpression(
                new BagExpression(
                    new CommaExpression(
                        new DoubleExpression(-11.5),
                        new IntegerExpression(9)
                    )
                )
            );
        e.accept(visitor);
        IDoubleResult result = (IDoubleResult )qres.pop();

        assertEquals(-1.25, result.getValue(), 0.0000001);
    }
}
