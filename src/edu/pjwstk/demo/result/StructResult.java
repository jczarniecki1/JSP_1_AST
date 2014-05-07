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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructResult)) return false;

        StructResult that = (StructResult) o;

        if (values != null ? !values.equals(that.values) : that.values != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return values != null ? values.hashCode() : 0;
    }

    @Override
    public String toString() {
        int index = 0;
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
