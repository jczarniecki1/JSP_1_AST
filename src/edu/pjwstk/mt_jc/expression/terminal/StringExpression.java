package edu.pjwstk.mt_jc.expression.terminal;

import edu.pjwstk.jps.ast.terminal.IStringTerminal;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class StringExpression extends TerminalExpression<String> implements IStringTerminal {

    public StringExpression(String value) {
        super(value);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitStringTerminal(this);
    }
}
