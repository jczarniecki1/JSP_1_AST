package edu.pjwstk.demo.res;

import edu.pjwstk.jps.result.IStringResult;

public class StringResult implements IStringResult{
    private String value;

    public StringResult(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
