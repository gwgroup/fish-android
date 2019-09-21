package com.ypcxpt.fish.core.net;

import android.support.annotation.NonNull;

import com.ypcxpt.fish.library.fetch.AbsFetcher;
import com.ypcxpt.fish.library.fetch.handler.BaseResultHandler;

import io.reactivex.Flowable;

public class Fetcher<R> extends AbsFetcher<R> {
    /**
     * 必须传入数据源.
     *
     * @param source
     */
    public Fetcher(@NonNull Flowable<R> source) {
        super(source);
    }

    @Override
    protected BaseResultHandler<R> buildResultHandler() {
        return new ResultHandler<>(mOnSuccess, mOnBizError, mOnError, mFC);
    }
}
