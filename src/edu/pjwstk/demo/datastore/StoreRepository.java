package edu.pjwstk.demo.datastore;

import edu.pjwstk.demo.common.Query;
import edu.pjwstk.demo.common.lambda.Predicate;
import edu.pjwstk.demo.result.*;
import edu.pjwstk.jps.datastore.IComplexObject;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.IReferenceResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.*;
import java.util.stream.Collectors;

/*
    Ukrywa niskopoziomowe operacja odczytu z bazy
*/
public class StoreRepository implements IStoreRepository {
    private ISBAStore store;

    public StoreRepository(ISBAStore store){

        this.store = store;
    }

    @Override
    public IAbstractQueryResult getFields(IReferenceResult reference, String fieldName) {

        IComplexObject object = getComplexObject(reference);
        return valuesByName(object, fieldName);
    }

    @Override
    public ISingleResult getField(IReferenceResult reference, String fieldName) {

        IComplexObject object = getComplexObject(reference);
        Optional<ISingleResult> optionalResult = singleValueByName(object, fieldName);
        return optionalResult.isPresent() ? optionalResult.get() : null;
    }

    // TODO: Obsługa binderów
    @Override
    public Collection<ISingleResult> getCollection(String name) {
        IComplexObject entry = (IComplexObject)store.retrieve(store.getEntryOID());
        return childrenByName(entry, name);
    }

    @Override
    public IBagResult getCollectionAsBag(IReferenceResult reference) {
        IComplexObject object = getComplexObject(reference);
        List<ISingleResult> ids = Query.select(object.getChildOIDs(),
                x -> new ReferenceResult(x));
        return new BagResult(ids);
    }

    private IAbstractQueryResult valuesByName(IComplexObject object, String fieldName) {
        List<ISingleResult> list = object.getChildOIDs()
                .stream()
                .map(x -> store.retrieve(x))
                .filter(x -> x.getName().equals(fieldName))
                .map(o -> {
                         if (o instanceof StringObject) return new StringResult(((StringObject) o).getValue());
                    else if (o instanceof IntegerObject) return new IntegerResult(((IntegerObject) o).getValue());
                    else if (o instanceof DoubleObject) return new DoubleResult(((DoubleObject) o).getValue());
                    else if (o instanceof BooleanObject) return new BooleanResult(((BooleanObject) o).getValue());
                    else return new ReferenceResult(o.getOID());
                })
                .collect(Collectors.toList());
        if (list.size() > 1) return new BagResult(list);
        else return list.iterator().next();
    }


    private Optional<ISingleResult> singleValueByName(IComplexObject object, String fieldName) {
        return object.getChildOIDs()
                .stream()
                .map(x -> store.retrieve(x))
                .filter(x -> x.getName().equals(fieldName))
                .map(o -> {
                         if (o instanceof StringObject) return new StringResult(((StringObject) o).getValue());
                    else if (o instanceof IntegerObject) return new IntegerResult(((IntegerObject) o).getValue());
                    else if (o instanceof DoubleObject) return new DoubleResult(((DoubleObject) o).getValue());
                    else if (o instanceof BooleanObject) return new BooleanResult(((BooleanObject) o).getValue());
                    else return new ReferenceResult(o.getOID());
                })
                .findFirst();
    }

    private IComplexObject getComplexObject(IReferenceResult reference) {
        IOID id = reference.getOIDValue();
        ISBAObject object = store.retrieve(id);
        return  (IComplexObject) object;
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
