package edu.pjwstk.demo.datastore;

import edu.pjwstk.demo.result.BagResult;
import edu.pjwstk.demo.result.ReferenceResult;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.ArrayList;
import java.util.Collection;

public class SBAStoreForJavaObjects extends SBAStore implements ISBAStoreJavaObjects {

    @Override
    public IOID getLastOID() {
        return new OID(lastGeneratedId);
    }

    @Override
    public IBagResult getBag(String name) {
        Collection<ISingleResult> results = new ArrayList<>();

        for (ISBAObject o : hash.values())
        {
            if (o.getName() == name){
                results.add(new ReferenceResult(o.getOID()));
            }
        }
        return new BagResult(results);
    }
}
