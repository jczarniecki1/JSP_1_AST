package edu.pjwstk.demo.solver;

import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.parser.ExpressionParser;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.visitor.ASTVisitor;

import java.io.*;

import static org.junit.Assert.fail;

public class ExpressionSolver {

    private static IStoreRepository repository;

    public static IAbstractQueryResult execute(String query){
        return execute(query, SolverParams.None);
    }

    public static IAbstractQueryResult execute(String query, SolverParams params){
        try {
            if (repository == null) {
                repository = StoreRepository.getInstance();
            }

            // Inicjalizacja qres i envs przed każdym zapytaniem
            QResStack qres = new QResStack();
            ASTVisitor visitor = new ConcreteASTVisitor(qres, repository);

            // Wykonanie zapytania
            Expression expression = parse(query);
            expression.accept(visitor);

            return qres.pop();
        }
        catch (Exception e) {
            if (params != SolverParams.Silent && params != SolverParams.ThrowExceptionOnly){
                e.printStackTrace();
            }
            if (params == SolverParams.ThrowException || params == SolverParams.ThrowExceptionOnly){
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    private static Expression parse(String query) throws Exception {
        ExpressionParser parser = new ExpressionParser(query);
        parser.user_init();
        parser.parse();

        return parser.RESULT;
    }

    public static ExecutionResult executeFromFile(String filePath) {
        int allCount = 0, successCount = 0, parsedCount = 0;



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

                    result = execute(query, SolverParams.ThrowExceptionOnly);

                    parsedCount++;
                    parsed = true;

                    if (expected.equals(result.toString())){
                        successCount++;
                        solved = true;
                    }
                }
                catch (Throwable ignored){}

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

        String summary = ("\n\nStatus: \n" +
            "\tparsed "+parsedCount+" of "+allCount +"\n"+
            "\tsolved "+successCount+" of "+allCount +"\n\n"+ testlog);

        return new ExecutionResult(successCount == allCount, summary);
    }

    /*
     * ***************** Dodatkowe metody *************************
     */

    private static String getSpace(int i) {
        String sp = "";
        while (i-- > 0) sp += " ";
        return sp;
    }

    private static String testlog = "";

    private static void Log(String t){
        testlog+= t;
    }
}
