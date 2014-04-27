package edu.pjwstk.demo.model;

public class Employee {

    public String name;
    public Double salary;

    public Employee(String name, Double salary) {
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee[name="+ name +", salary="+ salary +"]";
    }
}
