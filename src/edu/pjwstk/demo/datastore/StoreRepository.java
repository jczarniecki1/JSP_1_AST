package edu.pjwstk.demo.datastore;

import edu.pjwstk.demo.common.lambda.Predicate;
import edu.pjwstk.demo.result.*;
import edu.pjwstk.jps.datastore.IComplexObject;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.result.IReferenceResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.ArrayList;
import java.util.Collection;

public class StoreRepository implements IStoreRepository {
    private ISBAStore store;

    public StoreRepository(ISBAStore store){

        this.store = store;
    }

    @Override
    public ISingleResult getField(IReferenceResult reference, String fieldName) {

        IComplexObject object = getComplexObject(reference);
        return valueByName(object, fieldName);
    }

    // TODO: Obsługa binderów
    @Override
    public Collection<ISingleResult> getCollection(String name) {
        IComplexObject entry = (IComplexObject)store.retrieve(store.getEntryOID());
        return childrenByName(entry, name);
    }

    private ISingleResult valueByName(IComplexObject object, String fieldName) {
        ISBAObject o = firstChild(object,
            y -> y.getName().equals(fieldName)
        );

             if (o instanceof StringObject)  return new StringResult(((StringObject) o).getValue());
        else if (o instanceof IntegerObject) return new IntegerResult(((IntegerObject)o).getValue());
        else if (o instanceof DoubleObject)  return new DoubleResult(((DoubleObject) o).getValue());
        else if (o instanceof BooleanObject) return new BooleanResult(((BooleanObject)o).getValue());
        else return new ReferenceResult(o.getOID());
    }

    private IComplexObject getComplexObject(IReferenceResult reference) {
        IOID id = reference.getOIDValue();
        return  (IComplexObject)store.retrieve(id);
    }

    private ISBAObject firstChild(IComplexObject object, Predicate<ISBAObject> predicate){
        for (IOID id : object.getChildOIDs()) {
            ISBAObject child = store.retrieve(id);
            if (predicate.apply(child)) {
                return child;
            }
        }
        return null;
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
}
