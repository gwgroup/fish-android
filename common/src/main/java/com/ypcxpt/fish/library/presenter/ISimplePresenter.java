package com.ypcxpt.fish.library.presenter;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.ypcxpt.fish.library.view.IView;

public interface ISimplePresenter {
    /**
     * Binds presenter with a view when created. The Presenter will perform initialization here.
     *
     * @param view
     */
    void init(IView view);

    /**
     * V's first time fetching of data. Could be both/either remote and/or local data.
     */
    void openDataFetching();

    /**
     * {@link Activity#onStart()} or {@link Fragment#onStart()}
     */
    void onStart();

    /**
     * {@link Activity#onResume()} or {@link Fragment#onResume()}
     */
    void onResume();

    /**
     * {@link Activity#onPause()} or {@link Fragment#onPause()}
     */
    void onPause();

    /**
     * {@link Activity#onStop()} or {@link Fragment#onStop()}
     */
    void onStop();

    /**
     * {@link Activity#onDestroy()} or {@link Fragment#onDestroy()}
     */
    void onDestroy();
}
