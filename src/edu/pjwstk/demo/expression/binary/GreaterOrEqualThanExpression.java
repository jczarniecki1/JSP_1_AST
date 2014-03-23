package edu.pjwstk.demo.expression.binary;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.binary.IGreaterOrEqualThanExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class GreaterOrEqualThanExpression extends BinaryExpression implements IGreaterOrEqualThanExpression
{
    public GreaterOrEqualThanExpression(IExpression left, IExpression right) {
        super(left, right);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitGreaterOrEqualThanExpression(this);
    }
}
