package edu.pjwstk.demo.result;

import edu.pjwstk.demo.datastore.StaticReferenceResolver;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.result.IReferenceResult;

public class ReferenceResult implements IReferenceResult {

    private IOID objectId;

    public ReferenceResult(IOID objectId) {
        this.objectId = objectId;
    }

    @Override
    public IOID getOIDValue() {
        return objectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReferenceResult)) return false;

        ReferenceResult that = (ReferenceResult) o;

        if (objectId != null ? !objectId.equals(that.objectId) : that.objectId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return objectId != null ? objectId.hashCode() : 0;
    }

    @Override

    public String toString(){
        return "ref("+ StaticReferenceResolver.valueOrName(objectId) +")";
    }
}
