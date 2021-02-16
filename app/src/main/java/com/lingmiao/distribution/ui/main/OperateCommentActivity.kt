package com.lingmiao.distribution.ui.main

import com.lingmiao.distribution.R
import com.lingmiao.distribution.ui.main.presenter.IOperateCommentPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.OperateCommentPreImpl
import com.james.common.base.BaseActivity

/**
Create Date : 2021/1/77:30 PM
Auther      : Fox
Desc        : 评价
 **/
class OperateCommentActivity : BaseActivity<IOperateCommentPresenter>(), IOperateCommentPresenter.View {
    override fun createPresenter(): IOperateCommentPresenter {
        return OperateCommentPreImpl(this)
    }

    override fun initView() {

    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_operate_comment;
    }
}