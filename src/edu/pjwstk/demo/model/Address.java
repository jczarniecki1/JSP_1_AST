package edu.pjwstk.demo.model;

public class Address {
    public String city;

    public Address(String city) {
        super();
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address[city="+city+"]";
    }
}
