package edu.pjwstk.mt_jc.expression.binary;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.binary.IInExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class InExpression extends BinaryExpression implements IInExpression {
public InExpression(IExpression left, IExpression right) {
    super(left, right);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitInExpression(this);
    }
}
