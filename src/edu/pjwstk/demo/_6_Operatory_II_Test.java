package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.*;
import edu.pjwstk.demo.expression.terminal.BooleanExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.demo.expression.unary.CountExpression;
import edu.pjwstk.demo.expression.unary.NotExpression;
import edu.pjwstk.demo.expression.unary.UniqueExpression;
import edu.pjwstk.demo.solver.ExecutionResult;
import edu.pjwstk.demo.solver.ExpressionSolver;
import edu.pjwstk.jps.datastore.ISBAStore;

public class _6_Operatory_II_Test {

    public static void main(String[] args) throws Exception {

        ISBAStore store = SBAStore.getClearInstance();

        store.loadXML("res/dane_do_zap_testowych.xml");

        Log("---------------------");
        Log("(emp where count(book) > 1 or not(((address.number - 10)/2+10)%2 != 1)) union unique(sampleComplexObj.int)");
        Expression expression =
            new UnionExpression(
                new WhereExpression(
                    new NameExpression("emp"),
                    new OrExpression(
                        new GreaterThanExpression(
                            new CountExpression(
                                new NameExpression("book")
                            ),
                            new IntegerExpression(1)
                        ),
                        new NotExpression(
                            new NotEqualsExpression(
                                new ModuloExpression(
                                    new PlusExpression(
                                        new DivideExpression(
                                            new MinusExpression(
                                                new DotExpression(
                                                    new NameExpression("address"),
                                                    new NameExpression("number")
                                                ),
                                                new IntegerExpression(10)
                                            ),
                                            new IntegerExpression(2)
                                        ),
                                        new IntegerExpression(10)
                                    ),
                                    new IntegerExpression(2)
                                ),
                                new IntegerExpression(1)
                            )
                        )
                    )
                ),
                new UniqueExpression(
                    new DotExpression(
                        new NameExpression("sampleComplexObj"),
                        new NameExpression("int")
                    )
                )
            );
        Log(ExpressionSolver.execute(expression));

        Log("---------------------");
        Log("bag(booleanValue == false) join (booleanValue2 xor (true and booleanValue))");
        expression =
            new JoinExpression(
                new BagExpression(
                    new EqualsExpression(
                        new NameExpression("booleanValue"),
                        new BooleanExpression(false)
                    )
                ),
                new XORExpression(
                    new NameExpression("booleanValue2"),
                    new AndExpression(
                        new BooleanExpression(true),
                        new NameExpression("booleanValue")
                    )
                )
            );
        Log(ExpressionSolver.execute(expression));

        Log("---------------------");
        Log("Weryfikacja poprawności");
        ExecutionResult result = ExpressionSolver.executeFromFile("res/Zapytania_testowe_JPS.txt");
        Log(result.message);
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
