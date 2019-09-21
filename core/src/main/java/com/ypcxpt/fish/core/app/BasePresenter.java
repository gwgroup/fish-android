package com.ypcxpt.fish.core.app;

import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.library.fetch.BaseFetcher;
import com.ypcxpt.fish.library.presenter.AbsPresenter;
import com.ypcxpt.fish.library.view.IView;

import io.reactivex.Flowable;

public class BasePresenter<T extends IView> extends AbsPresenter<T> {
    @Override
    public <R> BaseFetcher<R> fetch(Flowable<R> source) {
        return new Fetcher<>(source)
                .withView(mView)
                .showLoading(true)
                .showNoNetWarning(true);
    }

    @Override
    public <R> BaseFetcher<R> silenceFetch(Flowable<R> source) {
        return new Fetcher<>(source).withView(mView);
    }
}
