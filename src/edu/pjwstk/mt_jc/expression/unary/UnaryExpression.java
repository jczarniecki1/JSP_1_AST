package edu.pjwstk.mt_jc.expression.unary;

import edu.pjwstk.mt_jc.expression.Expression;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.unary.IUnaryExpression;

// wyrażenia jednoargumentowe
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
