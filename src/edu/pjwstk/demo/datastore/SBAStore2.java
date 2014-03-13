package edu.pjwstk.demo.datastore;

import edu.pjwstk.demo.res.BagResult;
import edu.pjwstk.demo.res.ReferenceResult;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.ArrayList;
import java.util.Collection;

public class SBAStore2 extends SBAStore implements ISBAStore2 {

    @Override
    public IOID getLastOID() {
        return new OID(lastGeneratedId);
    }

    @Override
    public IBagResult getFakeBag(String name) {
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
