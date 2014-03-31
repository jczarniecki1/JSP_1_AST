package edu.pjwstk.tests.expression.auxname;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.auxname.AsExpression;
import edu.pjwstk.demo.expression.terminal.StringExpression;
import edu.pjwstk.tests.expression.AbstractExpressionTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Małgorzata on 01.04.14.
 */
public class AsExpressionTest extends AbstractExpressionTest {
    @Test
    public void shouldGiveTrueForSimpleBinding() throws Exception {
        Expression e = new AsExpression(
                new StringExpression("Lord"),
                "kot"
        );
        e.accept(visitor);
        boolean result = qres.pop().toString().equals("binder(name=\"kot\",value=\"Lord\")");

        assertEquals(true,result);
    }
}
