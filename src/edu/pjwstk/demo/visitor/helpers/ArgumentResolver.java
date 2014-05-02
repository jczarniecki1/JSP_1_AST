package edu.pjwstk.demo.visitor.helpers;

import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.jps.result.*;

public final class ArgumentResolver {


    // Pobieranie argumentów połączone z walidacją
    //
    public static Arguments getArguments(Operator operator, IAbstractQueryResult argument)
            throws RuntimeException {

        if (argument instanceof IReferenceResult) {
            argument = dereference((IReferenceResult) argument);
        }
        throwExceptionIfNullArgument(operator, argument);
        throwExceptionIfArgumentNotSupported(operator, argument);

        return new Arguments(argument);
    }

    public static Arguments getArguments(Operator operator, IAbstractQueryResult left, IAbstractQueryResult right)
            throws RuntimeException {

        if (left instanceof IReferenceResult) {
            left = dereference((IReferenceResult) left);
        }
        if (right instanceof IReferenceResult) {
            right = dereference((IReferenceResult) right);
        }
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

        else throw new RuntimeException("Type of argument not supported.");
    }

    // Dereferencja
    //
    private static IAbstractQueryResult dereference(IReferenceResult ref) {
        return StoreRepository.getInstance().get(ref);
    }
}