package com.lingmiao.distribution.ui.main.presenter.impl

import com.amap.api.mapcore.util.it
import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.common.api.CommonRepository
import com.lingmiao.distribution.ui.common.bean.ListTypeReq
import com.lingmiao.distribution.ui.common.presenter.IUploadFilePresenter
import com.lingmiao.distribution.ui.common.presenter.impl.UploadFilePreImpl
import com.lingmiao.distribution.ui.main.api.OrderRepository
import com.lingmiao.distribution.ui.main.bean.*
import com.lingmiao.distribution.ui.main.presenter.IDispatchOptionPresenter
import com.lingmiao.distribution.ui.main.presenter.IOperateExceptionPresenter
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
open class OperateExceptionPreImpl(open var view : IOperateExceptionPresenter.View) : BasePreImpl(view),
    IOperateExceptionPresenter {

    private val uploadPresenter : IUploadFilePresenter by lazy {
        UploadFilePreImpl(view);
    }

    private val orderPresenter: IOrderOptionPresenter by lazy {
        OrderOptionPreImpl(view);
    }

    private val dispatchPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

    override fun getExceptionList() {
        mCoroutine.launch {
            view?.showDialogLoading()
//            var req = ListTypeReq()
//            req.type = "exception_type";
            val resp = CommonRepository.queryListByType("exception_type");
            handleResponse(resp, {
                view?.setExceptionList(resp?.data?.data)
            }, {
                view?.showToast(resp?.data?.message)
                view?.getExceptionFail()
            })
            view?.hideDialogLoading()
        }
    }

    override fun uploadFile(path: String) {
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

    override fun submit(req: OrderExceptionReq, type : Int) {
        dispatchPresenter?.exception(req, {
            view?.showToast(it?.message)
            view?.submitSuccess();
        }, {

        })
    }

}