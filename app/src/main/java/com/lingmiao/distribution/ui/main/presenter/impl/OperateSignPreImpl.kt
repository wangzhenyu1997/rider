package com.lingmiao.distribution.ui.main.presenter.impl

import com.amap.api.mapcore.util.it
import com.fisheagle.mkt.base.CommonRepository
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.common.presenter.IUploadFilePresenter
import com.lingmiao.distribution.ui.common.presenter.impl.UploadFilePreImpl
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.OrderBatchSignReq
import com.lingmiao.distribution.ui.main.bean.OrderSignReq
import com.lingmiao.distribution.ui.main.presenter.IDispatchOptionPresenter
import com.lingmiao.distribution.ui.main.presenter.IOrderOptionPresenter
import com.lingmiao.distribution.ui.main.presenter.IOperateSignPresenter
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch
import java.io.File

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
open class OperateSignPreImpl(var view : IOperateSignPresenter.View) : BasePreImpl(view),
    IOperateSignPresenter {

    private val uploadPresenter : IUploadFilePresenter by lazy {
        UploadFilePreImpl(view);
    }

    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

    private val orderOptionPresenter : IOrderOptionPresenter by lazy {
        OrderOptionPreImpl(view);
    }

    override fun sign(id : String, urls : String, type : Int) {
        mCoroutine.launch {

            if(DispatchConstants.TYPE_DISPATCH == type) {
                view?.showDialogLoading()
                val req = OrderSignReq();
                req.dispatchId = id;
                req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
                req.latitude = Constant.LOCATIONLATITUDE;
                req.longitude = Constant.LOCATIONLONGITUDE;
                req.urls = urls;

                dispatchOptionPresenter.signSuccess(req, {
                    view?.hideDialogLoading();
                    view?.showToast(it?.message)
                    view?.signSuccess();
                },{
                    view?.hideDialogLoading();
                    view?.onSignedFail();
//                    view?.showToast("操作失败")
                });
            } else if(DispatchConstants.TYPE_ORDER == type){
                view?.showDialogLoading()
                val req = OrderSignReq();
                req.orderId = id;
                req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
                req.latitude = Constant.LOCATIONLATITUDE;
                req.longitude = Constant.LOCATIONLONGITUDE;
                req.urls = urls;
                orderOptionPresenter.signSuccess(req, {
                    view?.hideDialogLoading();
                    view?.showToast(it?.message)
                    view?.signSuccess();
                },{
                    view?.hideDialogLoading();
                    view?.onSignedFail();
                    //view?.showToast("操作失败")
                });
            }
        }
    }

    override fun batchSign(ids: ArrayList<String>?, urls: String, remarks : String, shortMessageFlag : Int) {
        mCoroutine.launch {
            view?.showDialogLoading()
            val req = OrderBatchSignReq();
            req.remarks = remarks
            req.shortMessageFlag = shortMessageFlag;
            req.signAddress = PublicUtil.isNull(Constant.LOCATIONADDRESS)
            req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS)
            req.longitude = Constant.LOCATIONLONGITUDE
            req.latitude = Constant.LOCATIONLATITUDE
            req.orderIds = ids;
            req.urls = urls;
            orderOptionPresenter?.batchSigned(req, {
                view?.hideDialogLoading()
                view?.showToast(it?.message)
                view?.signSuccess()
            }, {
                view?.hideDialogLoading()
                view?.onSignedFail();
            })
        }
    }

    override fun uploadFile(path : String) {
        uploadPresenter?.uploadFile(path, true, {
            if(it?.isSuccessAndData() == true) {
                view?.onUploadFileSuccess(path, it?.data!!);
            } else {
                view?.showToast(it?.message)
            }
        }, {
            view?.showToast(it)
        });
    }

}