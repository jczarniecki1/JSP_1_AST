package edu.pjwstk.demo.interpreter.envs;

import edu.pjwstk.demo.result.ReferenceResult;
import edu.pjwstk.jps.datastore.IComplexObject;
import edu.pjwstk.jps.datastore.IOID;
import edu.pjwstk.jps.datastore.ISBAObject;
import edu.pjwstk.jps.datastore.ISBAStore;
import edu.pjwstk.jps.interpreter.envs.IENVSBinder;
import edu.pjwstk.jps.interpreter.envs.IENVSFrame;
import edu.pjwstk.jps.result.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class ENVSFrame implements IENVSFrame {
    private Collection<IENVSBinder> elements;

    @Override
    public Collection<IENVSBinder> getElements() {
        return elements;
    }

    public ENVSFrame(Collection<IENVSBinder> elements) {
        this.elements = elements;
    }

    public ENVSFrame(IAbstractQueryResult elements, ISBAStore store) {
        this.elements = initElements(elements, store);
    }

    private Collection<IENVSBinder> initElements(IAbstractQueryResult x, ISBAStore store) {

        // Przydziela odpowiednią akcję względem typu analizowanego obiektu

             if (x instanceof IReferenceResult) return initElements((IReferenceResult)x, store);
        else if (x instanceof IBinderResult) return initElements((IBinderResult)x);
        else if (x instanceof IStructResult) return initElements((IStructResult)x, store);
        else return new ArrayList<>();
    }

    private Collection<IENVSBinder> initElements(IReferenceResult reference, ISBAStore store) {

        ArrayList<IENVSBinder> newElements = new ArrayList<>();
        ISBAObject object = store.retrieve(reference.getOIDValue());

        if (object instanceof IComplexObject){

            // Jeśli referencja na obiekt złożony, to dodaje Bindery z referencjami na jego pola

            newElements.addAll(
                ((IComplexObject) object).getChildOIDs()
                    .stream()
                    .map(x ->
                        new ENVSBinder(
                            retriveName(x, store),
                            new ReferenceResult(x)
                        )
                    )
                    .collect(Collectors.toList())
            );
        }
        else if (object instanceof IReferenceResult){

            // Jeśli referencja na referencję, to dodaje Bindery z jedną referencją

            IReferenceResult innerReference = (IReferenceResult) object;
            newElements.add(
                new ENVSBinder(
                    retriveName(innerReference, store),
                    innerReference
                )
            );
        }

        return newElements;
    }

    private Collection<IENVSBinder> initElements(IBinderResult binder) {

        // Jeśli Binder, to dodaje taki sam ENVSBinder

        ArrayList<IENVSBinder> newElements = new ArrayList<>(1);
        newElements.add(
            new ENVSBinder(
                binder.getName(),
                binder.getValue()
            )
        );
        return newElements;
    }

    private Collection<IENVSBinder> initElements(IStructResult result, ISBAStore store) {

        // Jeśli struktura, to wykonaj nested (czyli initElements) na każdym z jej elementów
        //  - flatMap scala wszyskie ENVSBindery w jedną listę do ramki

        return result.elements()
            .stream()
            .flatMap(structElement -> initElements(structElement, store).stream())
            .collect(Collectors.toList());
    }

    //
    // Skróty do wyciągania nazw dla ENVSBinderów
    //
    private String retriveName(IReferenceResult reference, ISBAStore store) {
        return store.retrieve(reference.getOIDValue()).getName();
    }

    private String retriveName(IOID id, ISBAStore store) {
        return store.retrieve(id).getName();
    }
}
