package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.auxname.AsExpression;
import edu.pjwstk.demo.expression.auxname.GroupAsExpression;
import edu.pjwstk.demo.expression.binary.*;
import edu.pjwstk.demo.expression.terminal.*;
import edu.pjwstk.demo.expression.unary.AvgExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.demo.expression.unary.CountExpression;
import edu.pjwstk.demo.expression.unary.StructExpression;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.visitor.ASTVisitor;


public class _1_AST_Test {

    private static QResStack qres = new QResStack();
    private static ISBAStore store = new SBAStore();
    private static ASTVisitor visitor;

    public static void main(String[] args){

        LoadData();
        IStoreRepository repository = new StoreRepository(store);
        visitor = new ConcreteASTVisitor(qres, repository);

        SolveDemoQuery();

        // CW_QRES1

        // 1. (struct(1, 2+1), (bag("test", „Ala”) as nazwa));
        SolveQRESQuery1();

        // 2. (bag("ala"+" ma"+" kota"), bag(8*10, false));
        SolveQRESQuery2();

        // 3. ((bag(1, 2) groupas x), bag(3, 4), 5);

        SolveQRESQuery3();


        // CW_AST

        // 1. Firma where (avg(zatrudnia.pensja) > 2550.50)
        SolveQuery1();

        // 2. Pracownik where (adres.miasto in (bag(„Warszawa”, „Łódź”)))
        SolveQuery2();

        // 3. bag(1,2+1) in bag(4-1,3-2) – Uwaga! Bag jest operatorem unarnym
        SolveQuery3();

        // 4. (Pracownik where nazwisko=”Kowalski”).(adres where miasto=”Łódź”)
        SolveQuery4();
    }

    // SELECT a.City
    // FROM   Address a
    // INNER  JOIN Person p ON a.Id = p.Address_Id
    // WHERE  p.Married = 1;
    private static void SolveDemoQuery() {
        Expression expression =
            new CountExpression(
                new DotExpression(
                    new DotExpression(
                        new WhereExpression(
                            new NameExpression("Person"),
                            new NameExpression("Married")
                        ),
                        new NameExpression("Address")
                    ),
                    new NameExpression("City")
                )
            );

        expression.accept(visitor);

        Log("Result from Demo:");
        Log(qres.pop());
    }

    // SELECT c.* FROM Company c
    // INNER  JOIN Employee e ON e.Company_Id = c.Id
    // WHERE  AVG(e.Salary) > 2550.50
    private static void SolveQuery1() {
        Expression expression =
            new WhereExpression(
                new NameExpression("Company"),
                new GreaterThanExpression(
                    new AvgExpression(
                        new DotExpression(
                            new NameExpression("Employee"),
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

    // SELECT p.* FROM Person p
    // INNER  JOIN Address a ON a.Id = p.Address_Id
    // WHERE  a.City IN ('Warszawa', 'Łódź')
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
                        new CommaExpression(
                            new StringExpression("Warszawa"),
                            new StringExpression("Łódź")
                        )
                    )
                )
            );

        expression.accept(visitor);

        Log("Result from Query 2:");
        Log(qres.pop());
    }

    // TODO: Do zaimplementowania dla Gosi :)
    //  bag(1,2+1) in bag(4-1,3-2)

    private static void SolveQuery3() {
        Expression expression =
            new InExpression(
                new BagExpression(
                    new CommaExpression(
                        new IntegerExpression(1),
                        new PlusExpression(
                            new IntegerExpression(2),
                            new IntegerExpression(1)
                        )
                    )
                ),
                new BagExpression(
                    new CommaExpression(
                        new MinusExpression(
                            new IntegerExpression(4),
                            new IntegerExpression(1)
                        ),
                        new MinusExpression(
                            new IntegerExpression(3),
                            new IntegerExpression(2)
                        )
                    )
                )
            );

        expression.accept(visitor);

        Log("Result from Query 3:");
        Log(qres.pop());
    }

    // SELECT p.* FROM Person p
    // INNER  JOIN Address a ON a.Id = p.Address_Id
    // WHERE  p.LastName = 'Kowalski' && a.City = 'Łódź'
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

    //  (struct(1, 2+1), (bag("test", „Ala”) as nazwa));
    private static void SolveQRESQuery1(){

        Expression expression = new CommaExpression(
            new StructExpression(
                new CommaExpression(
                    new IntegerExpression(1),
                    new PlusExpression(
                        new IntegerExpression(2),
                        new IntegerExpression(1)
                    )
                )
            ),
            new AsExpression(
               new BagExpression(
                   new CommaExpression(
                        new StringExpression("test"),
                        new StringExpression("Ala")
                   )
               ),
               "nazwa"
            )
        );
        expression.accept(visitor);

        Log("Result from QRESQuery1:  (struct(1, 2+1), (bag(\"test\", „Ala”) as nazwa)); ");
        Log(qres.pop());
    }

    //   (bag("ala"+" ma"+" kota"), bag(8*10, false));
    private static void SolveQRESQuery2(){

        Expression expression = new CommaExpression(
                new BagExpression(
                        new PlusExpression(
                                new StringExpression("ala"),
                                new PlusExpression(
                                        new StringExpression(" ma"),
                                        new StringExpression(" kota")
                                )
                        )
                ),
                new BagExpression(
                        new CommaExpression(
                                new MultiplyExpression(
                                        new IntegerExpression(8),
                                        new IntegerExpression(10)
                                ),
                                new BooleanExpression(false)
                        )
                )
        );
        expression.accept(visitor);

        Log("Result from QRESQuery2:  (bag(\"ala\"+\" ma\"+\" kota\"), bag(8*10, false)); ");
        Log(qres.pop());
    }

    //    ( (bag(1, 2) groupas x), bag(3, 4), 5);
    private static void SolveQRESQuery3(){

        Expression expression =
                new CommaExpression(
                    new GroupAsExpression(
                        new BagExpression(
                                new CommaExpression(
                                    new IntegerExpression(1),
                                    new IntegerExpression(2)
                                )
                        )
                        ,"x"
                    ),
                    new CommaExpression(
                        new BagExpression(
                            new CommaExpression(
                                new IntegerExpression(3),
                                new IntegerExpression(4)
                            )
                        ),
                        new IntegerExpression(5)
                    )
        );
        expression.accept(visitor);

        Log("Result from QRESQuery3:  ((bag(1, 2) groupas x), bag(3, 4), 5);");
        Log(qres.pop());
    }
    //    ( (bag(1, 2) groupas x), bag(3, 4), 5);

    private static void LoadData() {
        ExampleData data = new ExampleData();
        store.addJavaCollection(data.getPersons(), "Person");
        store.addJavaCollection(data.getCompanies(), "Company");
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
