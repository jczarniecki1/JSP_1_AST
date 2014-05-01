package edu.pjwstk.tests.parser;

import edu.pjwstk.jps.result.IAbstractQueryResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionParserWithExternalSourceTest extends AbstractParserTest {

    @Override
    public void initData(){
        store.loadXML("res/dane_do_zap_testowych.xml");
    }

    @Test
    public void shouldSolveQueryThatAccessDataFromExternalSource(){
        IAbstractQueryResult result = SolveQuery("integerNumber");
        assertEquals("ref(10)", result.toString());
    }

    @Test
    public void shouldSolveQueryThatAccessStringValueFromExternalSource2(){
        IAbstractQueryResult result = SolveQuery("stringValue");
        assertEquals("ref(\"Ala\")", result.toString());
    }

    @Test
    public void shouldSolveQueryThatAccessBooleanValueFromExternalSource2(){
        IAbstractQueryResult result = SolveQuery("booleanValue");
        assertEquals("ref(true)", result.toString());
    }
}
