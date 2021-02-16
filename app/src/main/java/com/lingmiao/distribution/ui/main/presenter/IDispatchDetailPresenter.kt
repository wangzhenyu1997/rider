package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.ui.main.bean.DispatchListReq
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.bean.RefuseOrderReq
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2020/12/297:38 PM
Auther      : Fox
Desc        :
 **/
interface IDispatchDetailPresenter : BasePresenter {

    fun loadDetail(id : String, req : DispatchListReq?, type : Int);

    /**
     * 确认接单
     */
    fun sureOrder(id: String, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 拒绝接单
     */
    fun refuseOrder(refuseOrderReq: RefuseOrderReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 到达取货点
     */
    fun arriveShop(id: String, type : Int, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 取货成功
     */
    fun pickup(id: String, type : Int, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 到达收货点
     */
    fun arriveStation(id: String, type : Int, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)


    /**
     * 到达收货点
     */
    fun batchArriveStation(ids: ArrayList<String>?, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 签收成功
     */
    fun signSuccess(id: String, urls : String, type : Int, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 签收失败
     */
    fun signFail(id: String, urls : String, type : Int, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    interface View : BaseView {

        fun loadDetailSuccess(bean : DispatchOrderRecordBean);

        fun loadDetailFail();

        fun optionSuccess();

        fun optionFailed();
    }

}