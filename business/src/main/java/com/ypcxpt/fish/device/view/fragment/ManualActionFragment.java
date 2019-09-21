package com.ypcxpt.fish.device.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.core.model.DeviceAction;
import com.ypcxpt.fish.core.model.ManualActionModel;
import com.ypcxpt.fish.device.adapter.ManualActionAdapter;
import com.ypcxpt.fish.device.event.OnManualActionItemClickEvent;
import com.ypcxpt.fish.device.presenter.BaseBLEPresenter;
import com.ypcxpt.fish.device.view.activity.DeviceDetailActivity;
import com.ypcxpt.fish.library.ui.fuction.GridDecoration;
import com.ypcxpt.fish.library.ui.widget.popup.OnPopupItemClickListener;
import com.ypcxpt.fish.library.ui.widget.popup.actionsheet.ActionSheet;
import com.ypcxpt.fish.library.view.fragment.BaseFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

public class ManualActionFragment extends BaseFragment {
    @BindView(R.id.rv) RecyclerView rv;

    private BaseBLEPresenter mBlePresenter;

    private ActionSheet<DeviceAction> mPopup;

    private static final int INDIVIDUAL_COLUMNS = 3;

    @Override
    protected int layoutResID() {
        return R.layout.fragment_manual_mode;
    }

    @Override
    protected void initData() {
        mBlePresenter = ((DeviceDetailActivity) getActivity()).getBLEPresenter();
    }

    @Override
    protected void initViews() {
        initRv();
        initPopup();
    }

    private void initRv() {
        ManualActionAdapter adapter = new ManualActionAdapter(R.layout.item_manual_mode_induvidual);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), INDIVIDUAL_COLUMNS));
        rv.addItemDecoration(new GridDecoration(3f, INDIVIDUAL_COLUMNS));
        rv.setAdapter(adapter);
    }

    private void initPopup() {
        mPopup = new ActionSheet<>(getContext(), mOnPopupItemClickListener);
    }

    private OnPopupItemClickListener mOnPopupItemClickListener = (OnPopupItemClickListener<DeviceAction>)
            itemData -> mBlePresenter.writeData(itemData);

    private void showPopup(List<DeviceAction> actionList) {
        if (mPopup.isShowing()) {
            return;
        }
        mPopup.setNewData(actionList);
        mPopup.showPopupWindow();
    }

    @Subscribe
    public void onEventReceived(Object event) {
        if (event instanceof OnManualActionItemClickEvent) {
            ManualActionModel data = ((OnManualActionItemClickEvent) event).data;
            if (data.isIndividual()) {
                mBlePresenter.writeData(data.deviceAction);
            } else {
                showPopup(data.deviceActionList);
            }
        }
    }

}
