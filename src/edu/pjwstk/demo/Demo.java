package edu.pjwstk.demo;

import edu.pjwstk.demo.expr.DotExpression;
import edu.pjwstk.demo.expr.Expression;
import edu.pjwstk.demo.expr.NameExpression;
import edu.pjwstk.demo.expr.WhereExpression;
import edu.pjwstk.demo.tree.ConcreteASTVisitor;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class Demo {

    public static void main(String[] args){

        Expression expression =
            new DotExpression(
                new DotExpression(
                    new WhereExpression(
                        new NameExpression("osoba"),
                        new NameExpression("żonaty")
                    ),
                    new NameExpression("książka")
                ),
                new NameExpression("autor")
            );

        ASTVisitor visitor = new ConcreteASTVisitor();

        expression.accept(visitor);

        Log("Result:");
        Log("TODO :)");
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
