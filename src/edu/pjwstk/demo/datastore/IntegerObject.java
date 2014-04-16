package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IIntegerObject;
import edu.pjwstk.jps.datastore.IOID;

/**
 * Created by Jacek on 3/12/14.
 */
public class IntegerObject extends SimpleObject<Integer> implements IIntegerObject {
    public IntegerObject(IOID id, String name, Integer value) {
        super(id, name, value);
    }
}
