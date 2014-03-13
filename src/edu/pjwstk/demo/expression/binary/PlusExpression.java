package edu.pjwstk.demo.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.jps.ast.binary.IPlusExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class PlusExpression extends BinaryExpression implements IPlusExpression{

	public PlusExpression(Expression leftExpr, Expression rightExpr) {
		super(leftExpr, rightExpr);
	}
	
	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitPlusExpression(this);
	}
}
