package edu.pjwstk.demo.interpreter.envs;

import edu.pjwstk.demo.result.BagResult;
import edu.pjwstk.demo.result.ReferenceResult;
import edu.pjwstk.jps.datastore.IComplexObject;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.interpreter.envs.IENVS;
import edu.pjwstk.jps.interpreter.envs.IENVSBinder;
import edu.pjwstk.jps.interpreter.envs.IENVSFrame;
import edu.pjwstk.jps.result.*;

import java.util.*;
import java.util.stream.Collectors;

public class ENVS implements IENVS {
    private Stack<IENVSFrame> stack;

    @Override
    public void init(IOID rootOID, ISBAStore store) {
        stack = new Stack<>();

        IComplexObject entry = (IComplexObject)store.retrieve(rootOID);

        Collection<IENVSBinder> frameCollection =
            entry.getChildOIDs()
            .stream()
            .map(x -> {
                ISBAObject object = store.retrieve(x);
                return new ENVSBinder(object.getName(), new ReferenceResult(x));
            })
            .collect(Collectors.toList());

        IENVSFrame frame = new ENVSFrame(frameCollection);
        push(frame);
    }

    @Override
    public IENVSFrame pop() {
        return stack.pop();
    }

    @Override
    public void push(IENVSFrame frame) {
        stack.push(frame);
    }

    @Override
    public IBagResult bind(String name) {
        List<ISingleResult> elements = new ArrayList<>();
        IENVSFrame firstFrame = null;

        int limit = stack.size();
        for (int i=0; i < limit; i++) {
            IENVSFrame currentFrame = stack.get(i);
            if (currentFrame.getElements()
                .stream()
                .anyMatch(x -> x.getName().equals(name)))
            {
                firstFrame = currentFrame;
            }
        }

        if (firstFrame != null) {
            Collection<IENVSBinder> elementsInFrame = firstFrame.getElements();

            for (IENVSBinder binder : elementsInFrame) {
                if (binder.getName().equals(name)) {
                    elements.add((ISingleResult) binder.getValue());
                }
            }
        }

        return new BagResult(elements);
    }

    @Override
    public IENVSFrame nested(IAbstractQueryResult result, ISBAStore store) {
        return new ENVSFrame(result, store);
    }
}
