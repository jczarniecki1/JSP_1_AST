package edu.pjwstk.mt_jc.common.lambda;

public interface Aggregator<TItem, T> {
    public T add(T current, TItem element);
}
