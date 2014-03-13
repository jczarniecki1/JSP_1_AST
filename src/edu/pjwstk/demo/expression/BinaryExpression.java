package edu.pjwstk.demo.expression;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.binary.IBinaryExpression;

public abstract class BinaryExpression extends Expression implements IBinaryExpression {

    protected final IExpression left;
    protected final IExpression right;

    public BinaryExpression(IExpression left, IExpression right){
        this.left = left;
        this.right = right;
    }

    @Override
    public IExpression getLeftExpression() {
        return left;
    }

    @Override
    public IExpression getRightExpression() {
        return right;
    }
}
