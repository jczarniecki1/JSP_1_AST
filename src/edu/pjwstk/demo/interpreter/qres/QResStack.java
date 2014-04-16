package edu.pjwstk.demo.interpreter.qres;

import edu.pjwstk.jps.interpreter.qres.IQResStack;
import edu.pjwstk.jps.result.IAbstractQueryResult;

import java.util.Stack;

public class QResStack implements IQResStack {
    private Stack<IAbstractQueryResult> stack;

    public QResStack() {
        this.stack = new Stack<>();
    }

    @Override
    public IAbstractQueryResult pop() {
        return stack.empty() ? null : stack.pop();
    }

    @Override
    public void push(IAbstractQueryResult value) {
        stack.push(value);
    }
}
