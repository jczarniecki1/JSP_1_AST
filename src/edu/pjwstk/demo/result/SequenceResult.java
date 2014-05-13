package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.ISequenceResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.List;

public class SequenceResult extends CollectionResult implements ISequenceResult {

    private List<ISingleResult> elements;

    public SequenceResult(List<ISingleResult> elements) {
        this.elements = elements;
    }

    @Override
    public List<ISingleResult> getElements() {
        return elements;
    }

    @Override
    public String toString(){
        int index=0;
        String textResult = "sequence(";
        if (elements != null) {
            for (ISingleResult innerResult : elements) {

                textResult += (index > 0 ? "," : "") + innerResult;
                index++;
            }
        }
        textResult += ")";
        return textResult;
    }
}
