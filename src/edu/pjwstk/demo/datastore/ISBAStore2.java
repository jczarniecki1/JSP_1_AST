package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.result.IBagResult;

public interface ISBAStore2 extends ISBAStore {
    public IOID getLastOID();
    public IBagResult getFakeBag(String name);
}
