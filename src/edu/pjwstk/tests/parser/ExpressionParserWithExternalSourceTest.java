package edu.pjwstk.tests.parser;

import edu.pjwstk.demo.visitor.ExpressionSolver;
import edu.pjwstk.demo.visitor.SolverParams;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import org.junit.Test;

import java.io.*;

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
        IAbstractQueryResult result = ExpressionSolver.execute("bag(\"Ala\",2,3) minus bag(2,3.40)");
        if (result == null){
            fail("Failed to run query");
        }
        assertEquals("", result.toString());
    }

    //
    // Metoda do testowania wszystkich przypadków
    //
    @Test
    public void shouldSolveAllQueriesFromGivenFile(){

        int allCount = 0, successCount = 0, parsedCount = 0;
        String filePath = "res/Zapytania_testowe_JPS.txt";


        FileInputStream inputStream = null;
        BufferedReader reader = null;

        try {
            inputStream = new FileInputStream(filePath);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine();

            String query = "";
            String expected = "";
            IAbstractQueryResult result = null;

            while(line != null){
                String[] lineArgs = line.split("[\t]");
                boolean parsed = false, solved = false;
                try {
                    allCount++;
                    query = lineArgs[0];
                    expected = lineArgs[1];

                    result = ExpressionSolver.execute(query, SolverParams.ThrowExceptionOnly);
                    parsedCount++;
                    parsed = true;
                    if (expected.equals(result.toString())){
                        successCount++;
                        solved = true;
                    }
                }
                catch (Throwable e){
                    //e.printStackTrace();
                }

                line = reader.readLine();
                boolean failed = parsed && !solved;

                Log("[ "+String.format("%3d",allCount)+" ]: "           // Numer
                        + query                                         // Treść zapytania
                        + getSpace(55 - query.length())                 //
                        + (parsed ? " | executed" : "")                 // Czy się wykonało?
                        + (solved ? " | ok" : "")                       // Czy wynik jest poprawny?
                        + (failed ? " | failed\n" +                     //
                                "\texpected: "+ expected+"\n" +         // Wynik oczekiwany
                                "\tactual:   "+ result : "")            // Wynik aktualny (błędny)
                        + "\n");
            }

        } catch (FileNotFoundException ex) {
            fail("File not found");
        } catch (IOException ex) {
            fail("Error while reading the file");
        } finally {
            try {
                if (reader != null) reader.close();
                if (inputStream != null) inputStream.close();
            }
            catch (IOException ignored) {}
        }

        if (successCount < allCount){
            fail("\n\nStatus: \n" +                                     // Podsumowanie
                    "\tparsed "+parsedCount+" of "+allCount +"\n"+
                    "\tsolved "+successCount+" of "+allCount +"\n\n"+ testlog);
        }
    }

    /*
     * ***************** Dodatkowe metody *************************
     */

    private String getSpace(int i) {
        String sp = "";
        while (i-- > 0) sp += " ";
        return sp;
    }

    private static String testlog = "";

    private static void Log(String t){
        testlog+= t;
    }
}
