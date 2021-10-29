package com.lingmiao.distribution.ui.main.presenter.impl

import android.content.Intent
import android.util.Log
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.ui.main.api.DispatchRepository
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.event.DispatchSingleNumberEvent
import com.lingmiao.distribution.ui.main.event.RefreshDispatchStatusEvent
import com.lingmiao.distribution.ui.main.fragment.DispatchTabFragment
import com.lingmiao.distribution.ui.main.presenter.IDispatchListPresenter
import com.lingmiao.distribution.ui.main.presenter.IDispatchOptionPresenter
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.dialog.HomeConfirmDialog
import com.lingmiao.distribution.ui.activity.RejectionActivity
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean
import com.lingmiao.distribution.ui.main.bean.VieOrder
import com.lingmiao.distribution.ui.main.pop.TakeOrderDialog
import com.lingmiao.distribution.ui.main.presenter.IOrderViePresenter
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
Create Date : 2020/12/274:31 PM
Auther      : Fox
Desc        :
 **/
class OrderVieListPreImpl(val view: IOrderViePresenter.View) : BasePreImpl(view) , IOrderViePresenter {


    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

    override fun loadList(page: IPage, datas: List<*>, event : HomeModelEvent) {
        mCoroutine.launch {
            if(datas.isEmpty()) {
                view.showPageLoading()
            }

            val resp = DispatchRepository.queryOrderListByRiderId(page.getPageIndex())
            if (resp.isSuccess) {
                val list = resp.data?.data?.records ?: listOf()
                view.upDateTotalCount(resp.data.data?.totalCount.toString())
                view.onLoadMoreSuccess(list, page.getPageIndex() < resp.data?.data?.totalPages?:0)
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

    override fun getStatusList(): MutableList<Int> {
        val mState: MutableList<Int> = mutableListOf()
        mState.add(DispatchOrderRecordBean.WAIT_ARRANGED);
        return mState;
    }

    override fun itemOptionClick(item: DispatchOrderItemBean) {

    }

    override fun batchOptionClick(ids: ArrayList<String>) {
        view?.hideDialogLoading()
    }

    // 接单
    override fun sureOrder(
        id: String,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        dispatchOptionPresenter.sureOrder(id, successCallback, failedCallback);
    }

    // 抢单
    override fun takeOrder(
        id: String,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        dispatchOptionPresenter.takeOrder(id, successCallback, failedCallback);
    }

}