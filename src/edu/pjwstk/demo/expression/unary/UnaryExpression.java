package edu.pjwstk.demo.expression.unary;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.unary.IUnaryExpression;

public abstract class UnaryExpression extends Expression implements IUnaryExpression {

    protected final IExpression expression;

    public UnaryExpression(IExpression expression){
        this.expression = expression;
    }

    @Override
    public IExpression getInnerExpression() {
        return expression;
    }
}
