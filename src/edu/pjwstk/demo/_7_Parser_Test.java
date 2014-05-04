package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.solver.ExecutionResult;
import edu.pjwstk.demo.solver.ExpressionSolver;
import edu.pjwstk.jps.datastore.ISBAStore;

public class _7_Parser_Test {

    public static void main(String[] args) throws Exception {

        ISBAStore store = SBAStore.getClearInstance();

        store.loadXML("res/dane_do_zap_testowych.xml");

        ExecutionResult result = ExpressionSolver.executeFromFile("res/Zapytania_testowe_JPS.txt");

        Log(result.message);
    }

    // Testowanie dowolnego zapytania
    /*
    public static void main(String[] args) throws Exception {

        ISBAStore store = SBAStore.getClearInstance();
        store.loadXML("res/dane_do_zap_testowych.xml");

        Log(ExpressionSolver.execute("1 + 1"));
    }
    */
    public static void Log(Object o){
        System.out.println(o);
    }
}
