package edu.pjwstk.demo.res;

import edu.pjwstk.jps.result.IIntegerResult;

public class IntegerResult implements IIntegerResult {
	private Integer value;
	
	public IntegerResult(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}

    @Override
    public String toString(){
        return value.toString();
    }
}
