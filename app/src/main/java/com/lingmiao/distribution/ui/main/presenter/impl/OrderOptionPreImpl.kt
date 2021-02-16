package com.lingmiao.distribution.ui.main.presenter.impl

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.ui.main.api.OrderRepository
import com.lingmiao.distribution.ui.main.bean.*
import com.lingmiao.distribution.ui.main.presenter.IOrderOptionPresenter
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.james.common.netcore.networking.http.core.HiResponse
import kotlinx.coroutines.launch

/**
Create Date : 2021/1/72:32 PM
Auther      : Fox
Desc        :
 **/
class OrderOptionPreImpl(open val view : BaseView) : BasePreImpl(view) , IOrderOptionPresenter{

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

    /**
     * 到达取货点
     */
    override fun arriveShop(
        req: OrderArriveShopReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val resp = OrderRepository.arriveShop(req);
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
            val resp = OrderRepository.pickFail(req);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 取货成功
     */
    override fun pickup(
        req: OrderPickReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val resp = OrderRepository.pickup(req);
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
            val resp = OrderRepository.arriveStation(req);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 到达收货点
     */
    override fun batchArriveStation(
        req: OrderBatchArriveStationReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val resp = OrderRepository.batchArriveStation(req);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 签收失败
     */
    override fun signFail(
        req: OrderSignFailedReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val resp = OrderRepository.signFail(req);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 签收失败
     */
    override fun batchSignFail(
        req: OrderBatchSignReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val resp = OrderRepository.batchSignFail(req);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 签收成功
     */
    override fun signSuccess(
        req: OrderSignReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val resp = OrderRepository.signed(req);
            option(resp, successCallback, failedCallback);
        }
    }

    /**
     * 签收成功
     */
    override fun batchSigned(
        req: OrderBatchSignReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val resp = OrderRepository.batchSigned(req);
            option(resp, successCallback, failedCallback);
        }
    }
}