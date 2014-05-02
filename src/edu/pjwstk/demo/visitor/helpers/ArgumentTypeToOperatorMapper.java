package edu.pjwstk.demo.visitor.helpers;

import java.util.*;

public class ArgumentTypeToOperatorMapper {

    private static Map<Operator, List<ArgumentType>> ArgumentTypeMapping;

    private static void checkMappingInitialized() {
        if (ArgumentTypeMapping != null) return;

        ArgumentTypeMapping = new HashMap<>();

        // Operatory na kolekcjach
        AsCollectionOperator(Operator.AVG);
        AsCollectionOperator(Operator.SUM);
        AsCollectionOperator(Operator.COUNT);

        AsCollectionOperator(Operator.MINUS_SET);
        AsCollectionOperator(Operator.INTERSECT);
        AsCollectionOperator(Operator.UNION);
        AsCollectionOperator(Operator.JOIN);

        // Operatory dowolnych pojedynczych wartościach
        AsSingleValueOperator(Operator.EQUALS);
        AsSingleValueOperator(Operator.NOT_EQUALS);
        AsSingleValueOperator(Operator.PLUS);

        // Operatory logiczne
        AsBooleanOperator(Operator.AND);
        AsBooleanOperator(Operator.OR);
        AsBooleanOperator(Operator.NOT);

        // Operatory arytmetyczne
        AsNumberOperator(Operator.MINUS);
        AsNumberOperator(Operator.MULTIPLY);
        AsNumberOperator(Operator.DIVIDE);

        AsIntegerOperator(Operator.XOR);
        AsIntegerOperator(Operator.MODULO);

        // Operatory porównania
        AsNumberOperator(Operator.GREATER);
        AsNumberOperator(Operator.GREATER_OR_EQUAL);
        AsNumberOperator(Operator.LESS);
        AsNumberOperator(Operator.LESS_OR_EQUAL);


        // Dowolne argumenty przyjmują:
        //   MIN, MAX
        //   AS, GROUP_AS
        // ? EXISTS, UNIQUE
        //   BAG, STRUCT, COMMA
        //
        // Specialne traktowanie:
        //   IN
        //   DOT, WHERE
        //   FOR_ALL, FOR_ANY
        //   CLOSE_BY, ORDER_BY
    }

    public static boolean isValid(Operator operator, ArgumentType type){

        checkMappingInitialized();

        List<ArgumentType> validArguments = ArgumentTypeMapping.get(operator);

        return validArguments != null && validArguments.contains(type);
    }

    private static void map(Operator o, ArgumentType type) {
        List<ArgumentType> typeList = ArgumentTypeMapping.get(o);

        if (typeList != null) {
            typeList.add(type);
        } else {
            ArgumentTypeMapping.put(o, new ArrayList<>(Arrays.asList(new ArgumentType[]{type})));
        }
    }

    private static void AsCollectionOperator(Operator o) {
        map(o, ArgumentType.BAG);
        map(o, ArgumentType.SEQUENCE);
    }

    private static void AsSingleValueOperator(Operator o) {
        map(o, ArgumentType.INTEGER);
        map(o, ArgumentType.DOUBLE);
        map(o, ArgumentType.BOOLEAN);
        map(o, ArgumentType.STRING);
        map(o, ArgumentType.REFERENCE);
    }

    private static void AsBooleanOperator(Operator o) {
        map(o, ArgumentType.BOOLEAN);
    }

    private static void AsStringOperator(Operator o) {
        map(o, ArgumentType.STRING);
    }

    private static void AsIntegerOperator(Operator o) {
        map(o, ArgumentType.INTEGER);
    }

    private static void AsNumberOperator(Operator o) {
        map(o, ArgumentType.INTEGER);
        map(o, ArgumentType.DOUBLE);
    }
}