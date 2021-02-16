package com.lingmiao.distribution.ui.main.presenter.impl

import com.amap.api.mapcore.util.it
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.common.api.CommonRepository
import com.lingmiao.distribution.ui.common.bean.ListTypeReq
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.OrderBatchSignReq
import com.lingmiao.distribution.ui.main.bean.OrderSignFailedReq
import com.lingmiao.distribution.ui.main.presenter.IDispatchOptionPresenter
import com.lingmiao.distribution.ui.main.presenter.IOperateSignFailPresenter
import com.lingmiao.distribution.ui.main.presenter.IOrderOptionPresenter
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
open class OperateSignFailPreImpl(open var view : IOperateSignFailPresenter.View) : BasePreImpl(view),
    IOperateSignFailPresenter {

    private val orderOptionPresenter: IOrderOptionPresenter by lazy {
        OrderOptionPreImpl(view);
    }

    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

    override fun getReasonList() {
        mCoroutine.launch {
            view?.showDialogLoading()
//            var req = ListTypeReq()
//            req.type = "sign_fail_type";
            val resp = CommonRepository.queryListByType("sign_fail_type");
            handleResponse(resp, {
                view?.setReasonList(resp?.data?.data)
            }, {
                view?.showToast(resp?.data?.message)
                view?.getReasonFail()
            })
            view?.hideDialogLoading()
        }
    }

    override fun signFail(
        id: String?,
        type: Int?,
        mRefuseAcceptType: String?,
        mRefuseAcceptTypeName: String?,
        reason: String
    ) {
        val req = OrderSignFailedReq();
        req.explainReason = reason
        req.signFailType = mRefuseAcceptType
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS)
        req.longitude = Constant.LOCATIONLONGITUDE
        req.latitude = Constant.LOCATIONLATITUDE
        if(type == DispatchConstants.TYPE_DISPATCH) {
            req.dispatchId = id;
            dispatchOptionPresenter?.signFail(req, {
                view?.showToast(it?.message)
                view?.signed()
            }, {

            })
        } else {
            req.orderId = id
            orderOptionPresenter?.signFail(req, {
                view?.showToast(it?.message)
                view?.signed()
            }, {

            })
        }
    }

    override fun batchSignFail(
        ids: ArrayList<String>?,
        reasonType: String?,
        reasonName: String?,
        toString: String
    ) {
        val req = OrderBatchSignReq();
        req.explainReason = reasonName
        req.signFailType = reasonType
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS)
        req.longitude = Constant.LOCATIONLONGITUDE
        req.latitude = Constant.LOCATIONLATITUDE
        req.orderIds = ids;
        orderOptionPresenter?.batchSignFail(req, {
            view?.showToast(it?.message)
            view?.signed()
        }, {

        })
    }


}