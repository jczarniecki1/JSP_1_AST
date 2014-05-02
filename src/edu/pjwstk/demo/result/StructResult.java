package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.ISingleResult;
import edu.pjwstk.jps.result.IStructResult;

import java.util.List;

public class StructResult extends SingleResult implements IStructResult {

    private List<ISingleResult> values;

    public StructResult(List<ISingleResult> elements) {
        values = elements;
    }

    @Override
    public String toString() {
        int index=0;
        String textResult = "struct(";
        if (values != null) {
            for (ISingleResult innerResult : values) {

                textResult += (index > 0 ? "," : "") + innerResult;
                index++;
            }
        }
        textResult += ")";
        return textResult;
    }

    @Override
    public List<ISingleResult> elements() {
        return values;
    }
}
