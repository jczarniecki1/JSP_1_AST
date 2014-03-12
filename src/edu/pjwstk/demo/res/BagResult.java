package edu.pjwstk.demo.res;

import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.Collection;

public class BagResult implements IBagResult{

    private Collection<ISingleResult> values;

    public BagResult(Collection<ISingleResult> values) {
        this.values = values;
    }

    @Override
    public Collection<ISingleResult> getElements() {
        return values;
    }
}
