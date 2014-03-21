package edu.pjwstk.demo.datastore;

import edu.pjwstk.demo.common.Query;
import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;

import java.util.*;

public class SBAStore implements ISBAStore {

    protected Integer lastGeneratedId = 0;
    protected HashMap<IOID, ISBAObject> hash = new HashMap<>();
    private IOID entryOID;
    private IOID lastOID;

    @Override
    public IOID generateUniqueOID() {
        lastGeneratedId += 1;
        lastOID = new OID(lastGeneratedId);
        return lastOID;
    }

    @Override
    public ISBAObject retrieve(IOID oid) {
        return hash.get(oid);
    }

    @Override
    public IOID getEntryOID() {
        return entryOID;
    }

    @Override
    public void loadXML(String filePath) {

    }

    @Override
    public void addJavaObject(Object o, String name) {
        IOID id = generateUniqueOID();
        if (o instanceof String)  hash.put(id, new StringObject (id, name, (String) o));
        else if (o instanceof Integer) hash.put(id,new IntegerObject(id, name, (Integer)o));
        else if (o instanceof Double)  hash.put(id,new DoubleObject (id, name, (Double) o));
        else if (o instanceof Boolean) hash.put(id,new BooleanObject(id, name, (Boolean)o));
        else if (o instanceof IOID[])  hash.put(id,new ComplexObject(id, name, Arrays.asList((IOID[]) o)));
        else visit(o);
    }

    @Override
    public void addJavaCollection(Collection collection, String name) {
        entryOID = generateUniqueOID();
        List<IOID> childrenIds = Query.select(collection,x -> {
            addJavaObject(x, name);
            return lastOID;
        });
        hash.put(getEntryOID(), new ComplexObject(entryOID, "entry", childrenIds));
    }
    
    public IOID visitPerson(Person person) {
        Address address = person.getAddress();
        return importComplex("Person",
            new IOID[]{
                importObject(person.getFName(), "FirstName"),
                importObject(person.getLName(), "LastName"),
                importObject(person.getAge(), "Age"),
                importObject(person.getMarried(), "Married"),
                importComplex("Address",
                    new IOID[]{
                        importObject(address.getCity(), "City"),
                        importObject(address.getStreet(), "Street"),
                        importObject(address.getZip(), "Zip")
                    })
            });
    }

    public IOID importObject(Object o, String name) {
        addJavaObject(o, name);
        return lastOID;
    }

    public IOID importComplex(String name, IOID[] ids) {
        addJavaObject(ids, name);
        return lastOID;
    }

    public void visit(Object o) {
        if (o instanceof Person) visitPerson((Person)o);
    }
}
