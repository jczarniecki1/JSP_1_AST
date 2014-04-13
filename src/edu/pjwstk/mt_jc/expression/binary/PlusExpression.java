package edu.pjwstk.mt_jc.expression.binary;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.binary.IPlusExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class PlusExpression extends BinaryExpression implements IPlusExpression{

    public PlusExpression(IExpression left, IExpression right) {
        super(left, right);
    }

    @Override
	public void accept(ASTVisitor visitor) {
		visitor.visitPlusExpression(this);
	}
}
