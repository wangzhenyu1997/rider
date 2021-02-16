package com.lingmiao.distribution.ui.main.presenter.impl

import com.amap.api.mapcore.util.it
import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.common.api.CommonRepository
import com.lingmiao.distribution.ui.common.bean.ListTypeReq
import com.lingmiao.distribution.ui.main.api.OrderRepository
import com.lingmiao.distribution.ui.main.bean.*
import com.lingmiao.distribution.ui.main.presenter.*
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
open class OperatePickFailPreImpl(open var view : IOperatePickFailPresenter.View) : BasePreImpl(view),
    IOperatePickFailPresenter {

    private val orderOptionPresenter: IOrderOptionPresenter by lazy {
        OrderOptionPreImpl(view);
    }

    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

    override fun pickFail(
        id: String?,
        type: Int?,
        mRefuseAcceptType: String?,
        mRefuseAcceptTypeName: String?,
        reason: String
    ) {
        val req = OrderPickFailedReq();
        req.explainReason = reason
        req.pickupFailType = mRefuseAcceptType
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS)
        req.longitude = Constant.LOCATIONLONGITUDE
        req.latitude = Constant.LOCATIONLATITUDE
        if(type == DispatchConstants.TYPE_DISPATCH) {
            req.dispatchId = id;
            dispatchOptionPresenter?.pickFail(req, {
                view?.showToast(it?.message)
                view?.picked()
            }, {

            })
        } else {
            req.orderId = id
            orderOptionPresenter?.pickFail(req, {
                view?.showToast(it?.message)
                view?.picked()
            }, {

            })
        }
    }

    override fun getReasonList() {
        mCoroutine.launch {
            view?.showDialogLoading()
//            var req = ListTypeReq()
//            req.type = "pickup_fail_type"
            val resp = CommonRepository.queryListByType("pickup_fail_type");
            handleResponse(resp, {
                view?.setReasonList(resp?.data?.data)
            }, {
                view?.showToast(resp?.data?.message)
                view?.getReasonFail()
            })
            view?.hideDialogLoading()
        }
    }

}