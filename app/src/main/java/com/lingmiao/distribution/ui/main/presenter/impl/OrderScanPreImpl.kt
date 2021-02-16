package com.lingmiao.distribution.ui.main.presenter.impl

import com.google.zxing.BarcodeFormat
import com.lingmiao.distribution.ui.main.api.DispatchRepository
import com.lingmiao.distribution.ui.main.presenter.IOrderScanPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch
import java.util.*

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
class OrderScanPreImpl(private var view : IOrderScanPresenter.View) : BasePreImpl(view),
    IOrderScanPresenter {
    override fun getScanBarcodeFormats(): List<BarcodeFormat> {
        return Arrays.asList(
            BarcodeFormat.QR_CODE,
            BarcodeFormat.CODABAR,
            BarcodeFormat.CODE_39,
            BarcodeFormat.CODE_93,
            BarcodeFormat.CODE_128,
            BarcodeFormat.DATA_MATRIX,
            BarcodeFormat.EAN_8,
            BarcodeFormat.EAN_13,
            BarcodeFormat.ITF,
            BarcodeFormat.MAXICODE,
            BarcodeFormat.PDF_417,
            BarcodeFormat.RSS_14,
            BarcodeFormat.RSS_EXPANDED,
            BarcodeFormat.UPC_A,
            BarcodeFormat.UPC_E,
            BarcodeFormat.UPC_EAN_EXTENSION,
            BarcodeFormat.AZTEC
        );
    }

    override fun getOrderByUpNo(id: String) {
        mCoroutine.launch {
            val resp = DispatchRepository.getOrderDetailByUpsBillNo(id);
            if(resp?.isSuccess) {
                if(resp?.data?.isRespSuccess() == true) {
                    view?.toOrderDetail(resp?.data?.data);
                } else {
                    view?.showToast(resp?.data?.message);
                    view?.toResetBarCodeScan();
                }
            }
        }
    }

}