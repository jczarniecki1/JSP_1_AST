package edu.pjwstk.demo.expression.binary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.jps.ast.binary.IMultiplyExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class MultiplyExpression extends BinaryExpression implements IMultiplyExpression{

	public MultiplyExpression(Expression leftExpr, Expression rightExpr) {
		super(leftExpr, rightExpr);
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visitMultiplyExpression(this);
	}
}
