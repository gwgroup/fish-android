package com.ypcxpt.fish.library.fetch;

import android.support.annotation.NonNull;

import com.ypcxpt.fish.library.fetch.handler.BaseResultHandler;
import com.ypcxpt.fish.library.net.response.BizMsg;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.view.IView;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public abstract class BaseFetcher<R> {
    /* 业务逻辑成功回调 */
    protected Consumer<R> mOnSuccess;

    /* 业务逻辑失败回调 */
    protected Consumer<BizMsg> mOnBizError;

    /* 其他异常回调 */
    protected Consumer<Throwable> mOnError;

    /* 数据源（本地＋网络/纯本地/纯网络) */
    protected Flowable<R> mSource;

    /* UI关联 */
    protected IView mView;

    /**
     * 必须传入数据源.
     */
    public BaseFetcher(@NonNull Flowable<R> source) {
        this.mSource = source;
    }

    /**
     * 开始获取（请求/加载）数据
     */
    public void start() {
        if (mSource == null) {
            throw new IllegalArgumentException("数据源不能为空");
        }
        rawStart(buildResultHandler());
    }

    /**
     * 设置“业务逻辑成功”回调
     */
    public BaseFetcher<R> onSuccess(Consumer<R> onSuccess) {
        mOnSuccess = onSuccess;
        return this;
    }

    /**
     * 设置“业务逻辑错误”回调
     */
    public BaseFetcher<R> onBizError(Consumer<BizMsg> onBizError) {
        mOnBizError = onBizError;
        return this;
    }

    /**
     * 设置“其他异常”回调
     */
    public BaseFetcher<R> onError(Consumer<Throwable> onError) {
        mOnError = onError;
        return this;
    }

    protected void commonNoNetWorkWarning() {
        Toaster.showLong("无网络连接");
    }

    protected abstract void rawStart(BaseResultHandler<R> handler);

    protected abstract BaseResultHandler<R> buildResultHandler();

}
