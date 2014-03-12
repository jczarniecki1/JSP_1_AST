package edu.pjwstk.demo.res;

import edu.pjwstk.jps.result.IBooleanResult;

public class BooleanResult implements IBooleanResult {
	private Boolean value;

	public BooleanResult(Boolean value) {
		this.value = value;
	}
	
	public Boolean getValue() {
		return value;
	}
}
