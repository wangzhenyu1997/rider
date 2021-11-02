package com.lingmiao.distribution.ui.main.presenter.impl

import com.lingmiao.distribution.base.IConstant
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.main.api.DispatchRepository
import com.lingmiao.distribution.ui.main.bean.*
import com.lingmiao.distribution.ui.main.presenter.IDispatchListPresenter
import com.lingmiao.distribution.ui.main.presenter.IDispatchOptionPresenter
import com.lingmiao.distribution.ui.main.presenter.IOrderOptionPresenter
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import kotlinx.coroutines.launch

/**
Create Date : 2020/12/274:31 PM
Auther      : Fox
Desc        :
 **/
class DispatchDeliveringListPreImpl(val view: IDispatchListPresenter.View) : BasePreImpl(view) , IDispatchListPresenter {

    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

    private val orderOptionPresenter : IOrderOptionPresenter by lazy {
        OrderOptionPreImpl(view);
    }

    override fun loadList(page: IPage, datas: List<*>, event : HomeModelEvent) {
        mCoroutine.launch {
            if(datas?.isEmpty()) {
                view?.showPageLoading()
            }
            var req = DispatchListReq();
            req.pageNum = page.getPageIndex();
            req.pageSize = IConstant.PAGE_SIZE_DEFAULT;
            req.dispatchStatusArray = getStatusList();
            req.longitude = Constant.LOCATIONLONGITUDE;
            req.latitude = Constant.LOCATIONLATITUDE;
            req.workStatus = event?.workStatus;
            req.pickOrder = event?.pickOrder;
            req.pickOrderSort = event?.pickOrderSort;
            req.deliveryOrder = event?.deliveryOrder;
            req.deliveryOrder = event?.deliveryOrderSort;

            val resp = DispatchRepository.queryDispatchList(req);

            if (resp.isSuccess) {
                val list = resp?.data?.data?.records ?: listOf();
                list?.forEachIndexed { index, item ->
                    item.req = req;
                }
                dispatchOptionPresenter?.changeViewType(list, event);
//                EventBus.getDefault().post(DispatchSingleNumberEvent(DispatchConstants.DISPATCH_STATUS_DELIVERING, resp?.data?.data?.totalCount ?: 0));
                view?.onLoadMoreSuccess(list, page.getPageIndex() < resp?.data?.data?.totalPages?:0)
            } else {
                view?.onLoadMoreFailed()
            }
            view?.hidePageLoading()
        }
    }

    override fun getStatusList(): MutableList<Int> {
        val mState: MutableList<Int> = mutableListOf()
        mState.add(DispatchOrderRecordBean.WAIT_ARRIVE_STATION);
        mState.add(DispatchOrderRecordBean.WAIT_SIGN);
        return mState;
    }

    override fun itemOptionClick(item: DispatchOrderRecordBean) {
        if(item?.dispatchStatus == DispatchOrderRecordBean.WAIT_ARRIVE_STATION) {
            val req = OrderArriveStationReq();
            req.dispatchId = item?.id!!
            req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS)
            req.latitude = Constant.LOCATIONLATITUDE
            req.longitude = Constant.LOCATIONLONGITUDE
            dispatchOptionPresenter?.arriveStation(req,{
                view?.hideDialogLoading()
                view?.onItemOptionSuccess();
            },{
                view?.hideDialogLoading()
                view?.showToast("操作失败")
            });
        } else if(item?.dispatchStatus == DispatchOrderRecordBean.WAIT_SIGN) {
            view?.hideDialogLoading()
        }
    }

    override fun batchOptionClick(ids: ArrayList<String>) {
        var req = OrderBatchArriveStationReq();
        req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
        req.latitude = Constant.LOCATIONLATITUDE;
        req.longitude = Constant.LOCATIONLONGITUDE;
        req.orderIds = ids;
        orderOptionPresenter?.batchArriveStation(req, {
            view?.hideDialogLoading()
            view?.onItemOptionSuccess();
        }, {
            view?.hideDialogLoading()
        })
    }


}