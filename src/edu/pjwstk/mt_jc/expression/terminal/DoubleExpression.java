package edu.pjwstk.mt_jc.expression.terminal;

import edu.pjwstk.jps.ast.terminal.IDoubleTerminal;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class DoubleExpression extends TerminalExpression<Double> implements IDoubleTerminal {

    public DoubleExpression(Double value) {
        super(value);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitDoubleTerminal(this);
    }
}
