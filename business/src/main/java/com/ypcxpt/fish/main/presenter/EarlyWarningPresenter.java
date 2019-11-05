package com.ypcxpt.fish.main.presenter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.core.ble.BLEClient;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.device.model.Scenes;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.main.adapter.SceneAdapter;
import com.ypcxpt.fish.main.contract.EarlyWarningContract;
import com.ypcxpt.fish.main.event.OnGetScenesEvent;
import com.ypcxpt.fish.main.model.IoInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.ypcxpt.fish.core.ble.output.data.DeviceConstant.TARGET_CHARACTERISTIC_ID;

public class EarlyWarningPresenter extends BasePresenter<EarlyWarningContract.View> implements EarlyWarningContract.Presenter {
    private Disposable mScanDisposable;

    private SceneAdapter mAdapter;

    private DataSource mDS;

    public EarlyWarningPresenter() {
        mDS = new DataRepository();
    }

    public static boolean isBleConnecting = false;
    RxBleDevice bleDevice;
    @Override
    public void connectServices(NetDevice device) {
        String macAddress = device.macAddress;
        String chairName = device.type;
        if (StringUtils.isTrimEmpty(macAddress)) {
            Toaster.showLong("无效的设备信息");
            return;
        }

//        mView.showLoading();
        bleDevice = BLEClient.get().getBleDevice(macAddress);

        disposable = bleDevice.establishConnection(false)
                .flatMapSingle(RxBleConnection::discoverServices)
                .take(1) // Disconnect automatically after discovery
                .compose(getActivity().bindUntilEvent(ActivityEvent.PAUSE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(services -> {
                    isBleConnecting = true;
//                    mView.dismissLoading();
                    jumpToDeviceDetail(services, macAddress, chairName);

                }, this::onConnectionFailure);

//        ThreadHelper.postDelayed(() -> {
//            if (isConnected()) {
//                return;
//            }
//            if (!isBleConnecting) {
//                if (disposable != null) {
//                    disposable.dispose();
//                    clearDisposable();
////                    mView.onStopScan();
////                    mView.dismissLoading();
//                    dialog.dismiss();
//                }
////                mView.dismissLoading();
//                Toaster.showLong("连接不到设备，请稍后重试");
//            }
//
////            if (!isBleConnecting) {
////                Toaster.showShort("连接不到设备，请稍后重试");
////            }
////            if (disposable != null) {
////                disposable.dispose();
////                clearDisposable();
//////                    mView.onStopScan();
//////                    mView.dismissLoading();
////                dialog.dismiss();
////            }
//
//        }, 6000);
    }

    private boolean isConnected() {
        return bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
    }
    private Disposable disposable;
    private void clearDisposable() {
        disposable = null;
        isBleConnecting = false;
    }

    /**
     * 设备连接跳转逻辑
     * @param services
     * @param macAddress
     */
    private void jumpToDeviceDetail(RxBleDeviceServices services, String macAddress, String chairName) {
        Logger.d("CCC", services.toString());
        isBleConnecting = true;
        List<BluetoothGattService> bluetoothGattServices = services.getBluetoothGattServices();
        for (BluetoothGattService service : bluetoothGattServices) {
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                String characteristicUuid = characteristic.getUuid().toString();
                if (TARGET_CHARACTERISTIC_ID.equals(characteristicUuid)) {
                    if ("HOME-8".equals(chairName)) {
                        Router.build(Path.Device.DETAIL)
                                .withString("characteristicUuid", characteristicUuid)
                                .withString("macAddress", macAddress)
                                .withString("chairName", chairName)
                                .navigation(getActivity());
                    } else if ("HOME-6".equals(chairName)) {
                        Router.build(Path.Device.SHELL_CHAIR)
                                .withString("characteristicUuid", characteristicUuid)
                                .withString("macAddress", macAddress)
                                .withString("chairName", chairName)
                                .navigation(getActivity());
                    } else if ("T100".equals(chairName)) {
                        Router.build(Path.Device.T100)
                                .withString("characteristicUuid", characteristicUuid)
                                .withString("macAddress", macAddress)
                                .withString("chairName", chairName)
                                .navigation(getActivity());
                    }
//                    getActivity().overridePendingTransition(R.anim.anim_right_in,R.anim.anim_left_out);
                    Log.i("MyDevicePresenter", "macAddress:" + macAddress + ",characteristicUuid:" + characteristicUuid);
                }
            }
        }
    }

    private void onConnectionFailure(Throwable throwable) {
//        mView.dismissLoading();
        Logger.d("CCC", throwable.toString());
        Toaster.showLong("连接不到设备，请重试");
        isBleConnecting = true;
    }

    @Override
    public void removeDevice(NetDevice device) {
//        Flowable<Object> source = mDS.removeDevice(device);
//        silenceFetch(source)
//                .onSuccess(o -> {
//                    Logger.d("CCC", "移除成功");
//                    EventBus.getDefault().post(new OnGetScenesEvent());
//                })
//                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
//                .onError(throwable -> Logger.d("CCC", throwable.toString()))
//                .start();
    }

    @Override
    public void renameDevice(NetDevice device) {
//        Flowable<Object> source = mDS.renameDevice(device);
//        silenceFetch(source)
//                .onSuccess(o -> {
//                    Logger.d("CCC", "重命名成功");
//                    EventBus.getDefault().post(new OnGetScenesEvent());
//                })
//                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
//                .onError(throwable -> Logger.d("CCC", throwable.toString()))
//                .start();
    }

    @Override
    public void getScenes() {
        Flowable<List<Scenes>> source = mDS.getScenes();
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
        .onSuccess(scenes -> {
            Logger.d("CCC", "scenes-->" + scenes.toString());
            mView.showScenes(scenes);
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void skipDetail(NetDevice device) {
        Router.build(Path.Main.DEVICE_MANAGER_DETAIL)
                .withParcelable("mDevice", device)
                .navigation(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
