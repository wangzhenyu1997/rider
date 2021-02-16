package com.lingmiao.distribution.ui.main.presenter.impl

import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.main.api.DispatchRepository
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.presenter.IHistoryListPresenter
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
Create Date : 2020/12/274:31 PM
Auther      : Fox
Desc        :
 **/
class HistoryListPreImpl(val view: IHistoryListPresenter.View) : BasePreImpl(view) , IHistoryListPresenter {

    override fun loadList(page: IPage, datas: List<*>) {
        mCoroutine.launch {
            if (page.getPageIndex() <= 1 || datas.isEmpty()) {
                view?.showPageLoading()
            }

            val resp = DispatchRepository.queryDispatchList(page.getPageIndex(), getStatusList(), Constant.Home_Model_Event);
            if (resp.isSuccess) {
                val list = resp?.data?.data?.records ?: listOf();
                view?.onLoadMoreSuccess(list, page.getPageIndex() < resp?.data?.data?.totalPages?:0)
            } else {
                view?.onLoadMoreFailed()
            }

            if (page.getPageIndex() <= 1 || datas.isEmpty()) {
                view?.hidePageLoading()
            }
        }
    }

    override fun getStatusList(): MutableList<Int> {
        val mState: MutableList<Int> = mutableListOf()
        mState.add(DispatchOrderRecordBean.CANCEL)
        mState.add(DispatchOrderRecordBean.REFUSE)
        mState.add(DispatchOrderRecordBean.SIGNED)
        mState.add(DispatchOrderRecordBean.SIGN_FAIL)
        mState.add(DispatchOrderRecordBean.PICKUP_FAIL)
        return mState;
    }

}