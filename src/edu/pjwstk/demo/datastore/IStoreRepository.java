package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.IReferenceResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.Collection;

public interface IStoreRepository {
    public ISingleResult getField(IReferenceResult reference, String fieldName);
    public Collection<ISingleResult> getCollection(String name);
    public IBagResult getCollectionAsBag(IReferenceResult reference);
}
