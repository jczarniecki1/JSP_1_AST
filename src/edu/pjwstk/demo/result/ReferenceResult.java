package edu.pjwstk.demo.result;

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
    public String toString(){
        return objectId.toString();
    }
}
