package edu.pjwstk.demo.common.lambda;

public interface Selector<T, TResult> {
    TResult select(T element);
}
