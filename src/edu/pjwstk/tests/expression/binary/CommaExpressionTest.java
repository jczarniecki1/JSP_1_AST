package edu.pjwstk.tests.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.auxname.AsExpression;
import edu.pjwstk.demo.expression.binary.CommaExpression;
import edu.pjwstk.demo.expression.binary.PlusExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.terminal.StringExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.demo.expression.unary.StructExpression;
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

        assertEquals("struct(1,3)",qres.pop().toString());
    }

    @Test
    public void shoudGiveStructOf3IntegersFromIntegerAndStruct() throws Exception {
        Expression e = new CommaExpression(
                new IntegerExpression(1),
                new StructExpression(
                    new CommaExpression(
                        new IntegerExpression(2),
                        new IntegerExpression(3)
                    )
                )
        );
        e.accept(visitor);
        assertEquals("struct(1,2,3)",qres.pop().toString());
    }

    @Test
    public void shoudGiveStructOf3IntegersFromStructAndInteger() throws Exception {
        Expression e = new CommaExpression(
                new StructExpression(
                        new CommaExpression(
                                new IntegerExpression(1),
                                new IntegerExpression(2)
                        )
                ),
                new IntegerExpression(3)
        );
        e.accept(visitor);
        assertEquals("struct(1,2,3)",qres.pop().toString());
    }

    @Test
    public void shoudGiveBagOfStructsFromBagAndSimple() throws Exception {
        Expression e = new CommaExpression(
                new BagExpression(
                        new CommaExpression(
                                new IntegerExpression(1),
                                new IntegerExpression(2)
                        )
                ),
                new IntegerExpression(3)
        );
        e.accept(visitor);
        assertEquals("bag(struct(1,3),struct(2,3))",qres.pop().toString());
    }
    @Test
    public void shoudGiveBagOfStructsFromSimpleAndBag() throws Exception {
        Expression e = new CommaExpression(
                new IntegerExpression(1),
                new BagExpression(
                        new CommaExpression(
                                new IntegerExpression(2),
                                new IntegerExpression(3)
                        )
                )

        );
        e.accept(visitor);
        assertEquals("bag(struct(1,2),struct(1,3))",qres.pop().toString());
    }

    @Test
    public void shoudGiveBagOfStructsFromBagAndBag() throws Exception {
        Expression e = new CommaExpression(
                new BagExpression(
                        new CommaExpression(
                                new IntegerExpression(1),
                                new IntegerExpression(2)
                        )
                ),
                new BagExpression(
                        new CommaExpression(
                                new IntegerExpression(3),
                                new IntegerExpression(4)
                        )
                )

        );
        e.accept(visitor);
        assertEquals("bag(struct(1,3),struct(1,4),struct(2,3),struct(2,4))",qres.pop().toString());
    }



    @Test
    public void shoudGiveBagOfStructsFromBagAndStruct() throws Exception {
        Expression e = new CommaExpression(
                new BagExpression(
                    new CommaExpression(
                        new IntegerExpression(1),
                        new IntegerExpression(2)
                    )
                ),
                new StructExpression(
                    new CommaExpression(
                        new IntegerExpression(3),
                        new IntegerExpression(4)
                    )
                )

        );

        e.accept(visitor);
        assertEquals("bag(struct(1,3,4),struct(2,3,4))",qres.pop().toString());
    }

    @Test
    public void shoudGiveBagOfStructsFromStructAndBag2() throws Exception {
        Expression e = new CommaExpression(
                new StructExpression(
                        new CommaExpression(
                                new IntegerExpression(1),
                                new IntegerExpression(2)
                        )
                ),
                new BagExpression(
                        new CommaExpression(
                                new AsExpression(
                                    new StringExpression("test"),
                                    "nazwa"
                                ),
                                new AsExpression(
                                   new StringExpression("Ala"),
                                   "nazwa"
                                )
                        )
               )

        );
        // (struct(1, 2), (bag("test", „Ala”) as nazwa));
        e.accept(visitor);
        assertEquals("bag(struct(1,2,<nazwa,\"test\">),struct(1,2,<nazwa,\"Ala\">))",qres.pop().toString());
    }


}
