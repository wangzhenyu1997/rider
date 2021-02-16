package com.lingmiao.distribution.ui.main

import com.lingmiao.distribution.R
import com.lingmiao.distribution.ui.main.presenter.IOperateFeedbackPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.OperateFeedbackPreImpl
import com.james.common.base.BaseActivity

/**
Create Date : 2021/1/77:31 PM
Auther      : Fox
Desc        :
 **/
class OperateFeedbackActivity : BaseActivity<IOperateFeedbackPresenter>(), IOperateFeedbackPresenter.View {
    override fun createPresenter(): IOperateFeedbackPresenter {
        return OperateFeedbackPreImpl(this)
    }

    override fun initView() {

    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_operate_feedback
    }
}