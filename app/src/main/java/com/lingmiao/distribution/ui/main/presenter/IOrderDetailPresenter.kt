package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.bean.OrderDetail
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2021/1/611:57 AM
Auther      : Fox
Desc        :
 **/
interface IOrderDetailPresenter : BasePresenter {

    fun getDetail(id : String);

    /**
     * 取货失败
     */
    fun pickFail(id: String, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 到达收货点
     */
    fun arriveStation(id: String, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 签收失败
     */
    fun signFail(id: String, urls : String, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    interface View : BaseView {
        fun getDetailSuccess(item : OrderDetail?);
        fun getDetailFailed();
    }
}