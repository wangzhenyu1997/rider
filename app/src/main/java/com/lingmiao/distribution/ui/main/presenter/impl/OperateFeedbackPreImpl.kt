package com.lingmiao.distribution.ui.main.presenter.impl

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.config.Constant
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
open class OperateFeedbackPreImpl(open var view : IOperateFeedbackPresenter.View) : BasePreImpl(view),
    IOperateFeedbackPresenter {

    private val orderOptionPresenter: IOrderOptionPresenter by lazy {
        OrderOptionPreImpl(view);
    }

    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }


}