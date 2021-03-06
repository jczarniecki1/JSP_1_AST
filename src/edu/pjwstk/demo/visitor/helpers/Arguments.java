package edu.pjwstk.demo.visitor.helpers;

import edu.pjwstk.demo.result.StructResult;
import edu.pjwstk.jps.result.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

//
// Dostarcza wartości i informacje o typie przechowywanych argumentów
//  - Ukrywa często powtarzające się rzutowanie na inne typy
//  - Oryginalne wartości o typie abstrakcyjnym nie są dostępne
//
public class Arguments {

    private IAbstractQueryResult first;
    private IAbstractQueryResult second;

    public boolean mixedTypes;
    public boolean isInteger;
    public boolean firstIsInteger;
    public boolean secondIsInteger;
    public boolean isDouble;
    public boolean firstIsDouble;
    public boolean secondIsDouble;
    public boolean isBoolean;
    public boolean firstIsBoolean;
    public boolean secondIsBoolean;
    public boolean isString;
    public boolean firstIsString;
    public boolean secondIsString;
    public boolean isCollection;
    public boolean firstIsCollection;
    public boolean secondIsCollection;
    public boolean isStruct;
    public boolean firstIsStruct;
    public boolean secondIsStruct;
    public boolean isSequence;
    public boolean firstIsSequence;
    public boolean secondIsSequence;

    //
    // Constructors
    //
    public Arguments(IAbstractQueryResult first) {

        this.first = first;

        if (first instanceof IIntegerResult) isInteger = true;
        else if (first instanceof IDoubleResult) isDouble = true;
        else if (first instanceof IBooleanResult) isBoolean = true;
        else if (first instanceof IStringResult) isString = true;
        else if (first instanceof ICollectionResult) isCollection = true;
        else if (first instanceof IStructResult) isStruct = true;

        if (first instanceof ISequenceResult) isSequence = true;
    }

    public Arguments(IAbstractQueryResult first, IAbstractQueryResult second) {

        this.first = first;
        this.second = second;

        if (first instanceof IIntegerResult) firstIsInteger = true;
        else if (first instanceof IDoubleResult) firstIsDouble = true;
        else if (first instanceof IBooleanResult) firstIsBoolean = true;
        else if (first instanceof IStringResult) firstIsString = true;
        else if (first instanceof ICollectionResult) firstIsCollection = true;
        else if (first instanceof IStructResult) firstIsStruct = true;

        if (first instanceof ISequenceResult) firstIsSequence = true;

        if (second instanceof IIntegerResult) secondIsInteger = true;
        else if (second instanceof IDoubleResult) secondIsDouble = true;
        else if (second instanceof IBooleanResult) secondIsBoolean = true;
        else if (second instanceof IStringResult) secondIsString = true;
        else if (second instanceof ICollectionResult) secondIsCollection = true;
        else if (second instanceof IStructResult) secondIsStruct = true;

        if (second instanceof ISequenceResult) secondIsSequence = true;

        mixedTypes = ! (
             (firstIsInteger && secondIsInteger)
          || (firstIsDouble  && secondIsDouble)
          || (firstIsBoolean && secondIsBoolean)
          || (firstIsString  && secondIsString)
          || (firstIsCollection  && secondIsCollection)
        );
    }

    //
    // Double argument
    //
    public Boolean firstBoolean(){
        return ((IBooleanResult)first).getValue();
    }
    public Boolean secondBoolean(){
        return ((IBooleanResult)second).getValue();
    }

    public Integer firstInteger(){
        return ((IIntegerResult)first).getValue();
    }
    public Integer secondInteger(){
        return ((IIntegerResult)second).getValue();
    }

    public Double firstDouble() {
        return ((IDoubleResult)first).getValue();
    }
    public Double secondDouble(){
        return ((IDoubleResult)second).getValue();
    }

    public String firstString() {
        return ((IStringResult)first).getValue();
    }
    public String secondString(){
        return ((IStringResult)second).getValue();
    }

    public Object firstValue() {
        if (first instanceof ISimpleResult) return ((ISimpleResult) first).getValue();
        else return first;
    }
    public Object secondValue() {
        if (second instanceof ISimpleResult) return ((ISimpleResult) second).getValue();
        else return second;
    }

    public double firstAsDouble() {
        if (first instanceof IIntegerResult) {
            return firstInteger().doubleValue();
        }
        else if (first instanceof IBooleanResult) {
            return firstBoolean() ? 1 : 0;
        }
        else {
            return firstDouble();
        }
    }
    public double secondAsDouble() {
        if (second instanceof IIntegerResult) {
            return secondInteger().doubleValue();
        }
        else if (second instanceof IBooleanResult) {
            return secondBoolean() ? 1 : 0;
        }
        else {
            return secondDouble();
        }
    }

    public int firstAsInteger() {
        if (first instanceof IBooleanResult) {
            return firstBoolean() ? 1 : 0;
        }
        else {
            return firstInteger();
        }
    }
    public int secondAsInteger() {
        if (second instanceof IBooleanResult) {
            return secondBoolean() ? 1 : 0;
        }
        else {
            return secondInteger();
        }
    }

    public IStructResult firstAsStruct() {
       List <ISingleResult> collection = new ArrayList<>();
       for (ISingleResult r: firstAsCollection())
           if (r instanceof IStructResult)
               collection.addAll(((IStructResult) r).elements());
           else
               collection.add(r);

       return new StructResult(collection);
    }

    public IStructResult secondAsStruct() {
        List <ISingleResult> collection = new ArrayList<>();
        for (ISingleResult r: secondAsCollection())
            if (r instanceof IStructResult)
                collection.addAll(((IStructResult) r).elements());
            else
                collection.add(r);

        return new StructResult(collection);
    }

    public String firstAsString() {
        if (first instanceof IStringResult) return ((IStringResult) first).getValue();
        else return first.toString();
    }
    public String secondAsString() {
        if (second instanceof IStringResult) return ((IStringResult) second).getValue();
        else return second.toString();
    }

    public boolean firstAsBoolean() {
        if (first instanceof IBooleanResult) return firstBoolean();
        else if (first instanceof IIntegerResult) return ((IIntegerResult) first).getValue() > 0;
        else return false;
    }
    public boolean secondAsBoolean() {
        if (second instanceof IBooleanResult) return secondBoolean();
        else if (second instanceof IIntegerResult) return ((IIntegerResult) second).getValue() > 0;
        else return false;
    }
    
    public Collection<ISingleResult> firstAsCollection() {
        return asCollection(first);
    }
    public Collection<ISingleResult> secondAsCollection() {
        return asCollection(second);
    }

    //
    // Single collection argument
    //
    public Collection<ISingleResult> getAsCollection() {
        return asCollection(first);
    }

    //
    // Single argument
    //
    public IAbstractQueryResult get() {
        return first;
    }
    public Boolean getBoolean(){
        return firstBoolean();
    }
    public Integer getInteger(){
        return firstInteger();
    }
    public Double getDouble(){
        return firstDouble();
    }
    public String getString(){
        return firstString();
    }

    public static Collection<ISingleResult> asCollection(IAbstractQueryResult element) {
        if (element instanceof ICollectionResult) {
            if (element instanceof IBagResult) return ((IBagResult)element).getElements();
            else return ((ISequenceResult)element).getElements();
        }
        else return new ArrayList<>(Arrays.asList((ISingleResult) element));
    }
}
