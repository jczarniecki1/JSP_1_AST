package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.IStringResult;

public class StringResult implements IStringResult{
    private String value;

    public StringResult(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString(){
        return "\"" + value + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringResult)) return false;

        StringResult that = (StringResult) o;

        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
