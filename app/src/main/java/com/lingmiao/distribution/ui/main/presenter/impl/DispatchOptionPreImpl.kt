package com.lingmiao.distribution.ui.main.presenter.impl

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.main.api.DispatchRepository
import com.lingmiao.distribution.ui.main.bean.*
import com.lingmiao.distribution.ui.main.presenter.IDispatchOptionPresenter
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.james.common.netcore.networking.http.core.HiResponse
import kotlinx.coroutines.launch

/**
Create Date : 2020/12/2810:29 AM
Auther      : Fox
Desc        :
 **/
open class DispatchOptionPreImpl(open val view : BaseView) : BasePreImpl(view),
    IDispatchOptionPresenter {

    override fun changeViewType(list: List<DispatchOrderRecordBean>, event : HomeModelEvent){
        list.forEachIndexed { _, dispatchOrderRecordBean ->
            dispatchOrderRecordBean.viewModelType = if(event.isFourMode) 0 else 1
        }
    }

    /**
     * 确认抢单
     */
    override fun takeOrder(
        id: String,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val ids : MutableList<String> = mutableListOf();
            ids.add(id);
            val resp = DispatchRepository.assignAndAccept(ids);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 确认接单
     */
    override fun sureOrder(id: String, successCallback: (DataVO<Unit>) -> Unit, failedCallback: () -> Unit) {
        mCoroutine.launch {
            val resp = DispatchRepository.agreeAccept(id);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 拒绝接单
     */
    override fun refuseOrder(refuseOrderReq: RefuseOrderReq, successCallback: (DataVO<Unit>) -> Unit, failedCallback: () -> Unit) {
        mCoroutine.launch {
            val resp = DispatchRepository.refuse(refuseOrderReq);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 到达取货点
     */
    override fun arriveShop(req: OrderArriveShopReq, successCallback: (DataVO<Unit>) -> Unit, failedCallback: () -> Unit) {
        mCoroutine.launch {
            val resp = DispatchRepository.arriveShop(req);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 取货成功
     */
    override fun pickSuccess(req: OrderPickReq, successCallback: (DataVO<Unit>) -> Unit, failedCallback: () -> Unit) {
        mCoroutine.launch {
            val resp = DispatchRepository.pickup(req);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 取货失败
     */
    override fun pickFail(
        req: OrderPickFailedReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val resp = DispatchRepository.pickFail(req);
            option(resp, successCallback, failedCallback);
        }
    }


    /**
     * 到达收货点
     */
    override fun arriveStation(
        req: OrderArriveStationReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {

            val resp = DispatchRepository.arriveStation(req);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 签收成功
     */
    override fun signSuccess(req : OrderSignReq, successCallback: (DataVO<Unit>) -> Unit, failedCallback: () -> Unit) {
        mCoroutine.launch {
            val resp = DispatchRepository.signed(req);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 签收失败
     */
    override fun signFail(
        req : OrderSignFailedReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val resp = DispatchRepository.signFail(req);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     *  异常
     */
    override fun exception(
        req: OrderExceptionReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val resp = DispatchRepository.uploadException(req);
            option(resp, successCallback, failedCallback);
        }
    }

    fun option(resp : HiResponse<DataVO<Unit>>, successCallback: (DataVO<Unit>) -> Unit, failedCallback: () -> Unit) {
        if(resp?.isSuccess) {
            if(resp?.data?.isRespSuccess() == true) {
                successCallback.invoke(resp?.data);
            } else {
                if(resp?.data?.message?.isNotBlank() == true) {
                    view?.showToast(resp?.data?.message)
                }
                failedCallback?.invoke();
            }
        }
    }
}