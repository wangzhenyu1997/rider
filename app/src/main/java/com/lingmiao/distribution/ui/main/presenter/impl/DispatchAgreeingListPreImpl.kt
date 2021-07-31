package com.lingmiao.distribution.ui.main.presenter.impl

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
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
Create Date : 2020/12/274:31 PM
Auther      : Fox
Desc        :
 **/
class DispatchAgreeingListPreImpl(val view: IDispatchListPresenter.View) : BasePreImpl(view) , IDispatchListPresenter {


    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

    override fun loadList(page: IPage, datas: List<*>, event : HomeModelEvent) {
        mCoroutine.launch {
            if(datas?.isEmpty()) {
                view?.showPageLoading()
            }

            val resp = DispatchRepository.queryDispatchList(page.getPageIndex(), getStatusList(), event);
            if (resp.isSuccess) {
                val list = resp?.data?.data?.records ?: listOf();
                dispatchOptionPresenter?.changeViewType(list, event);
//                EventBus.getDefault().post(DispatchSingleNumberEvent(DispatchConstants.DISPATCH_STATUS_AGREEING, resp?.data?.data?.totalCount ?: 0));
                view?.onLoadMoreSuccess(list, page.getPageIndex() < resp?.data?.data?.totalPages?:0)
            } else {
                view?.onLoadMoreFailed()
            }
            view?.hidePageLoading()
        }
    }

    override fun getStatusList(): MutableList<Int> {
        val mState: MutableList<Int> = mutableListOf()
        mState.add(DispatchOrderRecordBean.WAIT_ARRANGED);
        return mState;
    }

    override fun itemOptionClick(item: DispatchOrderRecordBean) {
        if(item?.dispatchStatus == DispatchOrderRecordBean.WAIT_ARRANGED) {
            dispatchOptionPresenter?.sureOrder(item?.id!!,{
                view?.hideDialogLoading()
                view?.onItemOptionSuccess();
            },{
                view?.hideDialogLoading()
                view?.showToast("接单失败");
            });
        }
    }

    override fun batchOptionClick(ids: ArrayList<String>) {
        view?.hideDialogLoading()
    }

}