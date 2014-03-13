package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IBooleanObject;
import edu.pjwstk.jps.datastore.IOID;

public class BooleanObject extends SimpleObject<Boolean> implements IBooleanObject {
    public BooleanObject(IOID id, String name, Boolean value) {
        super(id, name, value);
    }
}
