package com.ypcxpt.fish.library.definition;

/**
 * 作用同{@link java.util.function.Consumer}，但前者要求sdk最低为24.
 * 该类不同于{@link io.reactivex.functions.Consumer}，不会抛异常.
 */
public interface JConsumer<T> {
    /**
     * Consume the given value.
     *
     * @param t the value
     */
    void accept(T t);
}
