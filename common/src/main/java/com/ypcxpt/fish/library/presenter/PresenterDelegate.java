package com.ypcxpt.fish.library.presenter;

import com.blankj.utilcode.util.ObjectUtils;

import com.ypcxpt.fish.library.util.MapUtils;
import com.ypcxpt.fish.library.view.IView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresenterDelegate implements ISimplePresenter {
    private Map<Class, IPresenter> mPresenterMap;

    private List<IPresenter> mPresenterList;

    private int mSignature;

    /**
     * @param presenter
     */
    public <P extends IPresenter> void add(P presenter) {
        if (presenter == null) {
            return;
        }
        getPresenterMap().put(presenter.getClass(), presenter);
    }

    /**
     * @param clazz The class of Presenter.
     * @return
     */
    public <T extends IPresenter> T get(Class<T> clazz) {
        if (mPresenterMap == null) {
            return null;
        }
        return (T) mPresenterMap.get(clazz);
    }

    @Override
    public void init(IView view) {
        if (isEmpty()) return;

        for (IPresenter presenter : getPresenterList()) {
            presenter.init(view);
        }
    }

    @Override
    public void openDataFetching() {
        if (isEmpty()) return;

        for (IPresenter presenter : getPresenterList()) {
            presenter.openDataFetching();
        }
    }

    @Override
    public void onStart() {
        if (isEmpty()) return;

        for (IPresenter presenter : getPresenterList()) {
            presenter.onStart();
        }
    }

    @Override
    public void onResume() {
        if (isEmpty()) return;

        for (IPresenter presenter : getPresenterList()) {
            presenter.onResume();
        }
    }

    @Override
    public void onPause() {
        if (isEmpty()) return;

        for (IPresenter presenter : getPresenterList()) {
            presenter.onPause();
        }
    }

    @Override
    public void onStop() {
        if (isEmpty()) return;

        for (IPresenter presenter : getPresenterList()) {
            presenter.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (isEmpty()) return;

        for (IPresenter presenter : getPresenterList()) {
            presenter.onDestroy();
        }
    }

    private Map<Class, IPresenter> getPresenterMap() {
        if (mPresenterMap == null) {
            mPresenterMap = new HashMap();
        }
        return mPresenterMap;
    }

    private List<IPresenter> getPresenterList() {
        int newSignature = mPresenterMap.toString().hashCode();

        if (mSignature != newSignature) {
            if (mPresenterList == null) {
                mPresenterList = new ArrayList<>();
            }
            mPresenterList.clear();
            mPresenterList.addAll(MapUtils.getValues(mPresenterMap));
            mSignature = newSignature;
        }

        return mPresenterList;
    }

    private boolean isEmpty() {
        return ObjectUtils.isEmpty(mPresenterMap);
    }

}
