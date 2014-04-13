package edu.pjwstk.mt_jc.datastore;

import edu.pjwstk.jps.datastore.IDoubleObject;
import edu.pjwstk.jps.datastore.IOID;

public class DoubleObject extends SimpleObject<Double> implements IDoubleObject {
    public DoubleObject(IOID id, String name, Double value) {
        super(id, name, value);
    }
}
