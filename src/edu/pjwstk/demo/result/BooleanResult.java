package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.IBooleanResult;

public class BooleanResult implements IBooleanResult {
	private Boolean value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BooleanResult)) return false;

        BooleanResult that = (BooleanResult) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public BooleanResult(Boolean value) {
		this.value = value;
	}
	
	public Boolean getValue() {
		return value;
	}

    @Override
    public String toString(){
        return value.toString();
    }
}
