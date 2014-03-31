package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.result.IBinderResult;

/**
 * Created by Małgorzata on 31.03.14.
 */
public class BinderResult implements IBinderResult {
    private String name;
    private IAbstractQueryResult result;

    public BinderResult(String name, IAbstractQueryResult result) {
        this.name = name;
        this.result = result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IAbstractQueryResult getValue() {
        return result;
    }

    @Override
    public String toString() {
        return "binder(" +
                "name=\"" + name + '\"' +
                ",value=" + result +
                ')';
    }
}
