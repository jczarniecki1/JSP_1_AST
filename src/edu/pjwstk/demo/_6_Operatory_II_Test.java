package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.MinusExpression;
import edu.pjwstk.demo.expression.binary.PlusExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.solver.ExecutionResult;
import edu.pjwstk.demo.solver.ExpressionSolver;
import edu.pjwstk.jps.datastore.ISBAStore;

public class _6_Operatory_II_Test {

    public static void main(String[] args) throws Exception {

        ISBAStore store = SBAStore.getClearInstance();

        store.loadXML("res/dane_do_zap_testowych.xml");

        // TODO: Zapytania dla miniprojektu 6
        Log("---------------------");
        Log("1 + 2");
        Expression expression =
            new PlusExpression(
                new IntegerExpression(1),
                new IntegerExpression(2)
            );
        Log(ExpressionSolver.execute(expression));

        Log("---------------------");
        Log("1 - 2");
        expression =
            new MinusExpression(
                new IntegerExpression(1),
                new IntegerExpression(2)
            );
        Log(ExpressionSolver.execute(expression));

        // Weryfikacja poprawności
        ExecutionResult result = ExpressionSolver.executeFromFile("res/Zapytania_testowe_JPS.txt");

        Log(result.message);
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
