package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.ComplexObject;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;

import java.util.List;

public class _3_DataStore_Test {

    private static SBAStore store;

    public static void main(String[] args){

        store = new SBAStore();


//        ExampleData data = new ExampleData();
//        store.addJavaCollection(data.getPersons(), "Person");
//        store.addJavaCollection(data.getCompanies(), "Company");
//
        Log("Wczytywanie danych,,,");

        store.loadXML("res/example.xml");

        Log("\n");

        PrintDatabase();
    }

    private static void PrintDatabase() {
        printObject(store.retrieve(store.getEntryOID()));
    }

    private static void printObject(ISBAObject o) {

        Log(o.toString());

        if (o instanceof ComplexObject){
            List<IOID> childIds = ((ComplexObject) o).getChildOIDs();

            for (IOID id : childIds) {
               printObject(store.retrieve(id));
            }
        }

    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
