package com.lingmiao.distribution.ui.main.presenter

import android.view.View
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

/**
Create Date : 2021/1/208:48 AM
Auther      : Fox
Desc        :
 **/
interface ISearchPresenter : BasePresenter {

    fun loadList(status : Int, page : IPage, remarks : String, list : List<*>, event : HomeModelEvent);

    fun itemOptionClick(item : DispatchOrderRecordBean);

    fun batchOptionClick(status : Int?, ids : ArrayList<String>);

    fun clickMenuView(target: View);

    interface StatusView : BaseView, BaseLoadMoreView<DispatchOrderRecordBean> {
        fun onItemOptionSuccess();
        fun onAgreeingStatus();
        fun onTakingStatus();
        fun onDeliveryStatus();
    }

}