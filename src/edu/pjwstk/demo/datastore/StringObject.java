package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.IStringObject;

public class StringObject extends SimpleObject<String> implements IStringObject{
    public StringObject(IOID id, String name, String value) {
        super(id, name, value);
    }
}
