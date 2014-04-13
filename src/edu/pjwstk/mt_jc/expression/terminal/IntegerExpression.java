package edu.pjwstk.mt_jc.expression.terminal;

import edu.pjwstk.jps.ast.terminal.IIntegerTerminal;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class IntegerExpression extends TerminalExpression<Integer> implements IIntegerTerminal {

    public IntegerExpression(Integer value) {
        super(value);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitIntegerTerminal(this);
    }
}
