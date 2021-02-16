package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

/**
Create Date : 2021/1/45:09 PM
Auther      : Fox
Desc        :
 **/
interface IHistoryListPresenter : BasePresenter {

    fun loadList(page : IPage, list : List<*>);

    fun getStatusList(): MutableList<Int>

    interface View : BaseView, BaseLoadMoreView<DispatchOrderRecordBean> {

    }

}