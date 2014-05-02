package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.ComplexObject;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;

import java.util.List;

//
// Wypisuje zawartość bazy
//
public class DatastorePrinter {

    public static void PrintDatabase(ISBAStore store) {
        printObject(store, store.retrieve(store.getEntryOID()));
    }

    private static void printObject(ISBAStore store, ISBAObject o) {

        Log(o.toString());

        if (o instanceof ComplexObject){
            List<IOID> childIds = ((ComplexObject) o).getChildOIDs();

            for (IOID id : childIds) {
               printObject(store, store.retrieve(id));
            }
        }
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
