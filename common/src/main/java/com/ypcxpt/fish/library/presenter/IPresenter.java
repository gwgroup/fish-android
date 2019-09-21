package com.ypcxpt.fish.library.presenter;

import com.ypcxpt.fish.library.fetch.BaseFetcher;
import com.ypcxpt.fish.library.view.activity.BaseActivity;

import io.reactivex.Flowable;

/**
 * The Base 'P' in 'MVP'
 */
public interface IPresenter extends ISimplePresenter {

    /**
     * How a View send data to the relevant Presenter.
     *
     * @param filedName Presenter file name
     * @param value     可以传基本类型或者具体的引用类型的数据，与被赋值字段的类型保持一致。
     */
    void acceptData(String filedName, Object value);

    /**
     * 提供Activity对象.
     */
    BaseActivity getActivity();

    /**
     * 正常的数据请求，包含UI交互.
     */
    <R> BaseFetcher<R> fetch(Flowable<R> source);

    /**
     * 没有任何UI交互的数据请求.
     */
    <R> BaseFetcher<R> silenceFetch(Flowable<R> source);

}
