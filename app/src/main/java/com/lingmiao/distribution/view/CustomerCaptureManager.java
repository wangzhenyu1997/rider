package com.lingmiao.distribution.view;

import android.app.Activity;

import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class CustomerCaptureManager extends CaptureManager {

    private ScanResult mScanResult;
    public CustomerCaptureManager(Activity activity, DecoratedBarcodeView barcodeView, ScanResult scanResult) {
        super(activity, barcodeView);
        mScanResult = scanResult;
    }

    @Override
    protected void returnResult(BarcodeResult rawResult) {
//        super.returnResult(rawResult);
        if(mScanResult != null) {
            mScanResult.onSuccess(rawResult);
        }
    }

    public interface ScanResult {
        void onSuccess(BarcodeResult rawResult);
    }

}
