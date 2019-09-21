package com.ypcxpt.fish.main.presenter;

import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.StringUtils;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.exceptions.BleScanException;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.trello.rxlifecycle2.android.ActivityEvent;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.app.repository.DataRepository;
import com.ypcxpt.fish.app.repository.DataSource;
import com.ypcxpt.fish.core.app.BasePresenter;
import com.ypcxpt.fish.core.app.Path;
import com.ypcxpt.fish.core.ble.BLEClient;
import com.ypcxpt.fish.core.ble.BLEHelper;
import com.ypcxpt.fish.core.net.Fetcher;
import com.ypcxpt.fish.device.model.NetDevice;
import com.ypcxpt.fish.library.router.Router;
import com.ypcxpt.fish.library.util.Logger;
import com.ypcxpt.fish.library.util.ThreadHelper;
import com.ypcxpt.fish.library.util.Toaster;
import com.ypcxpt.fish.main.adapter.DetectedDeviceAdapter;
import com.ypcxpt.fish.main.contract.MyDeviceContract;
import com.ypcxpt.fish.main.event.OnBluetoothPreparedEvent;
import com.ypcxpt.fish.app.util.RippleLayout;
import com.ypcxpt.fish.main.event.OnGetDevicesEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.ypcxpt.fish.core.ble.BLEHelper.MAX_SCAN_TIME;
import static com.ypcxpt.fish.core.ble.output.data.DeviceConstant.TARGET_CHARACTERISTIC_ID;

public class MyDevicePresenter extends BasePresenter<MyDeviceContract.View> implements MyDeviceContract.Presenter {
    private Disposable mScanDisposable;

    private DetectedDeviceAdapter mAdapter;

    private DataSource mDS;

    public MyDevicePresenter() {
        mDS = new DataRepository();
    }

    /**
     * 先检查蓝牙状态，开启后，会发送{@link OnBluetoothPreparedEvent}
     */
    @Override
    public void checkBluetoothState() {
        BLEHelper.checkBluetoothState(getActivity(), () -> {
            /* 蓝牙已开启 */
            EventBus.getDefault().post(new OnBluetoothPreparedEvent());
        });
    }

    /**
     * 开始扫描
     *
     * @param continueScanning 如果正在扫描，是否继续扫描.
     */
    @Override
    public void startScan(boolean continueScanning) {
        if (isScanning()) {
            if (continueScanning) {
                /* 继续当前扫描 */
                return;
            } else {
                /* 停止当前扫描 */
                stopScan();
            }
        }

        ParcelUuid uuid = ParcelUuid.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
//        ParcelUuid uuid = ParcelUuid.fromString("000018f0-0000-1000-8000-00805f9b34fb");

        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build();

        ScanFilter scanFilter = new ScanFilter.Builder()
//                .setServiceUuid(uuid)
                .build();

        mScanDisposable = BLEClient.get().scanBleDevices(scanSettings, scanFilter)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(this::clearScanDisposable)
                .subscribe(mAdapter::addScanResult, this::onScanFailure);

        ThreadHelper.postDelayed(() -> {
            stopScan();
        }, MAX_SCAN_TIME);
    }

    private boolean isScanning() {
        return mScanDisposable != null;
    }

    private void stopScan() {
        if (mScanDisposable != null) {
            mScanDisposable.dispose();
            clearScanDisposable();
            mView.onStopScan();
        }
    }

    private void clearScanDisposable() {
        mScanDisposable = null;
    }

    private void onScanFailure(Throwable throwable) {
        mView.onScanFailure();
        if (throwable instanceof BleScanException) {
            BleScanException scanException = (BleScanException) throwable;
            String text;

            switch (scanException.getReason()) {
                case BleScanException.BLUETOOTH_NOT_AVAILABLE:
                    text = "蓝牙不可用";
                    break;
                case BleScanException.BLUETOOTH_DISABLED:
                    text = "请开启蓝牙收重试";
                    break;
                case BleScanException.LOCATION_PERMISSION_MISSING:
                    text = "Android6.0之后需要开启定位权限";
                    break;
                case BleScanException.LOCATION_SERVICES_DISABLED:
                    text = "Android6.0之后需要开启定位权限";
                    break;
                case BleScanException.SCAN_FAILED_ALREADY_STARTED:
                    text = "同样筛选条件的扫描已经开始";
                    break;
                case BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                    text = "Failed to register application for bluetooth scan";
                    break;
                case BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED:
                    text = "Scan with specified parameters is not supported";
                    break;
                case BleScanException.SCAN_FAILED_INTERNAL_ERROR:
                    text = "Scan failed due to internal error";
                    break;
                case BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES:
                    text = "Scan cannot start due to limited hardware resources";
                    break;
                case BleScanException.UNDOCUMENTED_SCAN_THROTTLE:
                    text = String.format(
                            Locale.getDefault(),
                            "Android 7+ does not allow more scans. Try in %d seconds",
                            secondsTill(scanException.getRetryDateSuggestion())
                    );
                    break;
                case BleScanException.UNKNOWN_ERROR_CODE:
                case BleScanException.BLUETOOTH_CANNOT_START:
                default:
                    text = "Unable to start scanning";
                    break;
            }
            Toaster.showLong(text);
        }
    }

