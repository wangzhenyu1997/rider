package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.bean.BasicListParam
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2021/1/77:34 PM
Auther      : Fox
Desc        :
 **/
interface IOperateSignFailPresenter  : BasePresenter {

    fun getReasonList()

    fun signFail(
        id: String?,
        type: Int?,
        mRefuseAcceptType: String?,
        mRefuseAcceptTypeName: String?,
        toString: String
    )

    fun batchSignFail(
        ids: ArrayList<String>?,
        mRefuseAcceptType: String?,
        mRefuseAcceptTypeName: String?,
        toString: String
    )

    interface View : BaseView {
        fun setReasonList(list : List<BasicListParam>?)

        fun getReasonFail()

        fun signed()
    }
}