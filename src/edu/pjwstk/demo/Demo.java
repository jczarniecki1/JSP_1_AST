package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.ISBAStoreJavaObjects;
import edu.pjwstk.demo.datastore.SBAStoreForJavaObjects;
import edu.pjwstk.demo.expression.*;
import edu.pjwstk.demo.expression.binary.*;
import edu.pjwstk.demo.expression.terminal.DoubleExpression;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import edu.pjwstk.demo.expression.terminal.StringExpression;
import edu.pjwstk.demo.expression.unary.AvgExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.demo.expression.unary.ExistsExpression;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.demo.visitor.ObjectsImporter;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.visitor.ASTVisitor;

import java.util.Stack;

public class Demo {

    private static Stack<IAbstractQueryResult> qres = new Stack<>();
    private static ISBAStoreJavaObjects store = new SBAStoreForJavaObjects();
    private static ASTVisitor visitor;

    public static void main(String[] args){

        LoadData();

        visitor = new ConcreteASTVisitor(store, qres);

        SolveDemoQuery();

        // 1. Firma where (avg(zatrudnia.pensja) > 2550.50)
        //SolveQuery1();

        // 2. Pracownik where (adres.miasto in (bag(„Warszawa”, „Łódź”)))
        //SolveQuery2();

        // 3. bag(1,2+1) in bag(4-1,3-2) – Uwaga! Bag jest operatorem unarnym
        //SolveQuery3();

        // 4. (Pracownik where nazwisko=”Kowalski”).(adres where miasto=”Łódź”)
        //SolveQuery4();
    }

    // SELECT Age FROM Person WHERE Married = 1
    private static void SolveDemoQuery() {
        Expression expression =
            new AvgExpression(
                new DotExpression(
                    new WhereExpression(
                        new NameExpression("Person"),
                        new NameExpression("Married")
                    ),
                    new NameExpression("Age")
                )
            );

        expression.accept(visitor);

        Log("Result from Demo:");
        Log(qres.pop());
    }

    private static void SolveQuery1() {
        Expression expression =
            new WhereExpression(
                new NameExpression("Company"),
                new GreaterThanExpression(
                    new AvgExpression(
                        new DotExpression(
                            new NameExpression("Employees"),
                            new NameExpression("Salary")
                        )
                    ),
                    new DoubleExpression(2550.50)
                )
            );

        expression.accept(visitor);

        Log("Result from Query 1:");
        Log(qres.pop());
    }

    private static void SolveQuery2() {
        Expression expression =
            new WhereExpression(
                new NameExpression("Person"),
                new InExpression(
                    new DotExpression(
                        new NameExpression("Address"),
                        new NameExpression("City")
                    ),
                    new BagExpression(
                        new Expression[]{
                            new StringExpression("Warszawa"),
                            new StringExpression("Łódź")
                        }
                    )
                )
            );

        expression.accept(visitor);

        Log("Result from Query 2:");
        Log(qres.pop());
    }

    private static void SolveQuery3() {
        Expression expression =
            new InExpression(
                new BagExpression(
                    new Expression[]{
                        new IntegerExpression(1),
                        new PlusExpression(
                            new IntegerExpression(2),
                            new IntegerExpression(1)
                        )
                    }
                ),
                new BagExpression(
                    new Expression[]{
                        new MinusExpression(
                            new IntegerExpression(4),
                            new IntegerExpression(1)
                        ),
                        new MinusExpression(
                            new IntegerExpression(3),
                            new IntegerExpression(2)
                        )
                    }
                )
            );

        expression.accept(visitor);

        Log("Result from Query 3:");
        Log(qres.pop());
    }

    private static void SolveQuery4() {
        Expression expression =
            new DotExpression(
                new WhereExpression(
                    new NameExpression("Person"),
                    new EqualsExpression(
                        new NameExpression("LastName"),
                        new StringExpression("Kowalski")
                    )
                ),
                new WhereExpression(
                    new NameExpression("Address"),
                    new EqualsExpression(
                        new NameExpression("City"),
                        new StringExpression("Łódź")
                    )
                )
            );

        expression.accept(visitor);

        Log("Result from Query 4:");
        Log(qres.pop());
    }

    private static void LoadData() {
        ExampleData data = new ExampleData();
        ObjectsImporter importer = new ObjectsImporter(store);
        importer.IntoStore(data.getPersons());
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
