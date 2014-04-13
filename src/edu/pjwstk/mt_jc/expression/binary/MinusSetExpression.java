package edu.pjwstk.mt_jc.expression.binary;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.binary.IMinusSetExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class MinusSetExpression extends BinaryExpression implements IMinusSetExpression {
    public MinusSetExpression(IExpression left, IExpression right) {
        super(left, right);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitMinusSetExpression(this);
    }
}
