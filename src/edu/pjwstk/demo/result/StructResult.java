package edu.pjwstk.demo.result;

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
        int index=0;
        for (ISingleResult innerResult : elementsOfStruct){
            if (textResult.length()>7) {
                textResult += ",";
            }
            textResult += index + "=" + innerResult;
            index++;
        }
        textResult += ")";
        return textResult;
    }

    @Override
    public List<ISingleResult> elements() {
        return elementsOfStruct;
    }
}
