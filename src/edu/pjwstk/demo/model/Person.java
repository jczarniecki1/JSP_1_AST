package edu.pjwstk.demo.model;

public class Person{
    public String firstName;
    public String lastName;
    public Integer age;
    public boolean married;
    public Address address;

    public Person(String name, String name2, Integer age,
                  boolean married, Address address) {
        super();
        firstName = name;
        lastName = name2;
        this.age = age;
        this.married = married;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person[firstName="+ firstName +", lastName="+ lastName +", age="+age+
                ", married="+married+", address="+address+"]";
    }
}
