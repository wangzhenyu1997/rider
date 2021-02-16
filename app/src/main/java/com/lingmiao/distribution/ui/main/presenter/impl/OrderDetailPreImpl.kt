package com.lingmiao.distribution.ui.main.presenter.impl

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.main.api.OrderRepository
import com.lingmiao.distribution.ui.main.bean.*
import com.lingmiao.distribution.ui.main.presenter.IDispatchOptionPresenter
import com.lingmiao.distribution.ui.main.presenter.IOrderDetailPresenter
import com.lingmiao.distribution.ui.main.presenter.IOrderOptionPresenter
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
open class OrderDetailPreImpl(open var view : IOrderDetailPresenter.View) : BasePreImpl(view),
    IOrderDetailPresenter {

    private val orderOptionPresenter: IOrderOptionPresenter by lazy {
        OrderOptionPreImpl(view);
    }

    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

    override fun getDetail(id: String) {
        mCoroutine.launch {
            view?.showDialogLoading();
            val resp = OrderRepository.queryOrderById(id);
            handleResponse(resp, {
                view?.getDetailSuccess(resp?.data?.data);
            }, {
                view?.getDetailFailed();
            });
            view?.hideDialogLoading();
        }
    }

    /**
     * 取货失败
     */
    override fun pickFail(
        id: String,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        val req = OrderPickFailedReq();
        req.orderId = id;
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
        req.latitude = Constant.LOCATIONLATITUDE;
        req.longitude = Constant.LOCATIONLONGITUDE;
        orderOptionPresenter?.pickFail(req, successCallback, failedCallback);
    }

    /**
     * 到达收货点
     */
    override fun arriveStation(
        id: String,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        val req = OrderArriveStationReq();
        req.orderId = id;
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
        req.latitude = Constant.LOCATIONLATITUDE;
        req.longitude = Constant.LOCATIONLONGITUDE;
        orderOptionPresenter?.arriveStation(req, successCallback, failedCallback);
    }

    /**
     * 签收失败
     */
    override fun signFail(
        id: String,
        urls: String,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        val req = OrderSignFailedReq();
        req.orderId = id;
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
        req.latitude = Constant.LOCATIONLATITUDE;
        req.longitude = Constant.LOCATIONLONGITUDE;
        req.urls = urls;
        orderOptionPresenter?.signFail(req, successCallback, failedCallback);
    }


}