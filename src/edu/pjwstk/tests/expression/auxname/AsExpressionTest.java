package edu.pjwstk.tests.expression.auxname;


import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.auxname.AsExpression;
import edu.pjwstk.demo.expression.binary.*;
import edu.pjwstk.demo.expression.terminal.IntegerExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
import edu.pjwstk.demo.expression.terminal.StringExpression;
import edu.pjwstk.demo.expression.unary.BagExpression;
import edu.pjwstk.demo.expression.unary.StructExpression;
import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;
import edu.pjwstk.demo.result.IntegerResult;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.result.IBinderResult;
import edu.pjwstk.jps.result.IIntegerResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AsExpressionTest extends AbstractAuxiliaryNameExpressionTest{

    @Before
    public void BeforeTest(){
        super.InitStore();

        List<Person> persons = new ArrayList<>();

        persons.add(new Person("Marcin","Lewandowski",  20, true,  new Address("Gdańsk")));
        persons.add(new Person("Jan","Kowalski",        21, true,  new Address("Łódź")));

        store.addJavaCollection(persons, "Person");
        super.InitVisitor();
    }

    @Test
    public void shouldBindNameToInteger() throws Exception {
        Expression e = new AsExpression(
                            new IntegerExpression(12),
                            "liczba"
                        );
        e.accept(visitor);
        IAbstractQueryResult result = qres.pop();
        assertEquals("<liczba,12>", result.toString());
    }

    @Test
    public void shouldBindElementsInBag() throws Exception {
        //bag(1,2) as num
        Expression e = new AsExpression(
                new BagExpression(
                    new CommaExpression(
                        new IntegerExpression(1),
                        new IntegerExpression(2)
                    )
                ),
                "num"
        );
        e.accept(visitor);
        IAbstractQueryResult result = qres.pop();
        assertEquals("bag(<num,1>,<num,2>)", result.toString());

    }

    @Test

    public void shouldBindElementsInBag2() throws Exception {
        //bag(1,2) as num
        Expression e = new AsExpression(
                new BagExpression(
                        new CommaExpression(
                                new StringExpression("test"),
                                new StringExpression("Ala")
                        )
                ),
                "nazwa"
        );
        e.accept(visitor);
        IAbstractQueryResult result = qres.pop();
        assertEquals("bag(<nazwa,\"test\">,<nazwa,\"Ala\">)", result.toString());

    }

    @Test
    public void shouldBindNameToString() throws Exception {
        Expression e = new AsExpression(
                new StringExpression("dwanaście"),
                "liczba"
        );
        e.accept(visitor);
        IAbstractQueryResult result = qres.pop();
        assertEquals("<liczba,\"dwanaście\">", result.toString());
    }

    @Test
    public void shouldBindNameToIntegerTerminal() throws Exception {

        Expression e = new AsExpression(
                new IntegerExpression(12),
                "testName1"
            );
        IBinderResult[] results = getBinders(e);

        IIntegerResult resultValue = (IIntegerResult)(results[0].getValue());
        String resultName = results[0].getName();

        assertEquals(resultValue.getValue(),new Integer(12));
        assertEquals(resultName, "testName1");
    }

    @Test
    public void shouldBeAbleToUseBinding_SimpleExample() throws Exception {

        Expression e =
            new DotExpression(
                new AsExpression(
                    new IntegerExpression(1),
                    "n"
                ),
                new NameExpression("n")
            );

        e.accept(visitor);
        IntegerResult result = (IntegerResult)qres.pop();

        assertEquals(1, (long)result.getValue());
    }

    @Test
    public void shouldBeAbleToUseBinding_1() throws Exception {
    // (Person union bag(struct("Zuzanna" as firstName, "Nowakowska" as lastName))).firstName

        Expression e =
            new DotExpression(
                new StructExpression(
                    new CommaExpression(
                        new AsExpression(
                            new StringExpression("Zuzanna"),
                            "firstName"
                        ),
                        new AsExpression(
                            new StringExpression("Nowakowska"),
                            "lastName"
                        )
                    )
                ),
                new NameExpression("firstName")
            );

        e.accept(visitor);

        assertEquals("\"Zuzanna\"", qres.pop().toString());

        e = new DotExpression(
                new NameExpression("Person"),
                new NameExpression("firstName")
            );

        e.accept(visitor);

        assertEquals("bag(ref(\"Marcin\"),ref(\"Jan\"))", qres.pop().toString());

        e = new DotExpression(
                new UnionExpression(
                    new NameExpression("Person"),
                    new BagExpression(
                        new StructExpression(
                            new CommaExpression(
                                new AsExpression(
                                    new StringExpression("Zuzanna"),
                                    "firstName"
                                ),
                                new AsExpression(
                                    new StringExpression("Nowakowska"),
                                    "lastName"
                                )
                            )
                        )
                    )
                ),
                new NameExpression("firstName")
            );

        e.accept(visitor);

        assertEquals("bag(ref(\"Marcin\"),ref(\"Jan\"),\"Zuzanna\")", qres.pop().toString());
    }

    @Test
    public void shouldBeAbleToUseBinding_2() throws Exception {
        // ((Person union bag(struct("Zuzanna" as firstName, 29 as age))) where age > 20).firstName
        Expression e =
            new DotExpression(
                new WhereExpression(
                    new UnionExpression(
                        new NameExpression("Person"),
                        new UnionExpression(
                                new StructExpression(
                                    new CommaExpression(
                                        new AsExpression(
                                            new StringExpression("Zuzanna"),
                                            "firstName"
                                        ),
                                        new AsExpression(
                                            new IntegerExpression(29),
                                            "age"
                                        )
                                    )
                                ),
                                new StructExpression(
                                    new CommaExpression(
                                        new AsExpression(
                                            new StringExpression("Zuzanna2"),
                                            "firstName"
                                        ),
                                        new AsExpression(
                                            new IntegerExpression(29),
                                            "age"
                                        )
                                    )
                                )
                        )
                    ),
                    new GreaterThanExpression(
                        new NameExpression("age"),
                        new IntegerExpression(20)
                    )
                ),
                new NameExpression("firstName")
            );

        e.accept(visitor);

        assertEquals("bag(ref(\"Jan\"),\"Zuzanna\")", qres.pop().toString());
    }
}
