package edu.pjwstk.mt_jc.expression.terminal;

import edu.pjwstk.jps.ast.terminal.IBooleanTerminal;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class BooleanExpression extends TerminalExpression<Boolean> implements IBooleanTerminal {
    public BooleanExpression(Boolean value) {
        super(value);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitBooleanTerminal(this);
    }
}
