package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.result.IBinderResult;

<<<<<<< HEAD
/**
 * Created by Małgorzata on 31.03.14.
 */
public class BinderResult implements IBinderResult {
    private String name;
    private IAbstractQueryResult result;

    public BinderResult(String name, IAbstractQueryResult result) {
        this.name = name;
        this.result = result;
=======
public class BinderResult extends SingleResult implements IBinderResult {
    private final IAbstractQueryResult value;
    private final String name;

    public BinderResult(IAbstractQueryResult value, String name) {
        this.value = value;
        this.name = name;
>>>>>>> origin/master
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IAbstractQueryResult getValue() {
<<<<<<< HEAD
        return result;
    }

    @Override
    public String toString() {
        return "binder(" +
                "name=\"" + name + '\"' +
                ",value=" + result +
                ')';
=======
        return value;
    }

    @Override
    public String toString(){
        String textResult = "binder(";
        textResult += "name=\"" + name + "\", ";
        textResult += "value=" + value;
        textResult += ")";
        return textResult;
>>>>>>> origin/master
    }
}
