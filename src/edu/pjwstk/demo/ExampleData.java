package edu.pjwstk.demo;

import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Company;
import edu.pjwstk.demo.model.Employee;
import edu.pjwstk.demo.model.Person;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExampleData {
    private List<Person> persons = new ArrayList<Person>();
    private List<Company> companies = new ArrayList<Company>();

    public ExampleData() {
        initData();
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    private void initData() {
        persons.add(new Person("Marcin","Lewandowski",  20, true,  new Address("Gdańsk")));
        persons.add(new Person("Jan","Kowalski",        21, true,  new Address("Łódź")));
        persons.add(new Person("Piotr","Jankowski",     20, true,  new Address("Gdańsk")));
        persons.add(new Person("Krzysztof","Kowalski",  25, true,  new Address("Poznań")));
        persons.add(new Person("Tomasz","Nowak",        40, false, new Address("Poznań")));
        persons.add(new Person("Maria","Nowak",         31, false, new Address("Gdańsk")));
        persons.add(new Person("Katarzyna","Wieczorek", 27, false, new Address("Łódź")));
        persons.add(new Person("Agnieszka","Nowakowska",46, false, new Address("Warszawa")));
        persons.add(new Person("Wanda","Majewska",      51, true,  new Address("Warszawa")));
        persons.add(new Person("Agata","Olszewska",     56, true,  new Address("Warszawa")));

        List<Employee> emp1 = new ArrayList<Employee>();
        emp1.add(new Employee("Jan",2000.0));
        emp1.add(new Employee("Tom",3000.0));
        emp1.add(new Employee("Bob",3000.0));
        companies.add(new Company("Intel",emp1));

        List<Employee> emp2 = new ArrayList<Employee>();
        emp2.add(new Employee("Peter",1000.0));
        emp2.add(new Employee("Henry",2000.0));
        emp2.add(new Employee("Adam",3000.0));
        companies.add(new Company("Boeing",emp2));
    }
}