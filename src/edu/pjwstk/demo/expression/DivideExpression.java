package edu.pjwstk.demo.expression;

import edu.pjwstk.jps.ast.binary.IDivideExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class DivideExpression extends BinaryExpression implements IDivideExpression {

    public DivideExpression(Expression leftExpr, Expression rightExpr) {
		super(leftExpr, rightExpr);
	}

	@Override
	public void accept(ASTVisitor visitor) {
        visitor.visitDivideExpression(this);
	}
}