    private long secondsTill(Date retryDateSuggestion) {
        return TimeUnit.MILLISECONDS.toSeconds(retryDateSuggestion.getTime() - System.currentTimeMillis());
    }

    Dialog dialog;
    private void showDialog() {
        dialog = new Dialog(getActivity(), R.style.MyDialog);
        dialog.setCancelable(false);
        dialog.show();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View viewDialog = inflater.inflate(R.layout.dialog_layout_search, null);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        dialog.setContentView(viewDialog, layoutParams);
        RippleLayout ripple_layout = viewDialog.findViewById(R.id.ripple_layout);
        ripple_layout.startRippleAnimation();
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
        showDialog();
        bleDevice = BLEClient.get().getBleDevice(macAddress);

        disposable = bleDevice.establishConnection(false)
                .flatMapSingle(RxBleConnection::discoverServices)
                .take(1) // Disconnect automatically after discovery
                .compose(getActivity().bindUntilEvent(ActivityEvent.PAUSE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(services -> {
                    isBleConnecting = true;
//                    mView.dismissLoading();
                    dialog.dismiss();
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
        dialog.dismiss();
        Logger.d("CCC", throwable.toString());
        Toaster.showLong("连接不到设备，请重试");
        isBleConnecting = true;
    }

    @Override
    public void addDevice(NetDevice device) {
        Flowable<Object> source = mDS.addDevice(device);
        silenceFetch(source)
                .onSuccess(o -> Logger.d("CCC", "添加成功"))
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void startCodeScan(String type, String macAddress) {
        mAdapter.clearScanResults();

        String deviceType = "HOME-8";
        if ("home8".equals(type)) {
            deviceType = "HOME-8";
        }
        mAdapter.addCodeScanResult(deviceType, macAddress);
    }

    @Override
    public void removeDevice(NetDevice device) {
        Flowable<Object> source = mDS.removeDevice(device);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "移除成功");
                    EventBus.getDefault().post(new OnGetDevicesEvent());
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void renameDevice(NetDevice device) {
        Flowable<Object> source = mDS.renameDevice(device);
        silenceFetch(source)
                .onSuccess(o -> {
                    Logger.d("CCC", "重命名成功");
                    EventBus.getDefault().post(new OnGetDevicesEvent());
                })
                .onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();
    }

    @Override
    public void getDevices() {
        Flowable<List<NetDevice>> source = mDS.getDevices();
        new Fetcher<>(source)
                .withView(mView)
                .showLoading(false)
                .showNoNetWarning(false)
        .onSuccess(devices -> {
            Logger.d("CCC", "devices-->" + devices.toString());
            mView.showDevices(devices);
        }).onBizError(bizMsg -> Logger.d("CCC", bizMsg.toString()))
                .onError(throwable -> Logger.d("CCC", throwable.toString()))
                .start();

//        String url = "http://47.96.146.0:9004/app/get_devices";
//        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Logger.e("get_devices", "Json-->" + response);
//
//                Gson gson = new Gson();
////                VersionDetailInfo versionDetailInfo = gson.fromJson(response, VersionDetailInfo.class);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("", "");
//                return map;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new LinkedHashMap<>();
//                // 自定义请求头 user-token:AEUHY98QIASUDH
//                headers.put("authorization", AppData.token());
//                return headers;
//            }
//        };
//        mQueue.add(stringRequest);
    }

    @Override
    public void skipDetail(NetDevice device) {
        Router.build(Path.Main.DEVICE_MANAGER_DETAIL)
                .withParcelable("mDevice", device)
                .navigation(getActivity());
    }

    @Override
    public void onDestroy() {
        isBleConnecting = false;
        stopScan();
        super.onDestroy();
    }

}
