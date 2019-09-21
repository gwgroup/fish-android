package com.ypcxpt.fish.library.fetch.handler;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import com.ypcxpt.fish.library.net.exception.ApiException;
import com.ypcxpt.fish.library.net.exception.BusinessException;
import com.ypcxpt.fish.library.net.response.BizMsg;
import com.ypcxpt.fish.library.util.Toaster;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

public class BaseResultHandler<T> implements Subscriber<T> {
    /* 业务逻辑成功回调 */
    protected Consumer<T> mOnSuccess;

    /* 业务逻辑失败回调 */
    protected Consumer<BizMsg> mOnBizError;

    /* 其他异常会回调 */
    protected Consumer<Throwable> mOnError;

    /* 是否获取到数据. 包含本地缓存的情况 */
    protected boolean hasGotData;

    public BaseResultHandler(Consumer<T> onSuccess, Consumer<BizMsg> onBizError, Consumer<Throwable> onError) {
        this.mOnSuccess = onSuccess;
        this.mOnBizError = onBizError;
        this.mOnError = onError;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Integer.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        /* 获取到的数据是否为空 */
        if (!ObjectUtils.isEmpty(t)) hasGotData = true;
        handleOnSuccess(t);
    }

    @Override
    public void onComplete() {
        /* 如果没数据，则不会走onNext */
        if (!hasGotData) handleOnSuccess(null);
        onFinalComplete();
    }

    @Override
    public void onError(Throwable e) {
        /* 异常分析 */
        if (e instanceof HttpException || e instanceof SocketTimeoutException || e instanceof ConnectException) {
            /* 网络异常 */
            handleOtherError(e);
        } else if (e instanceof JsonIOException || e instanceof JsonSyntaxException || e instanceof JsonParseException) {
            /* 解析异常 */
            handleOtherError(e);
        } else if (e instanceof ApiException) {
            /* 数据异常 */
            handleApiException(e);
        } else if (e instanceof BusinessException) {
            /* 业务逻辑相关错误 */
            handleBizError(e);
        } else {
            /* 其他异常 */
            handleOtherError(e);
        }
        onFinalError();
    }

    /**
     * “业务逻辑成功”处理.
     * 大概率发生在{@link #onNext(Object)}中，小概率{@link #onComplete()}.
     */
    protected void handleOnSuccess(T t) {
        if (mOnSuccess != null) {
            try {
                mOnSuccess.accept(t);
            } catch (Exception e) {
                /* 非常重要，减少crash率！ 对于业务逻辑成功 的处理过程中引发的异常，都被拦截在此 */
                e.printStackTrace();
                /* 执行过程中又出现的异常，最后还是会传给onError处理 */
//                handleOtherError(e);
            }
        }
    }

    /**
     * “业务逻辑错误”处理.
     * 只会发生在{@link #onError(Throwable)}中.
     */
    private void handleBizError(Throwable t) {
        BizMsg bizMsg = ((BusinessException) t).getBizMsg();
        if (mOnBizError != null) {
            try {
                if (!onBizIntercept(bizMsg)) {
                    mOnBizError.accept(bizMsg);
                }
            } catch (Exception e) {
                /* 非常重要，减少crash率！ 对于业务逻辑错误 的处理过程中引发的异常 都被拦截于此 */
                e.printStackTrace();
                /* 执行过程中又出现的异常，最后还是会传给onError处理 */
//                handleOtherError(e);
            }
        }
        /* 最后给出提示信息 */
        showBizMsg(bizMsg);
    }

    /**
     * 数据异常.
     * 只会发生在{@link #onError(Throwable)}中.
     */
    private void handleApiException(Throwable t) {
        if (ApiException.DATA_NULL.equals(t.getMessage())) {
            handleOnSuccess(null);
        } else {
            handleOtherError(t);
        }
    }

    /**
     * “其他错误”处理.
     */
    protected void handleOtherError(Throwable t) {
        if (mOnError != null) {
            try {
                mOnError.accept(t);
            } catch (Exception e) {
                /* 非常重要，减少crash率！ 对于其他异常 的处理过程中又引发的异常 都被拦截于此.
                   如果异常处理执行过程中还出现异常，那么神都救不了了. */
                e.printStackTrace();
            }
        }
        /* 最后给出提示信息 */
        showErrorMsg();
    }

    /**
     * 需要进行拦截的“业务逻辑错误”，一般交给子类覆写.
     *
     * @param bizMsg 业务逻辑信息.
     * @return 默认false不拦截.
     */
    protected boolean onBizIntercept(BizMsg bizMsg) {
        return false;
    }

    /**
     * “业务逻辑”提示信息.
     */
    private void showBizMsg(BizMsg bizMsg) {
        if (!StringUtils.isTrimEmpty(bizMsg.msg)) {
            Toaster.showLong(bizMsg.msg);
        }
    }

    /**
     * “其他错误”提示信息.
     */
    private void showErrorMsg() {
        Toaster.showLong("网络连接异常");
    }

    /**
     * 成功后最后要处理的事.
     */
    protected void onFinalComplete() {
    }


    /**
     * 失败后最后要处理的事.
     */
    protected void onFinalError() {
    }


}
