package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;

import java.util.*;

public class SBAStore implements ISBAStore {

    protected Integer lastGeneratedId = 0;
    protected HashMap<IOID, ISBAObject> hash = new HashMap<>();

    @Override
    public IOID generateUniqueOID() {
        lastGeneratedId += 1;
        return new OID(lastGeneratedId);
    }

    @Override
    public ISBAObject retrieve(IOID oid) {
        return hash.get(oid);
    }

    @Override
    public IOID getEntryOID() {
        return null;
    }

    @Override
    public void loadXML(String filePath) {

    }

    @Override
    public void addJavaObject(Object o, String name) {
        IOID id = generateUniqueOID();
        if (o instanceof String)  hash.put(id, new StringObject (id, name, (String) o));
        else if (o instanceof Integer) hash.put(id, new IntegerObject(id, name, (Integer)o));
        else if (o instanceof Double)  hash.put(id, new DoubleObject (id, name, (Double) o));
        else if (o instanceof Boolean) hash.put(id, new BooleanObject(id, name, (Boolean)o));
        else if (o instanceof IOID[])  hash.put(id, new ComplexObject(id, name, Arrays.asList((IOID[]) o)));
    }

    @Override
    public void addJavaCollection(Collection o, String collectionName) {

    }
}
