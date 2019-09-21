package com.ypcxpt.fish.library.fetch;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;

import com.ypcxpt.fish.library.fetch.config.FetchConfig;
import com.ypcxpt.fish.library.fetch.handler.BaseResultHandler;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.view.IView;

import io.reactivex.Flowable;

public abstract class AbsFetcher<R> extends BaseFetcher<R> {
    /* UI关联操作汇总 */
    protected FetchConfig mFC;

    /* 是否显示加载页 */
    protected boolean showLoading;

    /* 是否显示无网提示 */
    protected boolean showNoNetWarning;

    /**
     * 必须传入数据源.
     *
     * @param source
     */
    public AbsFetcher(@NonNull Flowable<R> source) {
        super(source);
    }

    /**
     * 可选，若无需{@link IView}关联的操作，可以不设置。
     */
    public AbsFetcher<R> withView(IView view) {
        getFetchConfig().view = mView = view;
        return this;
    }

    /**
     * 可选，是否显示加载页。默认不显示。
     */
    public AbsFetcher<R> showLoading(boolean showLoading) {
        getFetchConfig().showLoading = this.showLoading = showLoading;
        return this;
    }

    /**
     * 可选，无网时的提示方式。默认不显示。
     */
    public AbsFetcher<R> showNoNetWarning(boolean showNoNetWarning) {
        this.showNoNetWarning = showNoNetWarning;
        return this;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void rawStart(BaseResultHandler handler) {
        /* 没有绑定IView，却需要IView相关的交互 */
        if (!gotView() && needView()) {
//            throw new IllegalArgumentException("需要先绑定IView实现UI关联操作");
            return;
        }
        /* 需要无网提示. 言外之意IView不为空 */
        if (!NetworkUtils.isConnected() && showNoNetWarning) {
            commonNoNetWorkWarning();
            return;
        }
        rebuildSource().subscribe(handler);
    }

    private Flowable<R> rebuildSource() {
        if (showLoading) mView.showLoading();
        if (gotView()) {
            mSource = mSource.compose(mView.bindToRxLifecycle());
        }
        return mSource.compose(ThreadHelper.applySchedulers);
    }

    public FetchConfig getFetchConfig() {
        return mFC == null ? mFC = new FetchConfig() : mFC;
    }

    private boolean gotView() {
        return mView != null;
    }

    private boolean needView() {
        return showLoading || showNoNetWarning;
    }

}
