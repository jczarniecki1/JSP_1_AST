package edu.pjwstk.demo.visitor.helpers;

public enum Operator {
    AS, GROUP_AS,
    // binary
    AND, CLOSE_BY, COMMA, DIVIDE, DOT, EQUALS, FOR_ALL, FOR_ANY, GREATER_OR_EQUAL, GREATER, IN, INTERSECT, JOIN, LESS_OR_EQUAL, LESS, MINUS, MINUS_SET, MODULO, MULTIPLY, NOT_EQUALS, ORDER_BY, OR, PLUS, UNION, WHERE, XOR,
    // unary
    AVG, BAG, COUNT, EXISTS, MAX, MIN, NOT, STRUCT, SUM, UNIQUE
}