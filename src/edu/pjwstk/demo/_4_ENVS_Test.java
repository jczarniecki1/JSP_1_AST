package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.expression.binary.DotExpression;
import edu.pjwstk.demo.expression.binary.WhereExpression;
import edu.pjwstk.demo.expression.terminal.NameExpression;
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

        SolveDemoQuery();

        // 1. ((emp where married) union (emp where lName=”Nowak)).fName
        //SolveQuery1();

        // 2. ((emp where exists address) where address.number>20).(street, city)
        //SolveQuery2();

    }

    private static void LoadData() {

        store.loadXML("res/envs_data.xml");
    }

    // SELECT a.City
    // FROM   Address a
    // INNER  JOIN Person p ON a.Id = p.Address_Id
    // WHERE  p.Married = 1;
    private static void SolveDemoQuery() {
        Expression expression =
            new DotExpression(
                new DotExpression(
                    new WhereExpression(
                        new NameExpression("emp"),
                        new NameExpression("married")
                    ),
                    new NameExpression("address")
                ),
                new NameExpression("city")
            );

        expression.accept(visitor);

        Log("Result from Demo:");
        Log(qres.pop());
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
