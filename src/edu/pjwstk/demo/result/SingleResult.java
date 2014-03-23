package edu.pjwstk.demo.result;

import edu.pjwstk.jps.result.ISingleResult;

/**
 * Created by Małgorzata on 22.03.14.
 */
public class SingleResult implements ISingleResult {
    private Double value;

    public SingleResult(double value) {
        this.value = Double.valueOf(value);
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString(){
        return value.toString();
    }
}
