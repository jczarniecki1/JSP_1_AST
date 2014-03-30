package edu.pjwstk.demo.expression.auxname;

import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.auxname.IAuxiliaryNameExpression;

public abstract class AuxiliaryNameExpression extends Expression implements IAuxiliaryNameExpression {
    private IExpression expression;
    private String auxiliaryName;

    public AuxiliaryNameExpression(IExpression expression, String auxiliaryName) {
        this.expression = expression;
        this.auxiliaryName = auxiliaryName;
    }

    @Override
    public String getAuxiliaryName() {
        return auxiliaryName;
    }

    @Override
    public IExpression getInnerExpression() {
        return expression;
    }
}
