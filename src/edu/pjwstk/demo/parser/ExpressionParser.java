
//----------------------------------------------------
// The following code was generated by CUP v0.11a beta 20060608
// Thu May 01 20:47:06 CEST 2014
//----------------------------------------------------

package edu.pjwstk.demo.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java_cup.runtime.DefaultSymbolFactory;
import java_cup.runtime.Symbol;
import edu.pjwstk.demo.expression.*;
import edu.pjwstk.demo.expression.terminal.*;
import edu.pjwstk.demo.expression.binary.*;

/** CUP v0.11a beta 20060608 generated parser.
  * @version Thu May 01 20:47:06 CEST 2014
  */
public class ExpressionParser extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public ExpressionParser() {super();}

  /** Constructor which sets the default scanner. */
  public ExpressionParser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public ExpressionParser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\016\000\002\002\004\000\002\002\003\000\002\003" +
    "\005\000\002\003\005\000\002\003\005\000\002\003\005" +
    "\000\002\003\005\000\002\003\005\000\002\003\003\000" +
    "\002\003\003\000\002\003\003\000\002\003\003\000\002" +
    "\003\003\000\002\003\005" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\030\000\016\004\006\005\005\006\007\007\013\010" +
    "\010\017\011\001\002\000\004\002\032\001\002\000\022" +
    "\002\ufff8\011\ufff8\012\ufff8\013\ufff8\014\ufff8\015\ufff8\016" +
    "\ufff8\020\ufff8\001\002\000\022\002\ufff9\011\ufff9\012\ufff9" +
    "\013\ufff9\014\ufff9\015\ufff9\016\ufff9\020\ufff9\001\002\000" +
    "\022\002\ufff7\011\ufff7\012\ufff7\013\ufff7\014\ufff7\015\ufff7" +
    "\016\ufff7\020\ufff7\001\002\000\022\002\ufff5\011\ufff5\012" +
    "\ufff5\013\ufff5\014\ufff5\015\ufff5\016\ufff5\020\ufff5\001\002" +
    "\000\016\004\006\005\005\006\007\007\013\010\010\017" +
    "\011\001\002\000\020\002\000\011\015\012\016\013\020" +
    "\014\017\015\014\016\021\001\002\000\022\002\ufff6\011" +
    "\ufff6\012\ufff6\013\ufff6\014\ufff6\015\ufff6\016\ufff6\020\ufff6" +
    "\001\002\000\016\004\006\005\005\006\007\007\013\010" +
    "\010\017\011\001\002\000\016\004\006\005\005\006\007" +
    "\007\013\010\010\017\011\001\002\000\016\004\006\005" +
    "\005\006\007\007\013\010\010\017\011\001\002\000\016" +
    "\004\006\005\005\006\007\007\013\010\010\017\011\001" +
    "\002\000\016\004\006\005\005\006\007\007\013\010\010" +
    "\017\011\001\002\000\016\004\006\005\005\006\007\007" +
    "\013\010\010\017\011\001\002\000\022\002\ufffa\011\015" +
    "\012\016\013\020\014\017\015\014\016\ufffa\020\ufffa\001" +
    "\002\000\022\002\ufffd\011\ufffd\012\ufffd\013\ufffd\014\ufffd" +
    "\015\ufffd\016\ufffd\020\ufffd\001\002\000\022\002\ufffc\011" +
    "\ufffc\012\ufffc\013\ufffc\014\ufffc\015\ufffc\016\ufffc\020\ufffc" +
    "\001\002\000\022\002\ufffe\011\ufffe\012\ufffe\013\020\014" +
    "\017\015\ufffe\016\ufffe\020\ufffe\001\002\000\022\002\uffff" +
    "\011\uffff\012\uffff\013\020\014\017\015\uffff\016\uffff\020" +
    "\uffff\001\002\000\022\002\ufffb\011\015\012\016\013\020" +
    "\014\017\015\ufffb\016\ufffb\020\ufffb\001\002\000\020\011" +
    "\015\012\016\013\020\014\017\015\014\016\021\020\031" +
    "\001\002\000\022\002\ufff4\011\ufff4\012\ufff4\013\ufff4\014" +
    "\ufff4\015\ufff4\016\ufff4\020\ufff4\001\002\000\004\002\001" +
    "\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\030\000\006\002\003\003\011\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\004\003\027\001\001\000\002\001" +
    "\001\000\002\001\001\000\004\003\026\001\001\000\004" +
    "\003\025\001\001\000\004\003\024\001\001\000\004\003" +
    "\023\001\001\000\004\003\022\001\001\000\004\003\021" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$ExpressionParser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$ExpressionParser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$ExpressionParser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 0;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


  /** User initialization code. */
  public void user_init() throws java.lang.Exception
    {
 	    lexer = new Lexer(new StringReader(expressionText)); 
    }

  /** Scan to get the next Symbol. */
  public java_cup.runtime.Symbol scan()
    throws java.lang.Exception
    {
		return lexer.next_token(); 
    }


	public Lexer lexer;
	private String expressionText;
	public Expression RESULT;

	public ExpressionParser(String expressionText) {
		this.symbolFactory = new DefaultSymbolFactory();
		this.expressionText = expressionText;
	}

	void setResult(Expression expression) {
		this.RESULT = expression;
	}


}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$ExpressionParser$actions {
  private final ExpressionParser parser;

  /** Constructor */
  CUP$ExpressionParser$actions(ExpressionParser parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$ExpressionParser$do_action(
    int                        CUP$ExpressionParser$act_num,
    java_cup.runtime.lr_parser CUP$ExpressionParser$parser,
    java.util.Stack            CUP$ExpressionParser$stack,
    int                        CUP$ExpressionParser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$ExpressionParser$result;

      /* select the action based on the action number */
      switch (CUP$ExpressionParser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // expr ::= LEFT_ROUND_BRACKET expr RIGHT_ROUND_BRACKET 
            {
              Expression RESULT =null;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).right;
		String o = (String)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).value;
		int e1left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).right;
		Expression e1 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).value;
		 RESULT = e1; 
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // expr ::= IDENTIFIER 
            {
              Expression RESULT =null;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		String o = (String)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = new NameExpression((String)o);      
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // expr ::= STRING_LITERAL 
            {
              Expression RESULT =null;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		String o = (String)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = new StringExpression((String)o);    
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // expr ::= BOOLEAN_LITERAL 
            {
              Expression RESULT =null;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		Boolean o = (Boolean)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = new BooleanExpression((Boolean)o);  
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // expr ::= DOUBLE_LITERAL 
            {
              Expression RESULT =null;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		Double o = (Double)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = new DoubleExpression((Double)o);    
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // expr ::= INTEGER_LITERAL 
            {
              Expression RESULT =null;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		Integer o = (Integer)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = new IntegerExpression((Integer)o);  
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // expr ::= expr OR expr 
            {
              Expression RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).right;
		Expression e1 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).value;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).right;
		String o = (String)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		Expression e2 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = new OrExpression(e1, e2);           
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // expr ::= expr AND expr 
            {
              Expression RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).right;
		Expression e1 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).value;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).right;
		String o = (String)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		Expression e2 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = new AndExpression(e1, e2);          
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // expr ::= expr DIVIDE expr 
            {
              Expression RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).right;
		Expression e1 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).value;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).right;
		String o = (String)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		Expression e2 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = new DivideExpression(e1, e2);       
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // expr ::= expr MULTIPLY expr 
            {
              Expression RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).right;
		Expression e1 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).value;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).right;
		String o = (String)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		Expression e2 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = new MultiplyExpression(e1, e2);     
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // expr ::= expr MINUS expr 
            {
              Expression RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).right;
		Expression e1 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).value;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).right;
		String o = (String)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		Expression e2 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = new MinusExpression(e1, e2);        
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // expr ::= expr PLUS expr 
            {
              Expression RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).right;
		Expression e1 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)).value;
		int oleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).left;
		int oright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).right;
		String o = (String)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		Expression e2 = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = new PlusExpression(e1, e2);         
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("expr",1, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-2)), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // goal ::= expr 
            {
              Expression RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()).right;
		Expression e = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.peek()).value;
		 RESULT = e; parser.setResult(e); 
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("goal",0, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          return CUP$ExpressionParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // $START ::= goal EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).right;
		Expression start_val = (Expression)((java_cup.runtime.Symbol) CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)).value;
		RESULT = start_val;
              CUP$ExpressionParser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.elementAt(CUP$ExpressionParser$top-1)), ((java_cup.runtime.Symbol)CUP$ExpressionParser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$ExpressionParser$parser.done_parsing();
          return CUP$ExpressionParser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}
