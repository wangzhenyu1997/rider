package com.lingmiao.distribution.ui.main.presenter.impl

import com.lingmiao.distribution.ui.main.presenter.*
import com.james.common.base.BasePreImpl

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
open class OperateCommentPreImpl(open var view : IOperateCommentPresenter.View) : BasePreImpl(view),
    IOperateCommentPresenter {

    private val orderOptionPresenter: IOrderOptionPresenter by lazy {
        OrderOptionPreImpl(view);
    }

    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

}