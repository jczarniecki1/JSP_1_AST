package edu.pjwstk.demo.common.lambda;

public interface Aggregator<TItem, T> {
    public T add(T current, TItem element);
}
