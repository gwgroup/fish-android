package com.ypcxpt.fish.library.util;


import android.os.Handler;

import com.ypcxpt.fish.library.definition.JAction;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ThreadHelper {
    public static final Handler HANDLER = new Handler();

    public static final FlowableTransformer applySchedulers = (Flowable upstream) -> upstream.subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    /**
     * 使用场景：仅仅需要异步处理，而没有与主线程的交互.
     */
    public static void run(JAction action) {
        if (action == null) {
            return;
        }

        Flowable.fromCallable(() -> {
            action.run();
            return 1;
        }).compose(applySchedulers).subscribe(i -> {
        });
    }

    /**
     * 使用场景：仅仅需要run在主线程上.
     */
    public static void runOnMainThread(JAction action) {
        if (action == null) return;
        Single.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(i -> action.run());
    }

    public static void post(Runnable r) {
        HANDLER.post(r);
    }

    public static void postDelayed(Runnable r, long delayMillis) {
//        HANDLER.postDelayed(r, delayMillis);
        Completable.timer(delayMillis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(() -> r.run());
    }

}
