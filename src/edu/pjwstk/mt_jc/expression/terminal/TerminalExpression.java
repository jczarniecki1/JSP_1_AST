package edu.pjwstk.mt_jc.expression.terminal;

import edu.pjwstk.mt_jc.expression.Expression;
import edu.pjwstk.jps.ast.terminal.ITerminalExpression;

public abstract class TerminalExpression<T> extends Expression implements ITerminalExpression<T> {

    protected T value;

    public TerminalExpression(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }
}
