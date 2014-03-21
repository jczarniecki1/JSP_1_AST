package edu.pjwstk.jps.ast.binary;

import edu.pjwstk.demo.expression.binary.BinaryExpression;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class AndExpression extends BinaryExpression implements IAndExpression {
    public AndExpression(IExpression left, IExpression right) {
        super(left, right);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitAndExpression(this);
    }
}
