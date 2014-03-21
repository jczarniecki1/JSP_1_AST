package edu.pjwstk.demo.common;

import edu.pjwstk.demo.common.lambda.Predicate;
import edu.pjwstk.demo.common.lambda.Selector;
import edu.pjwstk.demo.result.BagResult;
import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.IReferenceResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Query {

    public static IBagResult where(IBagResult bag, Predicate<ISingleResult> predicate){
        List<ISingleResult> results = new ArrayList<>();
        for (ISingleResult element : bag.getElements()) {
            if (predicate.apply(element)) {
                results.add(element);
            }
        }
        return new BagResult(results);
    }
    public static IBagResult selectFromBag(IBagResult bag, Selector<ISingleResult,ISingleResult> selector){
        return new BagResult(select(bag.getElements(), selector));
    }

    public static <T,TItem> List<T> select(Collection<TItem> collection, Selector<TItem,T> selector){
        List<T> results = new ArrayList<>();
        for (TItem element : collection) {
            results.add(selector.select(element));
        }
        return results;
    }
}