package com.ypcxpt.fish.library.view;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * The Base 'V' in 'MVP'
 */
public interface IView {

    void showLoading();

    void dismissLoading();

    <T> LifecycleTransformer<T> bindToRxLifecycle();

}
