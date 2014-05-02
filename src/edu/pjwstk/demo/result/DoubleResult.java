package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.IDoubleResult;

public class DoubleResult implements IDoubleResult {
	private Double value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleResult)) return false;

        DoubleResult result = (DoubleResult) o;

        if (value != null ? !value.equals(result.value) : result.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public DoubleResult(Double value) {
		this.value = value;
	}
	
	public Double getValue() {
		return value;
	}

    @Override
    public String toString(){
        return value.toString();
    }
}
