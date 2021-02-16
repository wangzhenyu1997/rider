package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.ui.main.bean.*
import com.james.common.base.BasePresenter

/**
Create Date : 2021/1/72:15 PM
Auther      : Fox
Desc        :
 **/
interface IOrderOptionPresenter : BasePresenter{

    /**
     * 到达取货点
     */
    fun arriveShop(req : OrderArriveShopReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 取货失败
     */
    fun pickFail(req : OrderPickFailedReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 取货成功
     */
    fun pickup(req : OrderPickReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 到达收货点
     */
    fun arriveStation(req : OrderArriveStationReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 到达收货点
     */
    fun batchArriveStation(req : OrderBatchArriveStationReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 签收失败
     */
    fun signFail(req : OrderSignFailedReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 签收成功
     */
    fun signSuccess(req : OrderSignReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 签收失败
     */
    fun batchSignFail(req : OrderBatchSignReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)

    /**
     * 签收成功
     */
    fun batchSigned(req : OrderBatchSignReq, successCallback : (DataVO<Unit>) -> Unit, failedCallback : () -> Unit)
}