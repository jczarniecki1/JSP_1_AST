package edu.pjwstk.demo.model;

import java.util.List;

public class Company {
    public Employee[] employees;
    public String name;

    public Company(String name, List<Employee> employees) {
        this.name = name;
        this.employees = employees.toArray(new Employee[employees.size()]);
    }

    @Override
    public String toString() {
        return "Company[name="+ name +", employees.size="+ employees.length +"]";
    }
}
