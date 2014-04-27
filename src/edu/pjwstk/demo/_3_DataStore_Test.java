package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.model.CD;
import edu.pjwstk.demo.model.Transakcja;
import edu.pjwstk.demo.model.Wytwornia;
import edu.pjwstk.jps.datastore.ISBAStore;

import java.util.ArrayList;
import java.util.List;

public class _3_DataStore_Test {

    private static ISBAStore store;
    private static List<CD> cds;
    private static List<Wytwornia> wytwornie;


    public static void main(String[] args){

        Log("#############################################");
        Log("  TestXML\n");

        TestXML();

        Log("\n\n");

        Log("#############################################");
        Log("  TestJavaCollection\n");

        TestJavaCollection();
    }

    private static void TestXML() {
        store = new SBAStore();
        Log("Wczytywanie danych...");

        store.loadXML("res/baza.xml");

        Log("Wczytywanie zakończono.");
        Log("Zawartość bazy:\n");

        DatastorePrinter.PrintDatabase(store);
    }

    private static void createCollection() {
        Wytwornia wytwornia;

        cds = new ArrayList<>();
        wytwornie = new ArrayList<>();

        CD cd;
        wytwornia = new Wytwornia("Universal Music Group", "USA");
        wytwornie.add(wytwornia);
        cd = new CD(wytwornia,"Back To Black","Emy Winehouse",2007, 37.99,1);
        cd.addTransakcja(new Transakcja("05-01-2014",2,36.50));
        cd.addTransakcja(new Transakcja("07-01-2014",5,37.50));
        cd.addTransakcja(new Transakcja("08-02-2014", 4, 37.88));
        cds.add(cd);
        cd = new CD(wytwornia,"Brothers In Arms","Dire Straits",1996, 35.99,2);
        cd.addTransakcja(new Transakcja("05-02-2014",2,35.55));
        cd.addTransakcja(new Transakcja("07-03-2014",5,35.99));
        cds.add(cd);

        cd = new CD(wytwornia,"Symphonica","George Michael",2014, 36.99,3);
        cd.addTransakcja(new Transakcja("15-03-2014",2,36.99));
        cd.addTransakcja(new Transakcja("17-04-2014",5,35.99));
        cds.add(cd);

        wytwornia = new Wytwornia("Sony Music", "USA");
        wytwornie.add(wytwornia);
        cd = new CD(wytwornia,"The Best Of Sade","Sade",2000, 23.99,4);
        cd.addTransakcja(new Transakcja("15-02-2014", 5, 23.50));
        cd.addTransakcja(new Transakcja("17-03-2014",5,23.50));
        cd.addTransakcja(new Transakcja("18-04-2014", 3, 24.88));
        cds.add(cd);

        cd = new CD(wytwornia,"Ten","Pearl Jam",1992, 23.99,5);
        cd.addTransakcja(new Transakcja("11-02-2014",2,23.50));
        cd.addTransakcja(new Transakcja("12-03-2014",4,23.50));
        cd.addTransakcja(new Transakcja("18-04-2014",2,24.88));
        cds.add(cd);

        wytwornia = new Wytwornia("Warner Music Poland", "Polska");
        wytwornie.add(wytwornia);
        cd = new CD(wytwornia,"Confessions On A Dance Floor","Madonna",2005, 62.49,6);
        cd.addTransakcja(new Transakcja("11-02-2014",5,62.50));
        cd.addTransakcja(new Transakcja("13-03-2014",1,63.99));
        cd.addTransakcja(new Transakcja("18-04-2014",2,62.88));
        cds.add(cd);
    }

    private static void TestJavaCollection() {
        store = new SBAStore();
        Log("Wczytywanie danych...");

        createCollection();
        store.addJavaCollection(cds, "CD");

        Log("Wczytywanie zakończono.");
        Log("Zawartość bazy:\n");

        DatastorePrinter.PrintDatabase(store);
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
