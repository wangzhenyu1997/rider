package com.lingmiao.distribution.ui.main.presenter.impl

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.main.api.DispatchRepository
import com.lingmiao.distribution.ui.main.bean.*
import com.lingmiao.distribution.ui.main.presenter.IDispatchDetailPresenter
import com.lingmiao.distribution.ui.main.presenter.IDispatchOptionPresenter
import com.lingmiao.distribution.ui.main.presenter.IOrderOptionPresenter
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
class DispatchDetailPreImpl(var view : IDispatchDetailPresenter.View) : BasePreImpl(view),
    IDispatchDetailPresenter {

    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

    private val orderOptionPresenter : IOrderOptionPresenter by lazy {
        OrderOptionPreImpl(view);
    }

    override fun loadDetail(id : String, req : DispatchListReq?, type : Int) {
        mCoroutine.launch {
            view?.showDialogLoading()
            if(DispatchConstants.isDeliveringTab(type)) {
                val resp = DispatchRepository.queryOrderListByPickOrderFlag(id, req);
                handleResponse(resp, {
                    if(it?.isSuccessAndData()) {
                        view?.loadDetailSuccess(resp?.data?.data !!);
                    } else {
                        view?.loadDetailFail();
                    }
                }, {});
            } else {
                val resp = DispatchRepository.queryDispatchById(id);
                if (resp.isSuccess && resp?.data != null && resp?.data?.data != null) {
                    view?.loadDetailSuccess(resp?.data?.data !!);
                }
            }

            view?.hideDialogLoading()
        }
    }

    /**
     * 确认接单
     */
    override fun sureOrder(
        id: String,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        dispatchOptionPresenter?.sureOrder(id, successCallback, failedCallback);
    }

    /**
     * 拒绝接单
     */
    override fun refuseOrder(
        req: RefuseOrderReq,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        dispatchOptionPresenter?.refuseOrder(req, successCallback, failedCallback);
    }

    /**
     * 到达取货点
     */
    override fun arriveShop(
        id: String,
        type: Int,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        val req = OrderArriveShopReq();
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
        req.latitude = Constant.LOCATIONLATITUDE;
        req.longitude = Constant.LOCATIONLONGITUDE;
        if(DispatchConstants.TYPE_DISPATCH == type) {
            req.dispatchId = id;
            dispatchOptionPresenter?.arriveShop(req, successCallback, failedCallback)
        } else {
            req.orderId = id;
            orderOptionPresenter?.arriveShop(req, successCallback, failedCallback);
        }
    }

    /**
     * 取货成功
     */
    override fun pickup(
        id: String,
        type: Int,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        val req = OrderPickReq();
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
        req.latitude = Constant.LOCATIONLATITUDE;
        req.longitude = Constant.LOCATIONLONGITUDE;
        if(DispatchConstants.TYPE_DISPATCH == type) {
            req.dispatchId = id;
            dispatchOptionPresenter?.pickSuccess(req, successCallback, failedCallback)
        } else {
            req.orderId = id;
            orderOptionPresenter?.pickup(req, successCallback, failedCallback);
        }
    }

    /**
     * 到达收货点
     */
    override fun arriveStation(
        id: String,
        type: Int,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        val req = OrderArriveStationReq();
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
        req.latitude = Constant.LOCATIONLATITUDE;
        req.longitude = Constant.LOCATIONLONGITUDE;
        if(DispatchConstants.TYPE_DISPATCH == type) {
            req.dispatchId = id;
            dispatchOptionPresenter?.arriveStation(req, successCallback, failedCallback)
        } else {
            req.orderId = id;
            orderOptionPresenter?.arriveStation(req, successCallback, failedCallback);
        }
    }

    /**
     * 到达收货点
     */
    override fun batchArriveStation(
        ids: ArrayList<String>?,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        var req = OrderBatchArriveStationReq();
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
        req.latitude = Constant.LOCATIONLATITUDE;
        req.longitude = Constant.LOCATIONLONGITUDE;
        req.orderIds = ids;
        orderOptionPresenter?.batchArriveStation(req, successCallback, failedCallback)
    }

    /**
     * 签收成功
     */
    override fun signSuccess(
        id: String,
        urls: String,
        type: Int,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        val req = OrderSignReq();
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
        req.latitude = Constant.LOCATIONLATITUDE;
        req.longitude = Constant.LOCATIONLONGITUDE;
        req.urls = urls;
        if(DispatchConstants.TYPE_DISPATCH == type) {
            req.dispatchId = id;
            dispatchOptionPresenter?.signSuccess(req, successCallback, failedCallback)
        } else {
            req.orderId = id;
            orderOptionPresenter?.signSuccess(req, successCallback, failedCallback);
        }
    }

    /**
     * 签收失败
     */
    override fun signFail(
        id: String,
        urls: String,
        type: Int,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        val req = OrderSignFailedReq();
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
        req.latitude = Constant.LOCATIONLATITUDE;
        req.longitude = Constant.LOCATIONLONGITUDE;
        req.urls = urls;
        if(DispatchConstants.TYPE_DISPATCH == type) {
            req.dispatchId = id;
            dispatchOptionPresenter?.signFail(req, successCallback, failedCallback)
        } else {
            req.orderId = id;
            orderOptionPresenter?.signFail(req, successCallback, failedCallback);
        }
    }

}