package edu.pjwstk.demo.datastore;

import edu.pjwstk.demo.result.*;
import edu.pjwstk.jps.datastore.IComplexObject;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.result.IReferenceResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/*
    Ukrywa niskopoziomowe operacja odczytu z bazy
*/
public class StoreRepository implements IStoreRepository {
    private ISBAStore store;

    private StoreRepository(ISBAStore store) {
        this.store = store;
    }

    private static IStoreRepository instance = null;
    public static IStoreRepository getInstance(){
        if (instance == null){
            instance = new StoreRepository(SBAStore.getInstance());
        }
        return  instance;
    }

    @Override
    public Stream<ISingleResult> getField(IReferenceResult reference, String fieldName) {

        IComplexObject object = getComplexObject(reference);
        return valuesByName2(object, fieldName);
    }

    // TODO: Obsługa binderów
    @Override
    public Collection<ISingleResult> getCollection(String name) {
        IComplexObject entry = (IComplexObject)store.retrieve(store.getEntryOID());
        return childrenByName(entry, name);
    }

    @Override
    public Object get(IReferenceResult reference) {
        return toResult(store.retrieve(reference.getOIDValue()));
    }

    @Override
    public String printById(IOID id) {
        return toString(store.retrieve(id));
    }

    @Override
    public ISBAStore getStore() {
        return store;
    }

    private Stream<ISingleResult> valuesByName2(IComplexObject object, String fieldName) {
        return object.getChildOIDs()
                .stream()
                .map(store::retrieve)
                .filter(x -> x.getName().equals(fieldName))
                .map(this::toResult);
    }

    private IComplexObject getComplexObject(IReferenceResult reference) {
        IOID id = reference.getOIDValue();
        ISBAObject object = store.retrieve(id);
        return  (IComplexObject) object;
    }

    private Collection<ISingleResult> childrenByName(IComplexObject object, String name){
        Collection<ISingleResult> children = new ArrayList<>();
        for (IOID id : object.getChildOIDs()) {
            ISBAObject childObject = store.retrieve(id);
            if (childObject.getName().equals(name)){
                children.add(new ReferenceResult(id));
            }
        }
        return children;
    }

    private ISingleResult toResult(ISBAObject o){
             if (o instanceof StringObject)  return new StringResult(((StringObject) o).getValue());
        else if (o instanceof IntegerObject) return new IntegerResult(((IntegerObject) o).getValue());
        else if (o instanceof DoubleObject)  return new DoubleResult(((DoubleObject) o).getValue());
        else if (o instanceof BooleanObject) return new BooleanResult(((BooleanObject) o).getValue());
        else return new ReferenceResult(o.getOID());
    }

    private String toString(ISBAObject o){
        if (o == null) {
            return "null";
        }
        if (o instanceof ComplexObject)
        {
            return o.getName().toString();
        }
        else
        {
            return toResult(o).toString();
        }
    }
}
