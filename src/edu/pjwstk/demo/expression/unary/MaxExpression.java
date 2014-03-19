package edu.pjwstk.demo.expression.unary;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.unary.IMaxExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class MaxExpression extends UnaryExpression implements IMaxExpression{
    public MaxExpression(IExpression expression) {
        super(expression);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitMaxExpression(this);
    }
}
