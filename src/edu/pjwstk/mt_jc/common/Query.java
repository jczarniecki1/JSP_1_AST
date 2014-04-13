package edu.pjwstk.mt_jc.common;

import edu.pjwstk.mt_jc.common.lambda.Aggregator;
import edu.pjwstk.mt_jc.common.lambda.Predicate;
import edu.pjwstk.mt_jc.common.lambda.Selector;
import edu.pjwstk.mt_jc.result.BagResult;
import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.ISingleResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
    Ułatwia nawigowanie po kolekcjach z pomocą lambd
    Można dodawać więcej metod podobnych do tych z LINQ
*/
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

    public static <T,TItem> T aggregate(Collection<TItem> collection, T startValue, Aggregator<TItem,T> aggregator){
        T sum = startValue;
        for (TItem element : collection) {
            sum = aggregator.add(sum, element);
        }
        return sum;
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