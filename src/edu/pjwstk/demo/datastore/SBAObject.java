package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;

public abstract class SBAObject implements ISBAObject {
    protected IOID id;
    protected String name;

    public SBAObject(IOID id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(IOID id){
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IOID getOID() {
        return id;
    }
}
