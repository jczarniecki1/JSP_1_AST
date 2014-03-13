package edu.pjwstk.demo.model;

import edu.pjwstk.demo.tree.IObjectsImporter;
import edu.pjwstk.demo.tree.VisitableObject;

public class Person implements VisitableObject{
    private String fName;
    private String lName;
    private Integer age;
    private boolean married;
    private Address address;

    public Person(String name, String name2, Integer age,
                  boolean married, Address address) {
        super();
        fName = name;
        lName = name2;
        this.age = age;
        this.married = married;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person[fName="+fName+", lName="+lName+", age="+age+
                ", married="+married+", address="+address+"]";
    }

    public String getFName() {
        return fName;
    }
    public void setFName(String name) {
        fName = name;
    }
    public String getLName() {
        return lName;
    }
    public void setLName(String name) {
        lName = name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public boolean getMarried() {
        return married;
    }
    public void setMarried(boolean married) {
        this.married = married;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public void accept(IObjectsImporter importer) {
        importer.visitPerson(this);
    }
}
