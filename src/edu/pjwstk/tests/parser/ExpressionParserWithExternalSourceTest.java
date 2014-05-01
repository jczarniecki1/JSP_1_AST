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
        assertEquals(result.toString(), "ref(10)");
    }
}
