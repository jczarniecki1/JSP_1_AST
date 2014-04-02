package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.result.IBinderResult;

/**
 * Created by Małgorzata on 31.03.14.
 */

public class BinderResult extends SingleResult implements IBinderResult {
    private final IAbstractQueryResult result;
    private final String name;

    public BinderResult(IAbstractQueryResult result, String name) {
        this.result = result;
        this.name = name;

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
    public String toString(){
        String textResult = "binder(";
        textResult += "name=\"" + name + "\",";
        textResult += "value=" + result;
        textResult += ")";
        return textResult;
    }
}
