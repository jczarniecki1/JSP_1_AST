package edu.pjwstk.demo.model;

import java.util.List;

public class Company {
    List<Employee> employees;
    String name;

    public Company(String name, List<Employee> employees) {
        this.name = name;
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Company[name="+ name +", employees.size="+ employees.size() +"]";
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
