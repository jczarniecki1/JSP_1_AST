package edu.pjwstk.tests.parser;

import edu.pjwstk.demo.datastore.IStoreRepository;
import edu.pjwstk.demo.datastore.SBAStore;
import edu.pjwstk.demo.datastore.StoreRepository;
import edu.pjwstk.demo.parser.ExpressionParser;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.interpreter.qres.IQResStack;
import edu.pjwstk.jps.visitor.ASTVisitor;
import org.junit.Before;

public abstract class AbstractParserTest {
    protected IQResStack qres;
    protected ISBAStore store;
    protected IStoreRepository repository;
    protected ASTVisitor visitor;
    protected ExpressionParser parser;

    public abstract void initData();

    @Before
    public void initContext(){

        store = SBAStore.getClearInstance();
        repository = StoreRepository.getInstance();

        initData();
    }
}
