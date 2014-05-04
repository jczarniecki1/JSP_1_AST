package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.EqualsExpression;
import edu.pjwstk.demo.expression.binary.NotEqualsExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.solver.ExpressionSolver;
import edu.pjwstk.jps.datastore.ISBAStore;

public class _5_Operatory_I_Test {

    public static void main(String[] args) throws Exception {

        ISBAStore store = SBAStore.getClearInstance();

        store.loadXML("res/dane_do_zap_testowych.xml");

        // TODO: Zapytania dla miniprojektu 5
        Log("---------------------");
        Log("1 == 2");
        Expression expression =
            new EqualsExpression(
                new IntegerExpression(1),
                new IntegerExpression(2)
            );
        Log(ExpressionSolver.execute(expression));

        Log("---------------------");
        Log("1 != 2");
        expression =
            new NotEqualsExpression(
                new IntegerExpression(1),
                new IntegerExpression(2)
            );
        Log(ExpressionSolver.execute(expression));
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
