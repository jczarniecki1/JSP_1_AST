package edu.pjwstk.tests.parser;

import edu.pjwstk.demo.solver.ExecutionResult;
import edu.pjwstk.demo.solver.ExecutionStatus;
import edu.pjwstk.demo.solver.ExpressionSolver;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//
// Testowanie zbioru przypadków zapisanych w pliku tekstowym
//
public class ExpressionParserWithExternalSourceTest extends AbstractParserTest {

    @Override
    public void initData(){
        store.loadXML("res/dane_do_zap_testowych.xml");
    }

    //
    // Metoda do izolowanego testowania wybranego przypadku
    //
    @Test
    public void shouldSolveQueryThatIsCurrentlyBeingFixed(){

        IAbstractQueryResult result = ExpressionSolver.execute("any (bag(1,bag(2,3) group as wew) as num) (num == 2)");

        if (result == null){
            fail("Failed to run query");
        }
        assertEquals("false", result.toString());
    }

    //
    // Metoda do testowania wszystkich przypadków
    //
    @Test
    public void shouldSolveAllQueriesFromGivenFile(){

        ExecutionResult result = ExpressionSolver.executeFromFile("res/Zapytania_testowe_JPS.txt");

        if (result.status == ExecutionStatus.SUCCESS){
            Log(result.message);
        }
        else {
            fail(result.message);
        }
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
