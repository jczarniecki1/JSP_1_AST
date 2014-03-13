package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IComplexObject;
import edu.pjwstk.jps.datastore.IOID;

import java.util.List;

public class ComplexObject extends SBAObject implements IComplexObject {
    protected IOID id;
    protected List<IOID> children;
    protected String name;

    public ComplexObject(IOID id, String name, List<IOID> children) {
        super(id, name);
        this.children = children;
    }

    @Override
    public List<IOID> getChildOIDs() {
        return children;
    }
}
