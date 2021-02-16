package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.ui.main.bean.*
import com.james.common.base.BasePresenter

/**
Create Date : 2020/12/3012:30 AM
Auther      : Fox
Desc        :
 **/
interface IDispatchOptionPresenter : BasePresenter {

    fun changeViewType(list : List<DispatchOrderRecordBean>, event : HomeModelEvent);

    /**
     * 确认接单
     */
    fun sureOrder(id : String, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 拒绝接单
     */
    fun refuseOrder(refuseOrderReq: RefuseOrderReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 到达取货点
     */
    fun arriveShop(req: OrderArriveShopReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 取货成功
     */
    fun pickSuccess(req: OrderPickReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 取货失败
     */
    fun pickFail(req: OrderPickFailedReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 到达收货点
     */
    fun arriveStation(req: OrderArriveStationReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 签收成功
     */
    fun signSuccess(req: OrderSignReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 签收失败
     */
    fun signFail(req : OrderSignFailedReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     *  异常
     */
    fun exception(req : OrderExceptionReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)
}