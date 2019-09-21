package com.ypcxpt.fish.library.definition;

/**
 * 不抛异常版的{@link io.reactivex.functions.Action}.
 * <p>
 * 注：
 * 不同于Consumer，JDK8中没有Action类.
 * 但为了区别RxJava2的Action，延用与{@link JConsumer}相同的命名方式.
 */
public interface JAction {
    /**
     * Runs the action.
     */
    void run();
}
