package edu.pjwstk.demo.visitor.helpers;

import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.result.StructResult;
import edu.pjwstk.jps.result.*;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//
// Zadaniem ArgumentResolver'a jest dostarczenie eleganckiej formy wybierania argumentow w metodach ConcreteASTVisitora
//  - Ukrywa szczegóły dotyczące walidacji typów
//  - Może rzucić wyjątek, jeśli typy się nie zgadzają
//
public final class ArgumentResolver {

    // Pobieranie argumentów połączone z walidacją
    //




    public static Arguments getArgumentsForJoin(IAbstractQueryResult left, IAbstractQueryResult right) {

        // mogą być dowolne argumenty, musi wyjść element albo kolekcja
        // nie mogą być dwie kolekcje

        if (left instanceof ICollectionResult && right instanceof ICollectionResult) {
            throwExceptionIfArgumentsConflict(Operator.JOIN,left, right);
        }

        return new Arguments(left, right);

        //
    }

    public static Arguments getArgumentsForComma(IAbstractQueryResult left, IAbstractQueryResult right) {

        // mogą być dowolne argumenty, musi wyjść struktura albo bag

        left = getStructOrBagForComma(left);
        right = getStructOrBagForComma(right);

        return new Arguments(left, right);
    }

    public static Arguments getArguments(Operator operator, IAbstractQueryResult argument)
            throws RuntimeException {

        boolean acceptReference = ArgumentTypeToOperatorMapper.checkReferenceAcceptance(operator);

        argument = getActualValue(argument, acceptReference);

        // walidacja
        throwExceptionIfNullArgument(operator, argument);
        throwExceptionIfArgumentNotSupported(operator, argument);

        return new Arguments(argument);
    }

    public static Arguments getArguments(Operator operator, IAbstractQueryResult left, IAbstractQueryResult right)
            throws RuntimeException {

        boolean acceptReference = ArgumentTypeToOperatorMapper.checkReferenceAcceptance(operator);

        left = getActualValue(left, acceptReference);
        right = getActualValue(right, acceptReference);

        // walidacja
        throwExceptionIfNullArgument(operator, left);
        throwExceptionIfNullArgument(operator, right);
        throwExceptionIfArgumentNotSupported(operator, left);
        throwExceptionIfArgumentNotSupported(operator, right);

        return new Arguments(left, right);
    }

    // Rzucenie wyjątku jeśli argument jest pusty
    //
    private static void throwExceptionIfNullArgument(Operator operator, IAbstractQueryResult argument)
            throws RuntimeException {
        if (argument == null){
            throw new RuntimeException("Argument of "+operator+" operator cannot be null.");
        }
    }

    // Rzucenie wyjątku jeśli typ argumentu jest niepoprawny
    //
    private static void throwExceptionIfArgumentNotSupported(Operator operator, IAbstractQueryResult argument)
            throws RuntimeException {

        ArgumentType type = getType(argument);

        if (!ArgumentTypeToOperatorMapper.isValid(operator, type)) {
            throw new RuntimeException("Argument " + type + " for " + operator + " operator not supported.");
        }
    }

    // Rzucenie wyjątku jeśli konflikt argumentów
    //
    private static void throwExceptionIfArgumentsConflict(Operator operator, IAbstractQueryResult argument1, IAbstractQueryResult argument2)
            throws RuntimeException {

        ArgumentType type1 = getType(argument1);
        ArgumentType type2 = getType(argument2);


        throw new RuntimeException("Argument " + type1 + " for " + operator + " is in conflict with second argument " + type2 + ".");

    }


    // Pobranie typu (rzutowanie na enum)
    //
    private static ArgumentType getType(IAbstractQueryResult argument)
            throws RuntimeException {
        if (argument instanceof IBooleanResult)     return ArgumentType.BOOLEAN;
        if (argument instanceof IStringResult)      return ArgumentType.STRING;
        if (argument instanceof IIntegerResult)     return ArgumentType.INTEGER;
        if (argument instanceof IDoubleResult)      return ArgumentType.DOUBLE;
        if (argument instanceof IBagResult)         return ArgumentType.BAG;
        if (argument instanceof ISequenceResult)    return ArgumentType.SEQUENCE;
        if (argument instanceof IStructResult)      return ArgumentType.STRUCT;
        if (argument instanceof IReferenceResult)   return ArgumentType.REFERENCE;
        if (argument instanceof IBinderResult)   return ArgumentType.BINDER;

        else throw new RuntimeException("Type of argument not supported.");
    }

    // Uzyskiwanie wartości - dereferencja lub pobranie elementu opakowanego w bag
    //
    private static IAbstractQueryResult getActualValue(IAbstractQueryResult argument, boolean acceptReferences) {
        if (argument instanceof IBagResult) {
            argument = tryFirstElementFromBag(argument);
        }
        if (!acceptReferences && argument instanceof IReferenceResult) {
            argument = dereference((IReferenceResult) argument);
        }
        return argument;
    }

    private static IAbstractQueryResult tryFirstElementFromBag(IAbstractQueryResult argument) {
        if (argument instanceof IBagResult) {
            Collection<ISingleResult> elements = ((IBagResult) argument).getElements();
            if (elements != null && elements.size() == 1) {
                argument = elements.iterator().next();
            }
        }
        return argument;
    }



    private static IAbstractQueryResult getStructOrBagForComma(IAbstractQueryResult argument) {

        IAbstractQueryResult result = argument;
        if (argument instanceof ISingleResult) {
            if (!(argument instanceof IStructResult))   {
                List <ISingleResult> struct = new ArrayList<>();
                struct.add((ISingleResult)argument);
                result = new StructResult(struct);
            }
        }

        // walidacja
        throwExceptionIfNullArgument(Operator.STRUCT, argument);
        throwExceptionIfArgumentNotSupported(Operator.STRUCT, argument);
        throwExceptionIfNullArgument(Operator.BAG, argument);
        throwExceptionIfArgumentNotSupported(Operator.BAG, argument);
        return result;
    }
    private static IAbstractQueryResult dereference(IReferenceResult ref) {
        return StoreRepository.getInstance().get(ref);
    }



}
