package edu.pjwstk.demo.model;

public class Person{
    private String firstName;
    private String lastName;
    private Integer age;
    private boolean married;
    private Address address;

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

    public String getFName() {
        return firstName;
    }
    public void setFName(String name) {
        firstName = name;
    }
    public String getLName() {
        return lastName;
    }
    public void setLName(String name) {
        lastName = name;
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
}
