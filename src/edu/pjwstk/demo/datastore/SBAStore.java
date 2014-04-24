package edu.pjwstk.demo.datastore;

import edu.pjwstk.demo.common.Query;
import edu.pjwstk.demo.model.Address;
import edu.pjwstk.demo.model.Company;
import edu.pjwstk.demo.model.Employee;
import edu.pjwstk.demo.model.Person;
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
import java.util.*;

/*
    Implementacja bazy danych

    TODO:
      - zastąpienie wczytywania obiektów typu Person
        uniwersalnym rozwiazaniem opartym na refleksji (CW3-DataStore.pdf)
*/
public class SBAStore implements ISBAStore {

    protected Integer lastGeneratedId = 0;
    protected HashMap<IOID, ISBAObject> hash = new HashMap<>();
    private IOID entryOID;
    private IOID lastOID;

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
        else visit(o);
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

    // Do usunięcia
    @Deprecated
    public IOID visitPerson(Person person) {
        Address address = person.getAddress();
        return importComplex("Person",
            new IOID[]{
                importObject(person.getFName(), "FirstName"),
                importObject(person.getLName(), "LastName"),
                importObject(person.getAge(), "Age"),
                importObject(person.getMarried(), "Married"),
                importComplex("Address",
                    new IOID[]{
                        importObject(address.getCity(), "City")
                    })
            });
    }
    // Do usunięcia
    @Deprecated
    public IOID visitCompany(Company company) {

        List<IOID> innerIds = Query.select(company.getEmployees(), x -> visitEmployee(x));

        innerIds.add(importObject(company.getName(), "Name"));

        return importComplex("Company", innerIds.toArray(new IOID[innerIds.size()]));
    }

    // Do usunięcia
    @Deprecated
    public IOID visitEmployee(Employee employee) {
        return importComplex("Employee",
            new IOID[]{
                importObject(employee.getName(), "Name"),
                importObject(employee.getSalary(), "Salary")
            });
    }

    public IOID importObject(Object o, String name) {
        addJavaObject(o, name);
        return lastOID;
    }

    public IOID importComplex(String name, IOID[] ids) {
        addJavaObject(ids, name);
        return lastOID;
    }

    // Do usunięcia
    @Deprecated
    public void visit(Object o) {
        if (o instanceof Person) visitPerson((Person)o);
        else if (o instanceof Company) visitCompany((Company)o);
        else if (o instanceof Employee) visitEmployee((Employee)o);
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
}
