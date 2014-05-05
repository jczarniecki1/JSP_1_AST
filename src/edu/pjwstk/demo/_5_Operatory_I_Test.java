package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.auxname.AsExpression;
import edu.pjwstk.demo.expression.auxname.GroupAsExpression;
import edu.pjwstk.demo.expression.binary.*;
import edu.pjwstk.demo.expression.terminal.BooleanExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import edu.pjwstk.demo.expression.unary.*;
import edu.pjwstk.demo.solver.ExpressionSolver;
import edu.pjwstk.jps.datastore.ISBAStore;

public class _5_Operatory_I_Test {

    public static void main(String[] args) throws Exception {

        ISBAStore store = SBAStore.getClearInstance();

        store.loadXML("res/dane_do_zap_testowych.xml");

        Log("---------------------");
        Log("avg(emp.address.number) * count(emp) == sum(emp.address.number)");
        Expression expression =
            new EqualsExpression(
                new MultiplyExpression(
                    new AvgExpression(
                        new DotExpression(
                            new DotExpression(
                                new NameExpression("emp"),
                                new NameExpression("address")
                            ),
                            new NameExpression("number")
                        )
                    ),
                    new CountExpression(
                        new NameExpression("emp")
                    )
                ),
                new SumExpression(
                    new DotExpression(
                        new DotExpression(
                            new NameExpression("emp"),
                            new NameExpression("address")
                        ),
                        new NameExpression("number")
                    )
                )
            );
        Log(ExpressionSolver.execute(expression));

        Log("---------------------");
        Log("any (emp as e) (all e.address number >= 10) != false");
        expression =
            new ForAnyExpression(
                new AsExpression(
                    new NameExpression("emp"),
                    "e"
                ),
                new NotEqualsExpression(
                    new ForAllExpression(
                        new DotExpression(
                            new NameExpression("e"),
                            new NameExpression("address")
                        ),
                        new GreaterOrEqualThanExpression(
                            new NameExpression("number"),
                            new IntegerExpression(10)
                        )
                    ),
                    new BooleanExpression(false)
                )
            );
        Log(ExpressionSolver.execute(expression));

        Log("---------------------");
        Log("max (bag(integerNumber, integerNumber2)) <= min (bag(realNumber, realNumber2))");
        expression =
            new LessOrEqualThanExpression(
                new MaxExpression(
                    new BagExpression(
                        new CommaExpression(
                            new NameExpression("integerNumber"),
                            new NameExpression("integerNumber2")
                        )
                    )
                ),
                new MinExpression(
                    new BagExpression(
                        new CommaExpression(
                            new NameExpression("realNumber"),
                            new NameExpression("realNumber2")
                        )
                    )
                )
            );
        Log(ExpressionSolver.execute(expression));

        Log("---------------------");
        Log("((exists(pomidor) intersect integerNumber < 11) in (bag(true, false) minus (realNumber > 240)) group as wynik");
        expression =
            new GroupAsExpression(
                new InExpression(
                    new IntersectExpression(
                        new ExistsExpression(
                            new NameExpression("pomidor")
                        ),
                        new LessThanExpression(
                            new NameExpression("integerNumber"),
                            new IntegerExpression(11)
                        )
                    ),
                    new MinusSetExpression(
                        new BagExpression(
                            new CommaExpression(
                                new BooleanExpression(true),
                                new BooleanExpression(false)
                            )
                        ),
                        new GreaterThanExpression(
                            new NameExpression("realNumber"),
                            new IntegerExpression(240)
                        )
                    )
                ),
                "wynik"
            );
        Log(ExpressionSolver.execute(expression));
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
