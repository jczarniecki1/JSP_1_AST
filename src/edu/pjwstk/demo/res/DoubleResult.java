package edu.pjwstk.demo.res;

import edu.pjwstk.jps.result.IDoubleResult;

public class DoubleResult implements IDoubleResult {
	private Double value;

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
