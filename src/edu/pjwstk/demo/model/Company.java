package edu.pjwstk.demo.model;

import java.util.List;

public class Company {
    List<Employee> employees;

    public Company(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
