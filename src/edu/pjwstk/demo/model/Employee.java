package edu.pjwstk.demo.model;

public class Employee {

    private String name;
    private Double salary;

    public Employee(String name, Double salary) {
        this.name = name;
        this.salary = salary;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setSalary(Double salary) {
        this.salary = salary;
    }
    public Double getSalary() {
        return salary;
    }
}
