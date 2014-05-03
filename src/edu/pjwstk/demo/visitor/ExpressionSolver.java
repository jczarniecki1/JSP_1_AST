package edu.pjwstk.demo.visitor;

import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.expression.Expression;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.parser.ExpressionParser;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.visitor.ASTVisitor;


public class ExpressionSolver {

    private static IStoreRepository repository;

    public static IAbstractQueryResult execute(String query){
        return execute(query, SolverParams.None);
    }

    public static IAbstractQueryResult execute(String query, SolverParams params){
        try {
            if (repository == null) {
                repository = StoreRepository.getInstance();
            }

            // Inicjalizacja qres i envs przed każdym zapytaniem
            QResStack qres = new QResStack();
            ASTVisitor visitor = new ConcreteASTVisitor(qres, repository);

            // Wykonanie zapytania
            Expression expression = parse(query);
            expression.accept(visitor);

            return qres.pop();
        }
        catch (Exception e) {
            if (params != SolverParams.Silent && params != SolverParams.ThrowExceptionOnly){
                e.printStackTrace();
            }
            if (params == SolverParams.ThrowException || params == SolverParams.ThrowExceptionOnly){
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    private static Expression parse(String query) throws Exception {
        ExpressionParser parser = new ExpressionParser(query);
        parser.user_init();
        parser.parse();

        return parser.RESULT;
    }
}
