package edu.pjwstk.tests.expression.unary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.CommaExpression;
import edu.pjwstk.demo.expression.binary.PlusExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.demo.expression.unary.StructExpression;
import edu.pjwstk.tests.expression.AbstractExpressionTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StructExpressionTest extends AbstractExpressionTest{

    @Test
    public void shoudBeAbleToPushStructResult() throws Exception {
        Expression e =
            new StructExpression(
                new CommaExpression(
                    new IntegerExpression(1),
                    new PlusExpression(
                        new IntegerExpression(2),
                        new IntegerExpression(1)
                    )
            )
        );
        e.accept(visitor);
        assertEquals("struct(1,3)", qres.pop().toString());
    }

    @Test
    public void shoudBeReturnOneStructResult() throws Exception {
        // struct(struct(1,2,3))

        Expression e =
           new StructExpression(
               new StructExpression(
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
        assertEquals("struct(1,2,3)",qres.pop().toString());
    }

    /* @Test
    public void shoudBeReturnOneStructWithTwoBagResults() throws Exception {

        // wydaje mi się, że tak nie może być
        // structura może mieć tylko elementy typu ISingleResult
        Expression e =
            new StructExpression(
                new CommaExpression(
                    new BagExpression(new IntegerExpression(1)),
                    new BagExpression(
                        new CommaExpression(
                            new IntegerExpression(2),
                            new IntegerExpression(1)
                        )
                    )
                )
            );

        e.accept(visitor);
        assertEquals("struct(bag(0=1),bag(0=2,1=1))",qres.pop().toString());
    } */
}
