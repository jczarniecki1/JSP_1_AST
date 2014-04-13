package edu.pjwstk.mt_jc.datastore;

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

    @Override
    public String toString() {
        // np. <i2, osoba, {i3,i4,i5,i6,i7,i11}
        int index = 0;
        String s = "<";
        s = s +  getOID().toString();
        s = s + ", " + getName()+ ", {";
        for (IOID child : children) {
            s = s + child.toString();
            index =+ 1;
            if (index < children.size()) {
                s = s + ",";
            }
        }
        s = s + "}>";
        return s;
    }
}
