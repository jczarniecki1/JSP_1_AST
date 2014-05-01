package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.*;
import edu.pjwstk.demo.interpreter.envs.ENVS;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.result.*;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.datastore.IStringObject;
import edu.pjwstk.jps.interpreter.envs.IENVS;
import edu.pjwstk.jps.result.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class _4_ENVS_Test {
    private static QResStack qres = new QResStack();

    private static IENVS envs = new ENVS();
    private static ISBAStore store = SBAStore.getInstance();

    public static void main(String[] args){

        LoadData();
        envs.init(store.getEntryOID(), store);

        // 1. ((emp where married) union (emp where lName=”Nowak)).fName
        SolveQuery1();

        // 2. ((emp where exists address) where address.number>20).address.(street, city)
        SolveQuery2();

    }

    private static void LoadData() {
        store.loadXML("res/envs_data.xml");
    }

    private static void SolveQuery1() {

        IBagResult bagWithEmployeesFromENVS = envs.bind("emp");
        qres.push(bagWithEmployeesFromENVS);
        Collection<ISingleResult> employees = ((IBagResult)qres.pop()).getElements();

        List<ISingleResult> marriedEmployees = new ArrayList<>();
        for (ISingleResult empRef : employees){
            envs.push(envs.nested(empRef, store));

            IBagResult bagFromENVS = envs.bind("married");

            ISingleResult firstValue = bagFromENVS.getElements().iterator().next();
            BooleanObject married = (BooleanObject) (store.retrieve(((IReferenceResult) firstValue).getOIDValue()));
            qres.push(new BooleanResult(married.getValue()));

            IBooleanResult isMarried = (IBooleanResult)qres.pop();
            if (isMarried.getValue()){
                marriedEmployees.add(empRef);
            }

            envs.pop();
        }

        qres.push(new BagResult(marriedEmployees));

        IBagResult leftUnionCollection = (IBagResult)qres.pop();

        IBagResult bagWithEmployeesFromENVS2 = envs.bind("emp");
        qres.push(bagWithEmployeesFromENVS2);
        Collection<ISingleResult> employees2 = ((IBagResult)qres.pop()).getElements();

        List<ISingleResult> nowakEmployees = new ArrayList<>();
        for (ISingleResult empRef : employees2){
            envs.push(envs.nested(empRef, store));

            IBagResult bagFromENVS = envs.bind("lName");

            ISingleResult firstValue = bagFromENVS.getElements().iterator().next();
            StringObject lName = (StringObject) (store.retrieve(((IReferenceResult) firstValue).getOIDValue()));
            qres.push(new StringResult(lName.getValue()));

            ISimpleResult leftEqualsArgument = (ISimpleResult)qres.pop();

            IStringResult nowakName = new StringResult("Nowak");
            qres.push(nowakName);

            ISimpleResult rightEqualsArgument = (ISimpleResult)qres.pop();

            qres.push(new BooleanResult(leftEqualsArgument.getValue().equals(rightEqualsArgument.getValue())));

            IBooleanResult isNowak = (IBooleanResult)qres.pop();
            if (isNowak.getValue()){
                nowakEmployees.add(empRef);
            }

            envs.pop();
        }

        qres.push(new BagResult(nowakEmployees));

        IBagResult rightUnionCollection = (IBagResult)qres.pop();

        Collection<ISingleResult> unionResults = leftUnionCollection.getElements();
        unionResults.addAll(rightUnionCollection.getElements());

        qres.push(new BagResult(unionResults));

        IBagResult unionBag = (IBagResult)qres.pop();

        List<ISingleResult> fNames = new ArrayList<>();

        for (ISingleResult empRef : unionBag.getElements()){
            envs.push(envs.nested(empRef, store));

            IBagResult bagFromENVS = envs.bind("fName");

            ISingleResult firstValue = bagFromENVS.getElements().iterator().next();
            StringObject fName = (StringObject) (store.retrieve(((IReferenceResult) firstValue).getOIDValue()));
            fNames.add(new StringResult(fName.getValue()));

            envs.pop();
        }

        qres.push(new BagResult(fNames));

        Log("Result from Query1:");
        Log(qres.pop());
    }

    private static void SolveQuery2() {

        IBagResult bagWithEmployeesFromENVS = envs.bind("emp");
        qres.push(bagWithEmployeesFromENVS);
        Collection<ISingleResult> employees = ((IBagResult)qres.pop()).getElements();

        List<ISingleResult> employeesWithAddress = new ArrayList<>();
        for (ISingleResult empRef : employees){
            envs.push(envs.nested(empRef, store));

            IBagResult bagFromENVS = envs.bind("address");

            ISingleResult firstValue = bagFromENVS.getElements().iterator().next();
            ComplexObject address = (ComplexObject) (store.retrieve(((IReferenceResult) firstValue).getOIDValue()));
            qres.push(new ReferenceResult(address.getOID()));

            IReferenceResult addressRef = (IReferenceResult)qres.pop();
            qres.push(new BooleanResult(addressRef != null));

            IBooleanResult hasAddress = (IBooleanResult)qres.pop();
            if (hasAddress.getValue()){
                employeesWithAddress.add(empRef);
            }
            envs.pop();
        }

        qres.push(new BagResult(employeesWithAddress));

        IBagResult bagOfEmployeesWithAddress = (IBagResult)qres.pop();

        List<ISingleResult> employeesWithAddressNumberGraterThan20 = new ArrayList<>();
        for (ISingleResult empRef : bagOfEmployeesWithAddress.getElements()){
            envs.push(envs.nested(empRef, store));

            IBagResult bagFromENVS = envs.bind("address");

            ISingleResult firstValue = bagFromENVS.getElements().iterator().next();
            ComplexObject address = (ComplexObject) (store.retrieve(((IReferenceResult) firstValue).getOIDValue()));
            qres.push(new ReferenceResult(address.getOID()));

            IReferenceResult addressRef = (IReferenceResult)qres.pop();
            envs.push(envs.nested(addressRef, store));

            IBagResult numberFromENVS = envs.bind("number");

            ISingleResult firstNumberValue = numberFromENVS.getElements().iterator().next();
            IntegerObject number = (IntegerObject) (store.retrieve(((IReferenceResult) firstNumberValue).getOIDValue()));
            envs.pop();

            qres.push(new IntegerResult(number.getValue()));
            IIntegerResult leftGreaterThanArgument = (IIntegerResult)qres.pop();

            qres.push(new IntegerResult(20));
            IIntegerResult rightGreaterThanArgument = (IIntegerResult)qres.pop();


            qres.push(new BooleanResult(leftGreaterThanArgument.getValue() >  rightGreaterThanArgument.getValue()));

            IBooleanResult hasAddressNumberGreaterThan20 = (IBooleanResult)qres.pop();
            if (hasAddressNumberGreaterThan20.getValue()){
                employeesWithAddressNumberGraterThan20.add(empRef);
            }
            envs.pop();
        }

        qres.push(new BagResult(employeesWithAddressNumberGraterThan20));

        IBagResult bagOfEmployeesWithAddressNumberGraterThan20 = (IBagResult)qres.pop();

        List<ISingleResult> addresses = new ArrayList<>();
        for (ISingleResult empRef : bagOfEmployeesWithAddressNumberGraterThan20.getElements()) {
            envs.push(envs.nested(empRef, store));

            IBagResult bagFromENVS = envs.bind("address");

            ISingleResult firstValue = bagFromENVS.getElements().iterator().next();
            ComplexObject address = (ComplexObject) (store.retrieve(((IReferenceResult) firstValue).getOIDValue()));
            qres.push(new ReferenceResult(address.getOID()));

            IReferenceResult addressRef = (IReferenceResult)qres.pop();
            addresses.add(addressRef);

            envs.pop();
        }

        qres.push(new BagResult(addresses));
        IBagResult bagOfAddresses = (IBagResult)qres.pop();

        List<ISingleResult> structures = new ArrayList<>();
        for (ISingleResult addressRef : bagOfAddresses.getElements()) {

            List<ISingleResult> structValues = new ArrayList<>();
            envs.push(envs.nested(addressRef, store));

            IBagResult bagWithStreetFromENVS = envs.bind("street");
            ISingleResult streetValue = bagWithStreetFromENVS.getElements().iterator().next();
            IStringObject street = (IStringObject) (store.retrieve(((IReferenceResult) streetValue).getOIDValue()));
            qres.push(new StringResult(street.getValue()));
            structValues.add((ISingleResult)qres.pop());
            
            IBagResult bagWithCityFromENVS = envs.bind("city");
            ISingleResult cityValue = bagWithCityFromENVS.getElements().iterator().next();
            IStringObject city = (IStringObject) (store.retrieve(((IReferenceResult) cityValue).getOIDValue()));
            qres.push(new StringResult(city.getValue()));
            structValues.add((ISingleResult)qres.pop());

            structures.add(new StructResult(structValues));
            envs.pop();
        }

        qres.push(new BagResult(structures));

        Log("Result from Query2:");
        Log(qres.pop());
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
