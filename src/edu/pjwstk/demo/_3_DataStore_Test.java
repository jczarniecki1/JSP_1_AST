package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.jps.datastore.ISBAStore;

public class _3_DataStore_Test {

    private static ISBAStore store = new SBAStore();

    public static void main(String[] args){

        Log("Wczytywanie danych...");

        store.loadXML("res/baza.xml");

        Log("Wczytywanie zakończono.");
        Log("\n");
        Log("Zawartość bazy:");
        Log("\n");

        DatastorePrinter.PrintDatabase(store);
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
