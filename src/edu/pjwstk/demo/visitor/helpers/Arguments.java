package edu.pjwstk.demo.visitor.helpers;

import edu.pjwstk.jps.result.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

    //
    // Constructors
    //
    public Arguments(IAbstractQueryResult first) {

        this.first = first;

        if (first instanceof IIntegerResult) isInteger = true;
        else if (first instanceof IDoubleResult) isDouble = true;
        else if (first instanceof IBooleanResult) isBoolean = true;
        else if (first instanceof IStringResult) isString = true;
    }

    public Arguments(IAbstractQueryResult first, IAbstractQueryResult second) {

        this.first = first;
        this.second = second;

        if (first instanceof IIntegerResult) firstIsInteger = true;
        else if (first instanceof IDoubleResult) firstIsDouble = true;
        else if (first instanceof IBooleanResult) firstIsBoolean = true;
        else if (first instanceof IStringResult) firstIsString = true;
        if (second instanceof IIntegerResult) secondIsInteger = true;
        else if (second instanceof IDoubleResult) secondIsDouble = true;
        else if (second instanceof IBooleanResult) secondIsBoolean = true;
        else if (second instanceof IStringResult) secondIsString = true;

        mixedTypes = ! (
             (firstIsInteger && secondIsInteger)
          || (firstIsDouble  && secondIsDouble)
          || (firstIsBoolean && secondIsBoolean)
          || (firstIsString  && secondIsString)
        );
    }


    //
    // Collection argument
    //
    public Collection<ISingleResult> getElements() {
        if (first instanceof IBagResult) return ((IBagResult)first).getElements();
        else return ((ISequenceResult)first).getElements();
    }

    //
    // Double collection argument
    //
    public Collection<ISingleResult> firstCollection() {
        if (first instanceof IBagResult) return ((IBagResult)first).getElements();
        else return ((ISequenceResult)first).getElements();
    }
    public Collection<ISingleResult> secondCollection() {
        if (second instanceof IBagResult) return ((IBagResult)second).getElements();
        else return ((ISequenceResult)second).getElements();
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

    public String firstAsString() {
        if (first instanceof IStringResult) return ((IStringResult) first).getValue();
        else return first.toString();
    }
    public String secondAsString() {
        if (second instanceof IStringResult) return ((IStringResult) second).getValue();
        else return second.toString();
    }
    
    public Collection<ISingleResult> firstAsCollection() {
        if (first instanceof ICollectionResult) return firstCollection();
        else return new ArrayList<>(Arrays.asList((ISingleResult) first));
    }
    public Collection<ISingleResult> secondAsCollection() {
        if (second instanceof ICollectionResult) return secondCollection();
        else return new ArrayList<>(Arrays.asList((ISingleResult)second));
    }

    //
    // Single argument
    //
    public Boolean getBoolean(){
        return ((IBooleanResult)first).getValue();
    }
    public Integer getInteger(){
        return ((IIntegerResult)first).getValue();
    }
    public Double getDouble(){
        return ((IDoubleResult)first).getValue();
    }
    public String getString(){
        return ((IStringResult)first).getValue();
    }
}