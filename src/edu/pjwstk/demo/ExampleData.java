package edu.pjwstk.demo;

import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Person;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExampleData {
    private List<Person> persons = new ArrayList<>();

    public ExampleData() {
        initData();
    }

    public List<Person> getPersons() {
        return persons;
    }

    private void initData() {
        List<String> authors = new ArrayList<>();
        authors.add("A. Fredro");
        authors.add("J. Kowalski");
        authors.add("K. Marks");

        List<String> cities = new ArrayList<>();
        cities.add("Łódź");
        cities.add("Warszawa");
        cities.add("Poznań");
        cities.add("Wrocław");
        cities.add("Kraków");
        cities.add("Gdańsk");
        cities.add("Szczecin");
        cities.add("Rzeszów");
        cities.add("Katowice");

        List<String> streets = new ArrayList<>();
        streets.add("5'th avenue");
        streets.add("S. La Salle St.");
        streets.add("South Cass Avenue");
        streets.add("Park Avenue");
        streets.add("West Avenue");
        streets.add("North Avenue");

        List<String> femaleLNames = new ArrayList<>();
        femaleLNames.add("NOWAK");
        femaleLNames.add("WIECZOREK");
        femaleLNames.add("NOWAKOWSKA");
        femaleLNames.add("MAJEWSKA");
        femaleLNames.add("OLSZEWSKA");

        List<String> femaleFNames = new ArrayList<>();
        femaleFNames.add("MARIA");
        femaleFNames.add("KATARZYNA");
        femaleFNames.add("AGNIESZKA");
        femaleFNames.add("WANDA");
        femaleFNames.add("AGATA");

        List<String> maleLNames = new ArrayList<String>();
        maleLNames.add("NOWAK");
        maleLNames.add("KOWALSKI");
        maleLNames.add("LEWANDOWSKI");
        maleLNames.add("JANKOWSKI");
        maleLNames.add("PIOTROWSKI");

        List<String> maleFNames = new ArrayList<String>();
        maleFNames.add("JAN");
        maleFNames.add("PIOTR");
        maleFNames.add("KRZYSZTOF");
        maleFNames.add("TOMASZ");
        maleFNames.add("MARCIN");
        maleFNames.add("MAREK");

        for(String c : cities) {
            for(int i=0; i<5; i++) {
                persons.add(
                        new Person(random(maleFNames), random(maleLNames),
                                randomInt(2, 3), randomBoolean(),
                                new Address(c, random(streets), randomZip())
                        )
                );
                persons.add(
                        new Person(random(femaleFNames), random(femaleLNames),
                                randomInt(2, 3), randomBoolean(),
                                new Address(c, random(streets), randomZip())
                        )
                );
            }
        }
    }

    private <T> T random(List<T> col) {
        int randomIndex = (int)(Math.random()*col.size());
        return col.get(randomIndex);
    }

    private String randomZip() {
        int r = (int)(Math.random() * 100000);
        DecimalFormat df = new DecimalFormat("00000");
        StringBuilder sb = new StringBuilder(df.format(r));
        sb.insert(2, "-");
        return sb.toString();
    }

    private int randomInt(int min, int max) {
        return (int)(Math.random() * (max-min+1)) + min;
    }

    private boolean randomBoolean() {
        int r = (int)(Math.random() * 2);
        return r > 0 ? true : false;
    }
}