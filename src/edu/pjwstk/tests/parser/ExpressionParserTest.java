package edu.pjwstk.tests.parser;

import edu.pjwstk.demo.result.BooleanResult;
import edu.pjwstk.demo.result.DoubleResult;
import edu.pjwstk.demo.result.IntegerResult;
import edu.pjwstk.demo.visitor.ExpressionSolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionParserTest extends AbstractParserTest {

    @Override
    public void initData() {}

    @Test
    public void shouldParseSimpleCalcQuery(){
        IntegerResult queryResult = (IntegerResult) ExpressionSolver.execute("1 + 2 * 3");
        assertEquals(7, queryResult.getValue(), 0.00001);
    }

    @Test
    public void shouldParseComplexCalcQuery(){
        DoubleResult queryResult = (DoubleResult) ExpressionSolver.execute("(1 + 2) * 3 / 4");
        assertEquals(2.25, queryResult.getValue(), 0.00001);
    }

    @Test
    public void shouldParseSimpleBooleanQuery(){
        BooleanResult queryResult = (BooleanResult) ExpressionSolver.execute("true and false");
        assertEquals(false, queryResult.getValue());
    }

    @Test
    public void shouldParseComplexBooleanQuery(){
        BooleanResult queryResult = (BooleanResult) ExpressionSolver.execute("false or (true and false) or true and false");
        assertEquals(false, queryResult.getValue());
    }

    @Test
    public void shouldParseQueryWithXor(){
        BooleanResult queryResult = (BooleanResult) ExpressionSolver.execute("true xor true");
        assertEquals(false, queryResult.getValue());
    }
}
