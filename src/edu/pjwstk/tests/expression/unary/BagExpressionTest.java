package edu.pjwstk.tests.expression.unary;

import edu.pjwstk.mt_jc.expression.Expression;
import edu.pjwstk.mt_jc.expression.binary.CommaExpression;
import edu.pjwstk.mt_jc.expression.binary.PlusExpression;
import edu.pjwstk.mt_jc.expression.terminal.IntegerExpression;
import edu.pjwstk.mt_jc.expression.unary.BagExpression;
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
    public void shouldBeReturnBagOfStructs() throws Exception {
        // (bag(1,2),3)
        Expression e =
                new BagExpression(
                new CommaExpression(
                    new BagExpression(
                        new CommaExpression(
                            new IntegerExpression(1),
                            new IntegerExpression(2)
                        )
                    ),
                    new IntegerExpression(3)
                ));


        e.accept(visitor);

        assertEquals("bag(0=struct(1,3),1=struct(2,3))",qres.pop().toString());

    }

    @Test
    public void shouldBeReturnOneBagIfBagIsInBag() throws Exception {
        // bag(bag(1,2,3))
        Expression e =
                new BagExpression(
                    new BagExpression (
                        new CommaExpression(
                                new IntegerExpression(1),
                                new CommaExpression(
                                    new IntegerExpression(2),
                                    new IntegerExpression(3)
                                )
                        )
                    )
                );

        e.accept(visitor);

        assertEquals("bag(0=1,1=2,2=3)",qres.pop().toString());

    }

    @Test
    public void shoudBeReturnOneBagFromTwoBagResults() throws Exception {
        // bag(1,bag(2,2+1))
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
        assertEquals("bag(0=struct(1,2),1=struct(1,3))",qres.pop().toString());

    }
}
