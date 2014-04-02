package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.CommaExpression;
import edu.pjwstk.demo.expression.binary.PlusExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Małgorzata on 31.03.14.
 */
public class CommaExpressionTest extends AbstractBinaryExpressionTest {
    @Test
    public void shoudGiveStructOf2Integer() throws Exception {
        Expression e = new CommaExpression(
                new IntegerExpression(1),
                new PlusExpression(
                    new IntegerExpression(2),
                    new IntegerExpression(1)
                )
        );
        e.accept(visitor);
        boolean result = qres.pop().toString().equals("struct(1,3)");

        assertEquals(result,true);
    }
}
