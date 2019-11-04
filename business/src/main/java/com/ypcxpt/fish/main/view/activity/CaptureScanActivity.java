package com.ypcxpt.fish.main.view.activity;

import android.content.Intent;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.ypcxpt.fish.library.view.activity.BaseActivity;

import com.ypcxpt.fish.R;
import com.ypcxpt.fish.library.util.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class CaptureScanActivity extends BaseActivity implements QRCodeView.Delegate, View.OnClickListener {

    @BindView(R.id.zxingview)
    ZXingView mZXingView;

    @BindView(R.id.iv_light)
    ImageView iv_light;

    private boolean light = false;

    @Override
    protected ImmersionBar initImmersionBar() {
        return ImmersionBar.with(this).transparentStatusBar().navigationBarColor(R.color.scan);
    }

    @Override
    protected int layoutResID() {
        return R.layout.activity_capture_scan;
    }

    @Override
    protected void initViews() {
        mZXingView.setDelegate(this);
    }

    @OnClick(R.id.rl_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.rl_light)
    public void onLight() {
        if (!light) {
            Logger.e("kkkkkk", "");
            mZXingView.openFlashlight(); // 打开闪光灯
            iv_light.setImageResource(R.mipmap.icon_light_ed);
            light = true;
        } else {
            Logger.e("gggggg", "");
            mZXingView.closeFlashlight(); // 关闭闪光灯
            iv_light.setImageResource(R.mipmap.icon_light_u);
            light = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mZXingView != null) {
            mZXingView.onDestroy(); // 销毁二维码扫描控件
        }
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Logger.i("扫描结果", "result:" + result);
        vibrate();

        mZXingView.startSpot(); // 开始识别

        Intent intent = getIntent();
        intent.putExtra("QRCODE_RESULT", result);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Logger.e("onScanQRCodeOpenCameraError", "打开相机出错");
    }

    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.start_preview:
//                mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//                break;
//            case R.id.stop_preview:
//                mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
//                break;
//            case R.id.start_spot:
//                mZXingView.startSpot(); // 开始识别
//                break;
//            case R.id.stop_spot:
//                mZXingView.stopSpot(); // 停止识别
//                break;
//            case R.id.start_spot_showrect:
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且开始识别
//                break;
//            case R.id.stop_spot_hiddenrect:
//                mZXingView.stopSpotAndHiddenRect(); // 停止识别，并且隐藏扫描框
//                break;
//            case R.id.show_scan_rect:
//                mZXingView.showScanRect(); // 显示扫描框
//                break;
//            case R.id.hidden_scan_rect:
//                mZXingView.hiddenScanRect(); // 隐藏扫描框
//                break;
//            case R.id.decode_scan_box_area:
//                mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
//                break;
//            case R.id.decode_full_screen_area:
//                mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(false); // 识别整个屏幕中的码
//                break;
//            case R.id.open_flashlight:
//                mZXingView.openFlashlight(); // 打开闪光灯
//                break;
//            case R.id.close_flashlight:
//                mZXingView.closeFlashlight(); // 关闭闪光灯
//                break;
//            case R.id.scan_one_dimension:
//                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
//                mZXingView.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_two_dimension:
//                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZXingView.setType(BarcodeType.TWO_DIMENSION, null); // 只识别二维条码
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_qr_code:
//                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZXingView.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_code128:
//                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
//                mZXingView.setType(BarcodeType.ONLY_CODE_128, null); // 只识别 CODE_128
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_ean13:
//                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
//                mZXingView.setType(BarcodeType.ONLY_EAN_13, null); // 只识别 EAN_13
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_high_frequency:
//                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZXingView.setType(BarcodeType.HIGH_FREQUENCY, null); // 只识别高频率格式，包括 QR_CODE、UPC_A、EAN_13、CODE_128
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_all:
//                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZXingView.setType(BarcodeType.ALL, null); // 识别所有类型的码
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_custom:
//                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//
//                Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
//                List<BarcodeFormat> formatList = new ArrayList<>();
//                formatList.add(BarcodeFormat.QR_CODE);
//                formatList.add(BarcodeFormat.UPC_A);
//                formatList.add(BarcodeFormat.EAN_13);
//                formatList.add(BarcodeFormat.CODE_128);
//                hintMap.put(DecodeHintType.POSSIBLE_FORMATS, formatList); // 可能的编码格式
//                hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); // 花更多的时间用于寻找图上的编码，优化准确性，但不优化速度
//                hintMap.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 编码字符集
//                mZXingView.setType(BarcodeType.CUSTOM, hintMap); // 自定义识别的类型
//
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
        }
    }
}