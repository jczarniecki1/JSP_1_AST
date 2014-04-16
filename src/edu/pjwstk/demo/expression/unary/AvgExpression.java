package edu.pjwstk.demo.expression.unary;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.unary.IAvgExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class AvgExpression extends UnaryExpression implements IAvgExpression {
    public AvgExpression(IExpression expression) {
        super(expression);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitAvgExpression(this);
    }
}
