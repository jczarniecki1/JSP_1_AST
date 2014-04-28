package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.*;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import edu.pjwstk.demo.expression.terminal.StringExpression;
import edu.pjwstk.demo.expression.unary.ExistsExpression;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class _4_ENVS_Test {
    private static QResStack qres = new QResStack();
    private static ISBAStore store = new SBAStore();
    private static ASTVisitor visitor;

    public static void main(String[] args){

        LoadData();
        IStoreRepository repository = new StoreRepository(store);
        visitor = new ConcreteASTVisitor(qres, repository);

        // 1. ((emp where married) union (emp where lName=”Nowak)).fName
        SolveQuery1();

        // 2. ((emp where exists address) where address.number>20).(street, city)
        SolveQuery2();

    }

    private static void LoadData() {
        store.loadXML("res/envs_data.xml");
    }

    private static void SolveQuery1() {
        Expression expression =
            new DotExpression(
                new UnionExpression(
                    new WhereExpression(
                        new NameExpression("emp"),
                        new NameExpression("married")
                    ),
                    new WhereExpression(
                        new NameExpression("emp"),
                        new EqualsExpression(
                            new NameExpression("lName"),
                            new StringExpression("Nowak")
                        )
                    )
                ),
                new NameExpression("fName")
            );

        expression.accept(visitor);

        Log("Result from Query1:");
        Log(qres.pop());
    }

    private static void SolveQuery2() {
        Expression expression =
            new DotExpression(
                new DotExpression(
                    new WhereExpression(
                        new WhereExpression(
                            new NameExpression("emp"),
                            new ExistsExpression(
                                new NameExpression("address")
                            )
                        ),
                        new GreaterThanExpression(
                            new DotExpression(
                                new NameExpression("address"),
                                new NameExpression("number")
                            ),
                            new IntegerExpression(20)
                        )
                    ),
                    new NameExpression("address")
                ),
                new CommaExpression(
                    new NameExpression("street"),
                    new NameExpression("city")
                )
            );

        expression.accept(visitor);

        Log("Result from Query2:");
        Log(qres.pop());
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
