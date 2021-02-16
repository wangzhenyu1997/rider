package com.lingmiao.distribution.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.HomeTwoParam;
import com.lingmiao.distribution.bean.ScanOrderBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityScanBinding;
import com.lingmiao.distribution.event.ScanList;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.ToastUtil;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScanActivity extends ActivitySupport {

    private ActivityScanBinding mViewBinding;
    /**
     * 条形码扫描视图
     */
    private DecoratedBarcodeView mBarcodeView;

    private BeepManager beepManager;

    private boolean isLighted = false;

    private List<HomeTwoParam> mList;

    /**
     * 已扫描的所有订单号拼接
     */
    private String orderNos = "";

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        mViewBinding = ActivityScanBinding.inflate(LayoutInflater.from(this));
        setContentView(mViewBinding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        initData();

        initBarCode();

        initTopView();
    }

    private void initData() {
        mList = new ArrayList<>();
        orderNos = getIntent().getStringExtra("orderNosString");
    }

    private void initBarCode() {
        beepManager = new BeepManager(this);

        mBarcodeView = mViewBinding.zxingBarcodeScanner;

        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODABAR, BarcodeFormat.CODE_39, BarcodeFormat.CODE_93, BarcodeFormat.CODE_128, BarcodeFormat.DATA_MATRIX, BarcodeFormat.EAN_8, BarcodeFormat.EAN_13, BarcodeFormat.ITF, BarcodeFormat.MAXICODE, BarcodeFormat.PDF_417, BarcodeFormat.RSS_14, BarcodeFormat.RSS_EXPANDED, BarcodeFormat.UPC_A, BarcodeFormat.UPC_E, BarcodeFormat.UPC_EAN_EXTENSION, BarcodeFormat.AZTEC);

        mBarcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        mBarcodeView.initializeFromIntent(getIntent());
        mBarcodeView.decodeContinuous(mCallback);

        mViewBinding.ivLight.setOnClickListener(v -> {
            if(isLighted) {
                mBarcodeView.setTorchOff();
            } else {
                mBarcodeView.setTorchOn();
            }
            mViewBinding.ivLight.setImageResource(isLighted ? R.mipmap.light_off : R.mipmap.light_on);
            isLighted = !isLighted;
        });
        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            mViewBinding.ivLight.setVisibility(View.GONE);
        }

    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void initTopView() {
        mViewBinding.ivBack.setOnClickListener(v -> {
            finish();
        });
        mViewBinding.btnScanFinish.setOnClickListener(v -> {
            EventBus.getDefault().post(new ScanList(mList));
            finish();
        });
    }

    private BarcodeCallback mCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            mBarcodeView.pauseAndWait();
            if(result.getText() == null || result.getText().length() == 0) {
                mBarcodeView.resume();
                return;
            }
            if(existOrderNo(result.getText()) || existOrder(result.getText())) {
                ToastUtil.showToast(ScanActivity.this, result.getText() + "已扫描");
                mBarcodeView.resume();
                return;
            }

            beepManager.playBeepSoundAndVibrate();
            loadOrder(result.getText());
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    /**
     * 是否已存在扫描列表
     * @param no
     * @return
     */
    private boolean existOrder(String no) {
        if(mList == null || mList.size() == 0) {
            return false;
        }
        for(HomeTwoParam item : mList) {
            if(item == null || item.upsBillNo == null || item.upsBillNo.length() == 0) {
                continue;
            }
            if(item.upsBillNo.equals(no)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否已存在取货扫描列表
     * @param no
     * @return
     */
    private boolean existOrderNo(String no) {
        return orderNos == null || orderNos.length() == 0 ? false : orderNos.indexOf(String.format("%s,", no)) > -1;
    }

    private void loadOrder(String no) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("upsBillNo", no);
        OkHttpUtils.postAync(Constant.QueryOrderByUpsBillNo, new Gson().toJson(mMap), new HttpCallback<ScanOrderBean>(ScanActivity.this, getProgressDialog()) {
            @Override
            public void onSuccess(ScanOrderBean response) {
                super.onSuccess(response);
                if(response != null && response.isResultSuccess()) {
                    if(response.getData() != null) {
                        mList.add(response.getData());
                    }
                    resetScanCount();
                } else {
                    ToastUtil.showToast(ScanActivity.this, response == null || response.getMessage() == null || response.getMessage().length() == 0 ? "获取失败" : response.getMessage());
                }
                mBarcodeView.resume();
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                mBarcodeView.resume();
                ToastUtil.showToast(ScanActivity.this, "获取失败");
            }
        });
    }

    private void resetScanCount() {
        mViewBinding.btnScanFinish.setText(String.format("%s单，完成扫描", mList.size()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBarcodeView != null) {
            mBarcodeView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mBarcodeView != null) {
            mBarcodeView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBarcodeView = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return mBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
        if(mBarcodeView != null) {
            return mBarcodeView.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
