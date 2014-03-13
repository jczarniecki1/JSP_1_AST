package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;

import java.util.Collection;

public class SBAStore2 extends SBAStore implements ISBAStore2 {

    @Override
    public IOID getLastOID() {
        return new OID(lastGeneratedId);
    }
}
