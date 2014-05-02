package edu.pjwstk.tests.expression;

import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.visitor.ConcreteASTVisitor;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.interpreter.qres.IQResStack;
import edu.pjwstk.jps.visitor.ASTVisitor;
import org.junit.Before;

public class AbstractExpressionTest {
    protected ISBAStore store;
    protected IQResStack qres;
    protected IStoreRepository repository;
    protected ASTVisitor visitor;

    @Before
    public void InitStore() {
        store = SBAStore.getClearInstance();
    }

    @Before
    public void InitVisitor() {
        qres = new QResStack();

        repository = StoreRepository.getInstance();
        visitor = new ConcreteASTVisitor(qres, repository);
    }

}
