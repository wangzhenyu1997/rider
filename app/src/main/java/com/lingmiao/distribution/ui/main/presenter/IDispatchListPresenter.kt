package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.bean.HomeParam
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

/**
Create Date : 2020/12/2710:40 AM
Auther      : Fox
Desc        :
 **/
interface IDispatchListPresenter : BasePresenter {

    fun loadList(page : IPage, list : List<*>, event : HomeModelEvent);

    fun getStatusList() : MutableList<Int>;

    fun itemOptionClick(item : DispatchOrderRecordBean);

    fun batchOptionClick(ids : ArrayList<String>);

    interface View : BaseView, BaseLoadMoreView<DispatchOrderRecordBean> {
        fun onItemOptionSuccess();
    }
}
