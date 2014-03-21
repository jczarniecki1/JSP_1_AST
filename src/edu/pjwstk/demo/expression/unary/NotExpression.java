package edu.pjwstk.demo.expression.unary;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.unary.INotExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class NotExpression extends UnaryExpression implements INotExpression {
    public NotExpression(IExpression expression) {
        super(expression);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitNotExpression(this);
    }
}
