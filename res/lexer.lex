package edu.pjwstk.demo.parser;

import java_cup.runtime.Symbol;
import static edu.pjwstk.demo.parser.Symbols.*;

%%
%{
	private Symbol createToken(int id) {
		return new Symbol(id, yyline, yycolumn);
	}
	private Symbol createToken(int id, Object o) {
		return new Symbol(id, yyline, yycolumn, o);
	}
%}

%public
%class Lexer
%cup
%line
%column
%char
%eofval{
	return createToken(EOF);
%eofval}

INTEGER = [0-9]+
BOOLEAN = true|false
IDENTIFIER = [_a-zA-Z][0-9a-zA-Z]*
DOUBLE = [0-9]+\.[0-9]+
STRING = [\"][^\"]*[\"]
CHAR = [\'][^\"][\']
LineTerminator = \r|\n|\r\n
WHITESPACE = {LineTerminator} | [ \t\f]

%%

<YYINITIAL> {
	"+"						{ return createToken(PLUS				); }
	"-"						{ return createToken(MINUS				); }
	"*"						{ return createToken(MULTIPLY			); }
	"/"						{ return createToken(DIVIDE				); }
	"%"						{ return createToken(MODULO				); }

	"xor"					{ return createToken(XOR				); }
	"not"					{ return createToken(NOT				); }
	"and"					{ return createToken(AND				); }
	"or"					{ return createToken(OR				    ); }

	"bag"					{ return createToken(BAG				); }
	"struct"				{ return createToken(STRUCT				); }
	"in"					{ return createToken(IN				    ); }
	"where"					{ return createToken(WHERE				); }

	"join"					{ return createToken(JOIN				); }
	"union"					{ return createToken(UNION				); }
	"intersect"				{ return createToken(INTERSECT			); }
	"minus"					{ return createToken(MINUS_SET			); }

	">"					    { return createToken(GREATER            ); }
	">="					{ return createToken(GREATER_EQUAL      ); }
	"<"					    { return createToken(LESS               ); }
	"<="					{ return createToken(LESS_EQUAL         ); }
	"=="					{ return createToken(EQUALS             ); }
	"!="					{ return createToken(NOT_EQUALS         ); }

	"max"					{ return createToken(MAX                ); }
	"min"					{ return createToken(MIN                ); }
	"sum"					{ return createToken(SUM                ); }
	"avg"					{ return createToken(AVG                ); }
	"count"					{ return createToken(COUNT              ); }
	"unique"                { return createToken(UNIQUE             ); }

	"."					    { return createToken(DOT                ); }
	","					    { return createToken(COMMA              ); }

	"("						{ return createToken(LEFT_ROUND_BRACKET	); }
	")"						{ return createToken(RIGHT_ROUND_BRACKET); }

	{WHITESPACE} { }
	{INTEGER} {
		int val;
		try {
			val = Integer.parseInt(yytext());
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return createToken(INTEGER_LITERAL, new Integer(val));
	}
	{DOUBLE} {
		double val;
		try {
			val = Double.parseDouble(yytext());
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return createToken(DOUBLE_LITERAL, new Double(val));
	}
	{BOOLEAN} {
		boolean val;
		try {
			val = Boolean.parseBoolean(yytext());
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return createToken(BOOLEAN_LITERAL, new Boolean(val));
	}
	{STRING} {
		String val;
		try {
			val = yytext();
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return createToken(STRING_LITERAL, new String(val.substring(1,val.length()-1)));
	}
	{IDENTIFIER} {
		String val;
		try {
			val = yytext();
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return createToken(IDENTIFIER, new String(val));
	}
}
