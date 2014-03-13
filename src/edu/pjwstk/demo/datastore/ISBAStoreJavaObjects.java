package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.result.IBagResult;

public interface ISBAStoreJavaObjects extends ISBAStore {
    public IOID getLastOID();
    public IBagResult getBag(String name);
}
