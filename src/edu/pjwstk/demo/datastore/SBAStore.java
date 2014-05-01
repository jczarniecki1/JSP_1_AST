package edu.pjwstk.demo.datastore;

import edu.pjwstk.demo.common.Query;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/*
    Implementacja bazy danych
*/
public class SBAStore implements ISBAStore {

    protected Integer lastGeneratedId = 0;
    protected HashMap<IOID, ISBAObject> hash = new HashMap<>();
    private IOID entryOID;
    private IOID lastOID;

    public static ISBAStore getClearInstance() {
        SBAStore store = (SBAStore)getInstance();
        store.clearDatabase();
        return store;
    }

    @Override
    public IOID generateUniqueOID() {
        lastGeneratedId += 1;
        lastOID = new OID(lastGeneratedId);
        return lastOID;
    }

    @Override
    public ISBAObject retrieve(IOID oid) {
        return hash.get(oid);
    }

    @Override
    public IOID getEntryOID() {
        if (entryOID == null){
            getRootObject();
        }
        return entryOID;
    }

    //
    // Wczytywanie do bazy z plików XML
    // TODO: Upewnić się, czy próba wczytania ma czyścić poprzednią zawartość
    // ( i jeśli nie, to czym zastąpić blok finally{..} ? )
    //
    @Override
    public void loadXML(String filePath) {
        boolean finishedSuccessfully = false;
        clearDatabase();

        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File(filePath));

            Node root = doc.getDocumentElement();
            root.normalize();

            loadNode(root, true);

