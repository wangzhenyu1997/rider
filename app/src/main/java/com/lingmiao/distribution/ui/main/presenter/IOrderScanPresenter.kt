package com.lingmiao.distribution.ui.main.presenter

import com.google.zxing.BarcodeFormat
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2020/12/263:43 PM
Auther      : Fox
Desc        :
 **/
interface IOrderScanPresenter : BasePresenter {

    fun getScanBarcodeFormats() : List<BarcodeFormat>;

    fun getOrderByUpNo(id : String);

    interface View : BaseView {

        fun toOrderDetail(item : DispatchOrderRecordBean?);

        fun toResetBarCodeScan();
    }

}