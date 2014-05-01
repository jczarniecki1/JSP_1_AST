package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.result.IReferenceResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.Collection;
import java.util.stream.Stream;

public interface IStoreRepository {
    public Stream<ISingleResult> getField(IReferenceResult reference, String fieldName);
    public Collection<ISingleResult> getCollection(String name);
    public Object get(IReferenceResult reference);
    public String printById(IOID id);
    public ISBAStore getStore();
}
