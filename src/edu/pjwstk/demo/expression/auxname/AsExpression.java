package edu.pjwstk.demo.expression.auxname;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.auxname.IAsExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class AsExpression extends AuxiliaryNameExpression implements IAsExpression {

    public AsExpression (IExpression expression, String auxiliaryName){
        super(expression,auxiliaryName);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitAsExpression(this);
    }
}
