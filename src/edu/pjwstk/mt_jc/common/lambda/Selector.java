package edu.pjwstk.mt_jc.common.lambda;

public interface Selector<T, TResult> {
    TResult select(T element);
}
