package edu.pjwstk.demo.expression.unary;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.unary.ISumExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class SumExpression extends UnaryExpression implements ISumExpression {
    public SumExpression(IExpression expression) {
        super(expression);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitSumExpression(this);
    }
}
