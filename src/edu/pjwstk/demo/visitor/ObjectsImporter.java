package edu.pjwstk.demo.visitor;

import edu.pjwstk.demo.datastore.ISBAStoreJavaObjects;
import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;
import edu.pjwstk.jps.datastore.IOID;

import java.util.Collection;

public class ObjectsImporter implements IObjectsImporter {
    private ISBAStoreJavaObjects store;

    public ObjectsImporter(ISBAStoreJavaObjects store) {
        this.store = store;
    }

    public void IntoStore(Collection<? extends VisitableObject> collection) {
        for (VisitableObject object : collection){
            object.accept(this);
        }
    }

    @Override
    public IOID visitAddress(Address address) {
        return importComplex("Address",
            new IOID[]{
                importObject(address.getCity(), "City"),
                importObject(address.getStreet(), "Street"),
                importObject(address.getZip(), "Zip")
            });
    }

    @Override
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

    public IOID importComplex(String name, IOID[] ids) {
        store.addJavaObject(ids, name);
        return store.getLastOID();
    }

    public IOID importObject(Object value, String name) {
        store.addJavaObject(value, name);
        return store.getLastOID();
    }
}
