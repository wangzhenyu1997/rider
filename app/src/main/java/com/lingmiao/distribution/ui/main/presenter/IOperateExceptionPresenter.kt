package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.bean.BasicListParam
import com.lingmiao.distribution.ui.common.bean.UploadDataBean
import com.lingmiao.distribution.ui.main.bean.OrderExceptionReq
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.netcore.networking.http.core.HiResponse

/**
Create Date : 2021/1/77:34 PM
Auther      : Fox
Desc        :
 **/
interface IOperateExceptionPresenter  : BasePresenter {

    fun getExceptionList()

    fun uploadFile(path : String)

    fun submit(req : OrderExceptionReq, type : Int)

    interface View : BaseView {

        fun setExceptionList(list : List<BasicListParam>?)

        fun getExceptionFail()

        fun onUploadFileSuccess(path : String, item : UploadDataBean)

        fun submitSuccess()
    }
}