package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.bean.HomeModelEvent
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean
import com.lingmiao.distribution.ui.main.bean.VieOrder

/**
Create Date : 2020/12/2710:40 AM
Auther      : Fox
Desc        :
 **/
interface IOrderViePresenter : BasePresenter {

    fun loadList(page : IPage, list : List<*>, event : HomeModelEvent);

    fun getStatusList() : MutableList<Int>;

    fun itemOptionClick(item : DispatchOrderItemBean);

    fun batchOptionClick(ids : ArrayList<String>);

    fun sureOrder(id : String, successCallback: (DataVO<Unit>) -> Unit, failedCallback: () -> Unit)

    fun takeOrder(id : String, successCallback: (DataVO<Unit>) -> Unit, failedCallback: () -> Unit)

    interface View : BaseView, BaseLoadMoreView<DispatchOrderItemBean> {
        fun onItemOptionSuccess();
    }
}
