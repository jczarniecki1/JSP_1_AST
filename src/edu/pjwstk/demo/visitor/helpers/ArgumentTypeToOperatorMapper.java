package edu.pjwstk.demo.visitor.helpers;

import java.util.*;

//
// Mapper ma za zadanie powiązać operatory z typami argumentów
//  - jeśli operator nie jest wymieniony, to akceptowany jest dowolny rodzaj argumentów
//  - mapowanie powinno być przede wszystkim czytelne
//
public class ArgumentTypeToOperatorMapper {

    private static Map<Operator, List<ArgumentType>> ArgumentTypeMapping;
    public static Collection<Operator> operatorsAcceptingReferences;

    private static void checkMappingInitialized() {
        if (ArgumentTypeMapping != null) return;

        ArgumentTypeMapping = new HashMap<>();

        // Operatory dowolnych pojedynczych wartościach
        AsSingleValueOperator(Operator.EQUALS);
        AsSingleValueOperator(Operator.NOT_EQUALS);
        AsSingleValueOperator(Operator.PLUS);

        // Operatory logiczne
        AsBooleanOperator(Operator.AND);
        AsBooleanOperator(Operator.OR);
        AsBooleanOperator(Operator.NOT);
        AsBooleanOperator(Operator.XOR);

        // Operatory arytmetyczne
        AsNumberOperator(Operator.MINUS);
        AsNumberOperator(Operator.MULTIPLY);
        AsNumberOperator(Operator.DIVIDE);
        AsNumberOperator(Operator.MODULO);

        // Operatory porównania
        AsNumberOperator(Operator.GREATER);
        AsNumberOperator(Operator.GREATER_OR_EQUAL);
        AsNumberOperator(Operator.LESS);
        AsNumberOperator(Operator.LESS_OR_EQUAL);

        // Dowolne argumenty:
        //   IN
        //   MIN, MAX
        //   AS, GROUP_AS
        //   EXISTS, UNIQUE
        //   SUM, AVG, COUNT
        //   BAG, STRUCT, COMMA
        //   UNION, INTERSECT, MINUS_SET, JOIN
        //
        // Specialne traktowanie:
        //   DOT, WHERE
        //   FOR_ALL, FOR_ANY
        //
        // Inne:
        //   CLOSE_BY, ORDER_BY

        operatorsAcceptingReferences = Arrays.asList(
            Operator.MINUS_SET,
            Operator.INTERSECT,
            Operator.IN
        );
    }

    public static boolean isValid(Operator operator, ArgumentType type){

        checkMappingInitialized();

        List<ArgumentType> validArguments = ArgumentTypeMapping.get(operator);

        return validArguments == null || validArguments.contains(type);
    }

    public static boolean checkReferenceAcceptance(Operator operator) {

        checkMappingInitialized();

        return operatorsAcceptingReferences.stream().anyMatch(operator::equals);
    }

    private static void map(Operator o, ArgumentType type) {
        List<ArgumentType> typeList = ArgumentTypeMapping.get(o);

        if (typeList != null) {
            typeList.add(type);
        } else {
            ArgumentTypeMapping.put(o, new ArrayList<>(Arrays.asList(new ArgumentType[]{type})));
        }
    }

    private static void AsSingleValueOperator(Operator o) {
        map(o, ArgumentType.INTEGER);
        map(o, ArgumentType.DOUBLE);
        map(o, ArgumentType.BOOLEAN);
        map(o, ArgumentType.STRING);
    }

    private static void AsBooleanOperator(Operator o) {
        map(o, ArgumentType.BOOLEAN);
        map(o, ArgumentType.INTEGER);
    }

    private static void AsNumberOperator(Operator o) {
        map(o, ArgumentType.INTEGER);
        map(o, ArgumentType.DOUBLE);
    }
}
