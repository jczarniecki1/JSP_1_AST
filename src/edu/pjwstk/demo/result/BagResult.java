package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.ArrayList;
import java.util.Collection;


public class BagResult implements IBagResult{

    private Collection<ISingleResult> values = new ArrayList<>();

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
        if (values != null) {
            for (ISingleResult innerResult : values) {

                textResult += (index > 0 ? "," : "") + innerResult;
                index++;
            }
        }
        textResult += ")";
        return textResult;
    }

    public static IBagResult Empty() {
        return new BagResult(new ArrayList<>());
    }
}
