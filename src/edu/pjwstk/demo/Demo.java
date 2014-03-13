package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.ISBAStore2;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.SBAStore2;
import edu.pjwstk.demo.expr.DotExpression;
import edu.pjwstk.demo.expr.Expression;
import edu.pjwstk.demo.expr.NameExpression;
import edu.pjwstk.demo.expr.WhereExpression;
import edu.pjwstk.demo.tree.ConcreteASTVisitor;
import edu.pjwstk.demo.tree.ObjectsImporter;
import edu.pjwstk.jps.datastore.IComplexObject;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class Demo {

    private static Stack<IAbstractQueryResult> qres = new Stack<>();
    private static ISBAStore2 store = new SBAStore2();

    public static void main(String[] args){

        Expression expression =
            new DotExpression(
                new DotExpression(
                    new WhereExpression(
                        new NameExpression("Person"),
                        new NameExpression("Married")
                    ),
                    new NameExpression("Address")
                ),
                new NameExpression("Zip")
            );

        ExampleData data = new ExampleData();

        ObjectsImporter importer = new ObjectsImporter(store);
        importer.IntoStore(data.getPersons());

        ASTVisitor visitor = new ConcreteASTVisitor(store, qres);

        expression.accept(visitor);

        Log("Result:");
        Log(qres.pop());
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
