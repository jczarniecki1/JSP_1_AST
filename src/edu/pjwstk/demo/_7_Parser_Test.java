package edu.pjwstk.demo;

import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.parser.ExpressionParser;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.interpreter.qres.IQResStack;
import edu.pjwstk.jps.visitor.ASTVisitor;

public class _7_Parser_Test {

    public static void main(String[] args) throws Exception {
   		ExpressionParser parser = new ExpressionParser("(1 + 2) * 3 / 4");
   		parser.user_init();
   		parser.parse();
   		Expression res = parser.RESULT;

        IQResStack qres = new QResStack();
        ISBAStore store = new SBAStore();
        IStoreRepository repository = new StoreRepository(store);
   		ASTVisitor visitor = new ConcreteASTVisitor(qres, repository);
        res.accept(visitor);
   		Log(qres.pop());
   	}

    private static void Log(Object o){
        System.out.println(o);
    }
}
