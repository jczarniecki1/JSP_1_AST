package edu.pjwstk.demo.visitor;

import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;
import edu.pjwstk.jps.datastore.IOID;

public interface IObjectsImporter {
    public IOID visitAddress(Address address);
    public IOID visitPerson(Person person);
}
