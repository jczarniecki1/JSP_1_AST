package edu.pjwstk.demo.expression.terminal;

import edu.pjwstk.jps.ast.terminal.INameTerminal;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class NameExpression extends TerminalExpression<String> implements INameTerminal{

    public NameExpression(String value){
        super(value);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visitNameTerminal(this);
    }

    @Override
    public String getName() {
        return value;
    }
}
