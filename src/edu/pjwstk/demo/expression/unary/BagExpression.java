package edu.pjwstk.demo.expression.unary;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.unary.IBagExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class BagExpression extends UnaryExpression implements IBagExpression {
    public BagExpression(IExpression expression) {
        super(expression);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitBagExpression(this);
    }
}
