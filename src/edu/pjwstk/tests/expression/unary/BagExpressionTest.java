package edu.pjwstk.tests.expression.unary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.CommaExpression;
import edu.pjwstk.demo.expression.binary.PlusExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.tests.expression.AbstractExpressionTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BagExpressionTest extends AbstractExpressionTest {

    @Test
    public void shoudBeAbleToPushBagResult() throws Exception {
        Expression e =
            new BagExpression(
                new CommaExpression(
                    new IntegerExpression(1),
                    new PlusExpression(
                        new IntegerExpression(2),
                        new IntegerExpression(1)
                    )
            )
        );
        e.accept(visitor);
        boolean result = qres.pop().toString().equals("bag(0=1,1=3)");

        assertEquals(true,result);
    }

    @Test
    public void shoudBeReturnOneBagFromTwoBagResults() throws Exception {
        Expression e =
               new BagExpression(
               new CommaExpression(
                    new IntegerExpression(1),
                    new BagExpression(
                        new CommaExpression(
                                new IntegerExpression(2),
                                new PlusExpression(
                                        new IntegerExpression(2),
                                        new IntegerExpression(1)
                                )
                        )
                    )
               ) );

        e.accept(visitor);
        boolean result = qres.pop().toString().equals("bag(0=1,1=2,2=3)");

        assertEquals(true,result);
    }
}
