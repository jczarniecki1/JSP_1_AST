package edu.pjwstk.tests.parser;

import edu.pjwstk.jps.result.IAbstractQueryResult;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

    @Test
    public void shouldSolveAllQueriesFromGivenFile(){

        int allCount = 0, passedCount = 0;
        String filePath = "res/Zapytania_testowe_JPS.txt";


        FileInputStream inputStream = null;
        BufferedReader reader = null;

        try {
            inputStream = new FileInputStream(filePath);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine();

            String query = "";
            String expected = "";

            while(line != null){
                String[] lineArgs = line.split("[\t]");
                boolean parsed = false, solved = false;
                try {
                    allCount++;
                    query = lineArgs[0];
                    expected = lineArgs[1];

                    IAbstractQueryResult result = SolveQuery(query);
                    parsed = true;
                    if (expected.equals(result.toString())){
                        passedCount++;
                        solved = true;
                    };
                }
                catch (Throwable e){
                    //e.printStackTrace();
                }

                line = reader.readLine();

                Log("[ "+allCount+" ]: "+query+getSpace(40-query.length())+" | "+(parsed ? "parsed" : "") + " | " +(solved ? "solved" : "") + "\n");
            }

        } catch (FileNotFoundException ex) {
            fail("File not found");
        } catch (IOException ex) {
            fail("Error while reading the file");
        } finally {
            try {
                reader.close();
                inputStream.close();
            } catch (IOException ex) {}
        }

        if (passedCount < allCount){
            fail("Status: passed "+passedCount+" of "+allCount +"\n\n"+ testlog);
        }
    }

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
