package com.lingmiao.distribution.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.view.KeyEvent
import android.view.WindowManager
import com.blankj.utilcode.util.ActivityUtils
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.lingmiao.distribution.R
import com.lingmiao.distribution.ui.activity.HomeDetailActivity
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.presenter.IOrderScanPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.OrderScanPreImpl
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.gone
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.android.synthetic.main.main_activivty_order_scan.*

/**
Create Date : 2021/1/19:45 PM
Auther      : Fox
Desc        :
 **/
class OrderScanActivity : BaseActivity<IOrderScanPresenter>(), IOrderScanPresenter.View {

    private var beepManager: BeepManager? = null

    private var isLighted = false

    private var lastText: String? = "";

    override fun createPresenter(): IOrderScanPresenter {
        return OrderScanPreImpl(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activivty_order_scan;
    }

    override fun useBaseLayout(): Boolean {
        return false;
    }

    override fun initView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        iv_back.setOnClickListener {
            finish();
        }
        iv_order_verify.setOnClickListener {
            if (et_order_name.text.toString().length > 0) {
                queryOrder(et_order_name.text.toString());
            }
        }
        initBarCode();
    }


    private fun initBarCode() {
        beepManager = BeepManager(this);

        iv_light.setOnClickListener { v ->
            if (isLighted) {
                zxing_barcode_scanner?.setTorchOff()
            } else {
                zxing_barcode_scanner?.setTorchOn()
            }
            iv_light.setImageResource(if (isLighted) R.mipmap.light_off else R.mipmap.light_on)
            isLighted = !isLighted
        }
        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            iv_light?.gone();
        }
        zxing_barcode_scanner.setTorchListener(torchListener);

        zxing_barcode_scanner.getBarcodeView().setDecoderFactory(DefaultDecoderFactory(mPresenter?.getScanBarcodeFormats()))
        zxing_barcode_scanner.initializeFromIntent(intent)
        zxing_barcode_scanner.decodeContinuous(callback)
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private fun hasFlash(): Boolean {
        return applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    var torchListener = object : DecoratedBarcodeView.TorchListener {

        override fun onTorchOn() {
            iv_light.setImageResource(R.mipmap.light_off)
        }

        override fun onTorchOff() {

            iv_light.setImageResource(R.mipmap.light_on)
        }

    }

    private val callback: BarcodeCallback = object : BarcodeCallback {

        override fun barcodeResult(result: BarcodeResult) {
            zxing_barcode_scanner.pauseAndWait();

            if (result.text == null  || result.text.length == 0) {
                toResetBarCodeScan();
                return
            }
            if(result.text == lastText) {
                showToast(String.format("%s已扫描", result.text));
                toResetBarCodeScan();
                return;
            }
            lastText = result.getText()
            beepManager?.playBeepSoundAndVibrate()
            queryOrder(lastText!!);
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {

        }
    }

    fun queryOrder(id : String) {
        mPresenter?.getOrderByUpNo(id);
    }

    override fun toOrderDetail(item: DispatchOrderRecordBean?) {
        toResetBarCodeScan();
        item?.apply {
            if(id?.isNotBlank() == true) {
//                val intent = Intent(context, HomeDetailActivity::class.java)
//                intent.putExtra("id", id!!);
//                ActivityUtils.startActivity(intent);
                DispatchDetailActivity.open(context!!, id!!, intent?.getIntExtra("type", -1)!!);
                finish();
            }
        }
    }

    override fun toResetBarCodeScan() {
        zxing_barcode_scanner?.resume();
    }

    override fun onResume() {
        super.onResume()
        toResetBarCodeScan();
    }

    override fun onPause() {
        super.onPause()
        if (zxing_barcode_scanner != null) {
            zxing_barcode_scanner.pause()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        return mBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
        return if (zxing_barcode_scanner != null) {
            zxing_barcode_scanner.onKeyDown(keyCode, event)
        } else super.onKeyDown(keyCode, event)
    }
}