package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.Collection;

public class BagResult implements IBagResult{

    private Collection<ISingleResult> values;

    public BagResult(Collection<ISingleResult> values) {
        this.values = values;
    }

    @Override
    public Collection<ISingleResult> getElements() {
        return values;
    }

    @Override
    public String toString(){
        String textResult = "{ ";
        for (ISingleResult innerResult : values){
            textResult += (textResult.length() > 2 ? ", " : "")
                    + innerResult;
        }
        textResult += (values.size() > 0 ? " " : "") + "}";
        return textResult;
    }
}
