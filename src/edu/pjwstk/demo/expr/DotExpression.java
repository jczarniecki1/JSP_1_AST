package edu.pjwstk.demo.expr;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.binary.IDotExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class DotExpression extends BinaryExpression implements IDotExpression {

    public DotExpression(IExpression left, IExpression right) {
        super(left, right);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitDotExpression(this);
    }
}
