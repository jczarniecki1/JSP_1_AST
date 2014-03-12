package edu.pjwstk.demo.expr;

import edu.pjwstk.jps.ast.binary.IMinusExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class MinusExpression extends BinaryExpression implements IMinusExpression {

	public MinusExpression(Expression leftExpr, Expression rightExpr) {
		super(leftExpr, rightExpr);
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitMinusExpression(this);
	}
}
