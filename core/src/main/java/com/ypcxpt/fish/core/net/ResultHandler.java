package com.ypcxpt.fish.core.net;

import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.BaseApp;
import com.ypcxpt.fish.library.fetch.config.FetchConfig;
import com.ypcxpt.fish.library.fetch.handler.BaseResultHandler;
import com.ypcxpt.fish.library.net.response.BizMsg;
import com.ypcxpt.fish.library.router.Router;

import io.reactivex.functions.Consumer;

import static com.ypcxpt.fish.core.app.ErrorCode.TOKEN_EXPIRED;
import static com.ypcxpt.fish.core.app.ErrorCode.VALIDATE_USER;

public class ResultHandler<T> extends BaseResultHandler<T> {
    private FetchConfig mFC;

    public ResultHandler(Consumer<T> onSuccess, Consumer<BizMsg> onBizError, Consumer<Throwable> onError, FetchConfig fc) {
        super(onSuccess, onBizError, onError);
        mFC = fc;
    }

    @Override
    protected void onFinalComplete() {
        super.onFinalComplete();
        dismissLoading();
    }

    @Override
    protected void onFinalError() {
        super.onFinalError();
        dismissLoading();
    }

    /**
     * 关闭loading页
     */
    private void dismissLoading() {
        if (mFC != null && mFC.view != null && mFC.showLoading) {
            mFC.view.dismissLoading();
        }
    }

    @Override
    protected boolean onBizIntercept(BizMsg bizMsg) {
        if (bizMsg.code == TOKEN_EXPIRED || bizMsg.code == VALIDATE_USER) {
            Router.build(Path.Login.LOGIN).clearTask().navigation(BaseApp.getApp());
            return true;
        } else {
            return super.onBizIntercept(bizMsg);
        }
    }
}
