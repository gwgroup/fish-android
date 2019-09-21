package com.ypcxpt.fish.main.view.activity;

import android.Manifest;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.util.CommonWheelHelper;
import com.ypcxpt.fish.app.util.DialogRegion;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.library.ui.widget.TitleBar;
import com.ypcxpt.fish.library.ui.widget.popup.PopupItem;
import com.ypcxpt.fish.library.ui.widget.popup.actionsheet.ActionSheet;
import com.ypcxpt.fish.library.util.B64PhotoHelper;
import com.ypcxpt.fish.library.util.FormatUtils;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.PermissionHelper;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.library.util.ViewHelper;
import com.ypcxpt.fish.library.view.activity.BaseActivity;
import com.ypcxpt.fish.login.model.UserProfile;
import com.ypcxpt.fish.main.contract.EditUserProfileContract;
import com.ypcxpt.fish.main.event.OnProfileUpdatedEvent;
import com.ypcxpt.fish.main.model.RegionInfo;
import com.ypcxpt.fish.main.presenter.EditUserProfilePresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

import static com.ypcxpt.fish.app.util.CommonWheelHelper.showDatePickerWithData;
import static com.ypcxpt.fish.app.util.DisplayUtils.getFormatDate;
import static com.ypcxpt.fish.library.util.ViewHelper.setText;

@Route(path = Path.Main.EDIT_PROFILE)
public class EditUserProfileActivity extends BaseActivity implements EditUserProfileContract.View {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.et_nick_name)
    EditText etNickName;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.et_height)
    EditText etHeight;
    @BindView(R.id.et_weight)
    EditText etWeight;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.iv_avatarDisplay)
    CircleImageView iv_avatarDisplay;
    @BindView(R.id.tv_region)
    TextView tv_region;

    @Autowired
    public UserProfile mUserProfile;

    private EditUserProfileContract.Presenter mPresenter;

    private ActionSheet<PopupItem> mPopup;

    private DatePicker mDataPicker;

    @Override
    protected int layoutResID() {
        return R.layout.activiy_edit_user_profile;
    }

    @Override
    protected void initData() {
        mPresenter = new EditUserProfilePresenter();
        addPresenter(mPresenter);
        mPresenter.acceptData("mUserProfile", mUserProfile);
    }

    @Override
    protected void initViews() {
        initInteract();
        refreshUI();
        etHeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        etWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public void onBackPressed() {
//        saveProfile();
        finish();
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.rl_save)
    public void onBackSave() {
        saveProfile();
    }

    private void refreshUI() {
        setText(etNickName, mUserProfile.name);
        setText(tvGender, mUserProfile.getGender());
        setText(tvBirthday, getFormatDate(mUserProfile.birthday));
        String strHeight = mUserProfile.height > 0 ? String.valueOf(mUserProfile.height) : "";
        setText(etHeight, strHeight);
        String strWeight = mUserProfile.weight > 0 ? String.valueOf(mUserProfile.weight) : "";
        setText(etWeight, strWeight);
        setText(etAddress, mUserProfile.address);
        ViewHelper.moveToLastCursor(etNickName);

        displayAvatar(mUserProfile);
        setText(tv_region, mUserProfile.region_name);
    }

    private void initInteract() {
        titleBar.setLeftClick(view -> onBackPressed());
        mPopup = new ActionSheet<>(this, itemData -> saveGender(itemData));
        mPopup.setNewData(mPresenter.getGenderData());
        mDataPicker = CommonWheelHelper.createCommonDatePicker(this, birthday -> saveBirthday(birthday));
    }

    private void displayAvatar(UserProfile userProfile) {
        String avatar = userProfile.avatar;
        /* 如果没有设置头像，则不处理，上面已经处理过 */
        if (StringUtils.isTrimEmpty(avatar)) {
            int resID = userProfile.gender == 1 ? R.mipmap.ic_default_avatar_male : R.mipmap.ic_default_avatar_female;
            iv_avatarDisplay.setImageResource(resID);
            return;
        }
        B64PhotoHelper.load(this, avatar, iv_avatarDisplay);
    }

    public void saveProfile() {
        if ("".equals(etNickName.getText().toString().trim())) {
            Toaster.showShort("请输入昵称");
            return;
        }
        saveET();
        if (mPresenter != null && mPresenter.isProfileChanged()) {
            mPresenter.saveProfile();
        } else {
            super.onBackPressed();
        }
    }

    private void saveET() {
        mUserProfile.name = ViewHelper.getText(etNickName);
        mUserProfile.address = ViewHelper.getText(etAddress);
        mUserProfile.region_name = ViewHelper.getText(tv_region);
        mUserProfile.height = FormatUtils.getInt(ViewHelper.getText(etHeight));
        mUserProfile.weight = FormatUtils.getInt(ViewHelper.getText(etWeight));
    }

    private void saveBirthday(String birthday) {
        saveET();
        mUserProfile.birthday = birthday;
        refreshUI();
    }

    private void saveAvatar(String path) {
        saveET();
        ThreadHelper.run(() -> {
            String b64Str = B64PhotoHelper.photoToB64Str(this, path);
            if (!StringUtils.isTrimEmpty(b64Str)) {
                mUserProfile.avatar = "data:image/png;base64," + b64Str;
                Logger.e("b64Str-->", b64Str);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        B64PhotoHelper.load(EditUserProfileActivity.this, b64Str, iv_avatarDisplay);
                    }
                });
            }
        });
    }

    private void saveGender(PopupItem itemData) {
        saveET();
        if ("男".equals(itemData.name)) {
            mUserProfile.gender = 1;
        } else {
            mUserProfile.gender = 2;
        }

        refreshUI();
    }

    @OnClick(R.id.rl_birthday)
    public void onBirthdayClick() {
        showDatePickerWithData(mDataPicker, mUserProfile.birthday);
    }

    @OnClick(R.id.rl_gender)
    public void onGenderClick() {
        mPopup.showPopupWindow();
    }

    @OnClick(R.id.rl_avatar)
    public void onAvatarClick() {
        new PermissionHelper().requestPermissions(this, this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA});
    }

    @OnClick({R.id.ll_nick_name, R.id.ll_weight, R.id.ll_height})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_nick_name:
                etNickName.requestFocus();
                break;
            case R.id.ll_weight:
                etWeight.requestFocus();
                break;
            case R.id.ll_height:
                etHeight.requestFocus();
                break;
        }
    }

    @Override
    public void onAcceptAllPermissions() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(true)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    public void onDenySomePermissions(List<String> deniedPermissions) {
        Toaster.showLong("没有权限开启相册");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE && data != null) {
            ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            if (!ObjectUtils.isEmpty(photos)) {
                String path = photos.get(0);
                saveAvatar(path);
            }
        }
    }

    @Override
    public void onUpdateSuccess(UserProfile newProfile) {
        EventBus.getDefault().post(new OnProfileUpdatedEvent(newProfile));
        Toaster.showLong("修改成功");
        finish();
    }

    @OnClick(R.id.ll_region)
    public void onSelectAddress() {
        mPresenter.selectAddress("0");
    }

    @Override
    public void showAddressRegion(List<RegionInfo> regionInfos) {
        DialogRegion.showRegionDialog(this, regionInfos, new DialogRegion.RegionListener() {
            @Override
            public void searchComplete(String code, String addressPin) {
                saveET();
                mUserProfile.region_code = code;
                mUserProfile.region_name = addressPin;

                refreshUI();
                Logger.i("区域", "code--" + code + ",address--" + addressPin);
//                Toaster.showShort(code + "," + addressPin.substring(0, addressPin.length() - 1));
            }
        });
    }

}
