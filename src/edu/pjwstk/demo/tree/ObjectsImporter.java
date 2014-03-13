package edu.pjwstk.demo.tree;

import edu.pjwstk.demo.datastore.ISBAStore2;
import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;
import edu.pjwstk.jps.datastore.IOID;

import java.util.Collection;

public class ObjectsImporter implements IObjectsImporter {
    private ISBAStore2 store;

    public ObjectsImporter(ISBAStore2 store) {
        this.store = store;
    }

    public void IntoStore(Collection<? extends VisitableObject> collection) {
        for (VisitableObject object : collection){
            object.accept(this);
        }
    }

    @Override
    public void visitAddress(Address address) {
        importComplex("Address",
            new IOID[]{
                importObject(address.getCity(), "City"),
                importObject(address.getStreet(), "Street"),
                importObject(address.getZip(), "Zip")
            });
    }

    @Override
    public void visitPerson(Person person) {
        importComplex("Person",
            new IOID[]{
                importObject(person.getFName(), "FirstName"),
                importObject(person.getLName(), "LastName"),
                importObject(person.getAge(), "Age"),
                importObject(person.getMarried(), "Married")
            });
        person.getAddress().accept(this);

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
