package edu.pjwstk.mt_jc.datastore;

import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.IReferenceResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.Collection;

public interface IStoreRepository {
    public ISingleResult getField(IReferenceResult reference, String fieldName);
    public IAbstractQueryResult getFields(IReferenceResult reference, String fieldName);
    public Collection<ISingleResult> getCollection(String name);
    public IBagResult getCollectionAsBag(IReferenceResult reference);
    public Object get(IReferenceResult reference);
}
