package edu.pjwstk.tests.parser;

import edu.pjwstk.demo.result.BooleanResult;
import edu.pjwstk.demo.result.DoubleResult;
import edu.pjwstk.demo.result.IntegerResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionParserTest extends AbstractParserTest {

    @Override
    public void initData() {}

    @Test
    public void shouldParseSimpleCalcQuery(){
        IntegerResult queryResult = (IntegerResult)SolveQuery("1 + 2 * 3");
        assertEquals(7, queryResult.getValue(), 0.00001);
    }

    @Test
    public void shouldParseComplexCalcQuery(){
        DoubleResult queryResult = (DoubleResult)SolveQuery("(1 + 2) * 3 / 4");
        assertEquals(2.25, queryResult.getValue(), 0.00001);
    }

    @Test
    public void shouldParseSimpleBooleanQuery(){
        BooleanResult queryResult = (BooleanResult)SolveQuery("true and false");
        assertEquals(false, queryResult.getValue());
    }

    @Test
    public void shouldParseComplexBooleanQuery(){
        BooleanResult queryResult = (BooleanResult)SolveQuery("false or (true and false) or true and false");
        assertEquals(false, queryResult.getValue());
    }

    @Test
    public void shouldParseQueryWithXor(){
        BooleanResult queryResult = (BooleanResult)SolveQuery("true xor true");
        assertEquals(false, queryResult.getValue());
    }
}
