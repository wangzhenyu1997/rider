package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.bean.BasicListParam
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2021/1/77:34 PM
Auther      : Fox
Desc        :
 **/
interface IOperatePickFailPresenter  : BasePresenter {
    fun pickFail(
        id: String?,
        type: Int?,
        reasonType: String?,
        reasonName: String?,
        reason: String
    )

    fun getReasonList()

    interface View : BaseView {
        fun getReasonFail()
        fun setReasonList(data: List<BasicListParam>?)
        fun picked()

    }
}