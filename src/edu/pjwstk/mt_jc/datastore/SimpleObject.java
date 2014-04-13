package edu.pjwstk.mt_jc.datastore;

import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISimpleObject;

public abstract class SimpleObject<T> extends SBAObject implements ISimpleObject<T> {
    protected T value;

    public SimpleObject(IOID id, String name, T value) {
        super(id, name);
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "<" + getOID() + ", " + getName() + "," + getValueString() + ">";
    }

    private String getValueString() {
        if (value instanceof String) {
            return "„" + getValue() + "”";
        } else {
            return value.toString();
        }
    }
}
