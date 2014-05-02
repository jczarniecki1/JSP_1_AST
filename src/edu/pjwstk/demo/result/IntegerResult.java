package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.IIntegerResult;

public class IntegerResult implements IIntegerResult {
	private Integer value;
	
	public IntegerResult(Integer value) {
		this.value = value;
	}

    @Override
	public Integer getValue() {
		return value;
	}

    @Override
    public String toString(){
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegerResult)) return false;

        IntegerResult result = (IntegerResult) o;

        if (value != null ? !value.equals(result.value) : result.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
