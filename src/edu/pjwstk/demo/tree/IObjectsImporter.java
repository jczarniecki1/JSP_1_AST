package edu.pjwstk.demo.tree;

import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;

public interface IObjectsImporter {
    public void visitAddress(Address address);
    public void visitPerson(Person person);
}
