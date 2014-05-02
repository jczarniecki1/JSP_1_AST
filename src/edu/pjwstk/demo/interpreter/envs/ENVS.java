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

        // Inicjuje stos i dodaje pierwszą ramkę
        //  - pierwsza ramka składa się z ENVSBinderów do dzieci elementu "entry"
        //  - każdy Binder ma tylko referencję na obiekt

        stack = new Stack<>();

        IComplexObject entry = (IComplexObject)store.retrieve(rootOID);

        Collection<IENVSBinder> frameCollection =
            entry.getChildOIDs()
                .stream()
                .map(childId -> getENVSBinder(store, childId))
                .collect(Collectors.toList());

        push(new ENVSFrame(frameCollection));
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

        // Szuka elementu na stosie środowiskowym po nazwie
        //  - najpierw znajduje poziom (pierwszą pasującą ramkę)
        //  - potem wybiera pasujące elementy

        IENVSFrame frame = findFrameForName(name);

        if (frame == null) {
            return BagResult.Empty();
        }
        else {
            Collection<ISingleResult> results = getResultsFromFrame(name, frame);
            return new BagResult(results);
        }
    }

    @Override
    public IENVSFrame nested(IAbstractQueryResult result, ISBAStore store) {

        // Ładowanie obiektu do ramki
        //  - cała logika w konstruktorze ramki

        return new ENVSFrame(result, store);
    }

    /*
     * ***************** Dodatkowe metody *************************
     */

    private ENVSBinder getENVSBinder(ISBAStore store, IOID id) {

        // Zamiana id na ENVSBinder

        ISBAObject object = store.retrieve(id);
        ReferenceResult objectRef = new ReferenceResult(id);
        return new ENVSBinder(object.getName(), objectRef);
    }

    private IENVSFrame findFrameForName(String name) {

        // Szukanie ramki, na której jest binder do podanej nazwy

        int limit = stack.size();
        for (int i = limit-1; i >= 0; i--) {
            IENVSFrame currentFrame = stack.get(i);
            if (currentFrame.getElements()
                .stream()
                .anyMatch(x -> x.getName().equals(name)))
            {
                return currentFrame;
            }
        }
        return null;
    }

    private List<ISingleResult> getResultsFromFrame(String name, IENVSFrame frame) {

        // Pobranie wartości binderów z ramki po nazwie

        Collection<IENVSBinder> elementsInFrame = frame.getElements();
        List<ISingleResult> results = new ArrayList<>();

        for (IENVSBinder binder : elementsInFrame) {
            if (binder.getName().equals(name)) {
                results.add((ISingleResult) binder.getValue());
            }
        }
        return results;
    }

}
