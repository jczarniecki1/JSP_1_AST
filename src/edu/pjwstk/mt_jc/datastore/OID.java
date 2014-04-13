package edu.pjwstk.mt_jc.datastore;

import edu.pjwstk.jps.datastore.IOID;

public class OID implements IOID {

    private Integer id;

    public OID(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj){
        return
            obj instanceof OID
            && ((OID)obj).id == this.id;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public String toString(){
        return "OID[" + id + "]";
    }
}