            finishedSuccessfully = true;

        } catch (SAXParseException err) {
            System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
            System.out.println(" " + err.getMessage ());
        } catch (SAXException e) {
            Exception x = e.getException ();
            ((x == null) ? e : x).printStackTrace ();
        } catch (Throwable t) {
            t.printStackTrace ();
        }
        finally {
            if (! finishedSuccessfully){
                clearDatabase();
            }
        }

    }

    //
    // Czyszczenie bazy danych - ustawia nowe "entry"
    //
    private void clearDatabase() {
        hash.clear();
        lastGeneratedId = 0;
        entryOID = null;
        getRootObject();
    }

    @Override
    public void addJavaObject(Object o, String name) {
        IOID id = generateUniqueOID();
        if (o instanceof String)  hash.put(id, new StringObject (id, name, (String) o));
        else if (o instanceof Integer) hash.put(id,new IntegerObject(id, name, (Integer)o));
        else if (o instanceof Double)  hash.put(id,new DoubleObject (id, name, (Double) o));
        else if (o instanceof Boolean) hash.put(id,new BooleanObject(id, name, (Boolean)o));
        else if (o instanceof IOID[])  hash.put(id,new ComplexObject(id, name, Arrays.asList((IOID[]) o)));
        else addOtherType(o, name);
    }

    @Override
    public void addJavaCollection(Collection collection, String name) {
        ComplexObject root = getRootObject();

        List<IOID> childrenIds = Query.select(collection,x -> {
            addJavaObject(x, name);
            return lastOID;
        });

        root.getChildOIDs().addAll(childrenIds);
    }

    //
    // Pobranie tzw. "entry" z bazy
    //
    // Uwaga: Jeśli to pierwsze pobranie, to dodajemy puste "entry" do bazy
    // Dlatego należy wywołać tą funkcję na samym początku ładowania jakichkolwiek danych
    //
    private ComplexObject getRootObject() {
        if (entryOID == null) {
            entryOID = generateUniqueOID();
            hash.put(entryOID, new ComplexObject(entryOID, "entry", new ArrayList<>()));
        }
        return  ((ComplexObject)retrieve(entryOID));
    }

    public IOID importObject(Object o, String name) {
        addJavaObject(o, name);
        return lastOID;
    }

    //
    // Dodawanie obiektów Javy
    // - rekurencja nie jest oczywista: do visit() trafiamy, jeśli argumentem addJavaObject() nie jest typ prosty.
    //   Pobieramy listę pól publicznych i iterujemy po nich dodając kolejne obiekty do bazy.
    //   Warunkiem wejścia "głębiej" w rekurencji jest posiadanie w obiekcie pola, które nie jest typem prostym.
    //   Na koniec dodajemy sam obiekt jako ComplexObject - czyli lista identyfikatorów obiektów wewnętrznych.
    //
    // TODO: To sie przestało robić czytelne. Trzeba rozdzielić metodę na kilka mniejszych.
    //
    public void addOtherType(Object o, String name) {
        Class<?> type = o.getClass();

        try {
            Field[] publicFields = type.getFields();
            List<IOID> ids = Arrays.asList(publicFields)
                .stream()
                .flatMap(x -> {
                    try {
                        String fieldName = x.getName();
                        Object fieldValue = x.get(o);
                        if (fieldValue == null) return null;

                        if (fieldValue instanceof Collection) {
                            List<IOID> innerIds = new ArrayList<>();
                            Collection collection = (Collection) fieldValue;
                            for (Object y : collection) {
                                innerIds.add(importObject(y, fieldName));
                            }
                            return innerIds.stream();
                        }
                        else if (fieldValue.getClass().isArray()) {
                            int arraySize = Array.getLength(fieldValue);
                            List<IOID> innerIds = new ArrayList<>();
                            for (int i = 0; i < arraySize; i++) {
                                Object arrayField = Array.get(fieldValue, i);
                                innerIds.add(importObject(arrayField, fieldName));
                            }
                            return innerIds.stream();
                        } else {
                            ArrayList<IOID> list = new ArrayList<>();
                            list.add(importObject(fieldValue, fieldName));
                            return list.stream();
                        }
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                })
                .filter(x -> x != null)
                .collect(Collectors.toList());
            IOID[] idsArray = ids.toArray(new IOID[ids.size()]);
            addJavaObject(idsArray, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    // Wczytywanie węzłów w XML do bazy
    //
    // - Jeśli wczytujemy pierwszy element (root),
    //   to zawsze traktujemy go jako ComplexObject
    //
    //   Inaczej:
    //
    //   - Jeśli element na podelementy (czyli spełnia warunek "hasChildren(...)"),
    //     to wczytaj jako ComplexObject
    //
    //     Inaczej:
    //     - dodaj jako zwyły element
    //       (roczaj rozpoznajemy parsując tekst wewnątrz elementu)
    //
    private IOID loadNode(Node node, boolean isRoot) {

        if (isRoot){

            ComplexObject root = getRootObject();
            NodeList childNodes = node.getChildNodes();
            List<IOID> ids = loadNodeChildren(childNodes);

            root.getChildOIDs().addAll(ids);

            return entryOID;
        }
        else {
            String name = node.getNodeName();

            if (hasChildren(node)) {
                List<IOID> ids = loadNodeChildren(node.getChildNodes());
                addJavaObject(ids.toArray(new IOID[ids.size()]), name);
            }
            else {
                String content = node.getTextContent();
                if (tryParseInt(content)) addJavaObject(Integer.parseInt(content, 10), name);
                else if (tryParseDouble(content)) addJavaObject(Double.parseDouble(content), name);
                else if (tryParseBoolean(content)) addJavaObject(Boolean.parseBoolean(content), name);
                else addJavaObject(content, name);
            }

            return lastOID;
        }

    }

    //
    // Sprawdzanie, czy węzeł ma dzieci
    // Jeśli natkniemy się na piewszy poprawny element, to od razu wychodzimy z funkcji
    //
    // - Uwaga: niektóre wczytane dzieci to komentarze albo tekst pomiędzy elementami
    //   Takie elementy odrzucamy
    //
    private boolean hasChildren(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i= 0; i < childNodes.getLength(); i++){
            if (isValidElement(childNodes.item(i))) {
                return true;
            }
        }
        return false;
    }

    //
    // Wczytywanie listy węzłów do bazy
    //
    // - Uwaga: j.w.
    //
    private List<IOID> loadNodeChildren(NodeList childNodes) {
        List<IOID> ids = new ArrayList<>();

        for(int i=0; i< childNodes.getLength(); i++) {

            Node item = childNodes.item(i);
            if (isValidElement(item)) {
                ids.add(loadNode(item, false));
            }
        }
        return ids;
    }

    //
    // Odrzucamy elementy, które nie są poprawnymi dziećmi węzłów
    //
    private boolean isValidElement(Node item) {
        return ! item.getNodeName().startsWith("#");
    }

    boolean tryParseInt(String value)
    {
         try
         {
             Integer.parseInt(value, 10);
             return true;
          } catch(NumberFormatException nfe)
          {
              return false;
          }
    }

    boolean tryParseDouble(String value)
    {
         try
         {
             Double.parseDouble(value);
             return true;
          } catch(NumberFormatException nfe)
          {
              return false;
          }
    }

    boolean tryParseBoolean(String value)
    {
        return  (value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")));
    }

    private static ISBAStore instance = null;
    public static ISBAStore getInstance() {
        if (instance == null){
            instance = new SBAStore();
        }
        return instance;
    }

    public void reset() {
        clearDatabase();
    }
}
