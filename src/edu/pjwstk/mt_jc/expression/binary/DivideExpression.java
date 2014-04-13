package edu.pjwstk.mt_jc.expression.binary;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.binary.IDivideExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class DivideExpression extends BinaryExpression implements IDivideExpression {

    public DivideExpression(IExpression left, IExpression right) {
        super(left, right);
    }

    @Override
	public void accept(ASTVisitor visitor) {
        visitor.visitDivideExpression(this);
	}
}
