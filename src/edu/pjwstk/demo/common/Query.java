package edu.pjwstk.demo.common;

import edu.pjwstk.demo.common.lambda.Predicate;
import edu.pjwstk.demo.common.lambda.Selector;
import edu.pjwstk.demo.result.BagResult;
import edu.pjwstk.jps.result.IBagResult;
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

    public static <T,TItem> List<T> select(Collection<TItem> collection, Selector<TItem,T> selector){
        List<T> results = new ArrayList<>();
        for (TItem element : collection) {
            results.add(selector.select(element));
        }
        return results;
    }

    public static <TItem> TItem firstOrDefault(Collection<TItem> collection, Predicate<TItem> predicate){
        for (TItem element : collection) {
            if (predicate.apply(element)) {
                return element;
            }
        }
        return null;
    }

    public static <TItem> boolean any(Collection<TItem> collection, Predicate<TItem> predicate){
        return firstOrDefault(collection, predicate) != null;
    }
}