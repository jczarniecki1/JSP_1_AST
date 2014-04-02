package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.Collection;


public class BagResult implements IBagResult{

    private Collection<ISingleResult> values;

    public BagResult() {
        // pusty bag
    }
    public BagResult(Collection<ISingleResult> values) {
        this.values = values;
    }

    @Override
    public Collection<ISingleResult> getElements() {
        return values;
    }

    @Override
    public String toString(){
        int index=0;
        String textResult = "bag(";
        for (ISingleResult innerResult : values){

            textResult += (textResult.length() > 4 ? "," : "")
                    + index + "="+ innerResult;
            index++;
        }
        //textResult += (values.size() > 0 ? " " : "") + ")";
        textResult += ")";
        return textResult;
    }
}
