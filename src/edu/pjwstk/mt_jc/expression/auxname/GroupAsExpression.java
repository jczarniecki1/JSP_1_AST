package edu.pjwstk.mt_jc.expression.auxname;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.ast.auxname.IGroupAsExpression;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class GroupAsExpression extends AuxiliaryNameExpression implements IGroupAsExpression {

    public GroupAsExpression(IExpression expression, String auxiliaryName) {
        super(expression, auxiliaryName);
    }


    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitGroupAsExpression(this);
    }
}
