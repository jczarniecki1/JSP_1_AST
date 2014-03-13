package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAStore;

public interface ISBAStore2 extends ISBAStore {
    public IOID getLastOID();
}
