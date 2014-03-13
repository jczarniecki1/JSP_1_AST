package edu.pjwstk.demo.expression;

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
