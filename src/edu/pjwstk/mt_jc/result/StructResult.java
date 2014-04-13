package edu.pjwstk.mt_jc.result;

import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.result.ISingleResult;
import edu.pjwstk.jps.result.IStructResult;

import java.util.List;

public class StructResult extends SingleResult implements IStructResult {

    private List<ISingleResult> elementsOfStruct;

    public StructResult(List<ISingleResult> elements) {
        elementsOfStruct = elements;
    }



    @Override
    public String toString() {
        String textResult = "struct(";
        for (IAbstractQueryResult innerResult : elementsOfStruct){
            if (textResult.length()>7) {
                textResult += ",";
            }
            textResult += innerResult;
        }
        textResult += ")";
        return textResult;
    }

    @Override
    public List<ISingleResult> elements() {
        return elementsOfStruct;
    }
}
