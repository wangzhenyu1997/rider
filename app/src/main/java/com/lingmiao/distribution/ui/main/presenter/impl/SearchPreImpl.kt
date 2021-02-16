package com.lingmiao.distribution.ui.main.presenter.impl

import android.content.Context
import android.view.View
import com.fisheagle.mkt.base.IConstant
import com.lingmiao.distribution.base.bean.BasePageReqVO
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.main.api.DispatchRepository
import com.lingmiao.distribution.ui.main.bean.*
import com.lingmiao.distribution.ui.main.pop.StatusMenuPop
import com.lingmiao.distribution.ui.main.presenter.IDispatchOptionPresenter
import com.lingmiao.distribution.ui.main.presenter.IOrderOptionPresenter
import com.lingmiao.distribution.ui.main.presenter.ISearchPresenter
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import kotlinx.coroutines.launch

/**
Create Date : 2021/1/208:55 AM
Auther      : Fox
Desc        :
 **/
class SearchPreImpl(context : Context, val view: ISearchPresenter.StatusView) : BasePreImpl(view), ISearchPresenter {

    private val statusPreImpl : StatusPreImpl by lazy {
        StatusPreImpl(context, view);
    }

    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

    private val orderOptionPresenter : IOrderOptionPresenter by lazy {
        OrderOptionPreImpl(view);
    }

    override fun loadList(
        status: Int,
        page: IPage,
        remarks: String,
        datas: List<*>,
        event: HomeModelEvent
    ) {
        mCoroutine.launch {
            if (page.getPageIndex() <= 1 || datas.isEmpty()) {
                view?.showPageLoading()
            }

            var resq = DispatchListReq();
            resq.dispatchStatusArray = getStatusList(status);
            resq.longitude = Constant.LOCATIONLONGITUDE;
            resq.latitude = Constant.LOCATIONLATITUDE;
            resq.workStatus = event?.workStatus;
            resq.pickOrder = event?.pickOrder;
            resq.pickOrderSort = event?.pickOrderSort;
            resq.deliveryOrder = event?.deliveryOrder;
            resq.deliveryOrder = event?.deliveryOrderSort;
            resq.remarks = remarks;

            val req = BasePageReqVO<DispatchListReq>();
            req.body = resq;
            req.pageSize = IConstant.PAGE_SIZE_DEFAULT;
            req.pageNum = page.getPageIndex();

            val resp = DispatchRepository.queryDispatchList(req);

            if (resp.isSuccess) {
                val list = resp?.data?.data?.records ?: listOf();
                list?.forEachIndexed { index, item ->
                    item.req = resq;
                }
                view?.onLoadMoreSuccess(
                    list,
                    page.getPageIndex() < resp?.data?.data?.totalPages ?: 0
                )
            } else {
                view?.onLoadMoreFailed()
            }
            if (page.getPageIndex() <= 1 || datas.isEmpty()) {
                view?.hidePageLoading()
            }
        }
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
        } else if(item?.dispatchStatus == DispatchOrderRecordBean.WAIT_ARRIVE_SHOP) {
            val req = OrderArriveShopReq();
            req.dispatchId = item?.id;
            req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
            req.latitude = Constant.LOCATIONLATITUDE;
            req.longitude = Constant.LOCATIONLONGITUDE;

            dispatchOptionPresenter?.arriveShop(req,{
                view?.hideDialogLoading()
                view?.onItemOptionSuccess();
            },{
                view?.hideDialogLoading()
                view?.showToast("操作失败")
            });
        } else if(item?.dispatchStatus == DispatchOrderRecordBean.WAIT_PICKUP) {
            val req = OrderPickReq();
            req.dispatchId = item?.id
            req.address = PublicUtil.isNull(Constant.LOCATIONADDRESS);
            req.latitude = Constant.LOCATIONLATITUDE;
            req.longitude = Constant.LOCATIONLONGITUDE;
            dispatchOptionPresenter?.pickSuccess(req, {
                view?.hideDialogLoading()
                view?.onItemOptionSuccess();
            },{
                view?.hideDialogLoading()
                view?.showToast("操作失败")
            });
        }else if(item?.dispatchStatus == DispatchOrderRecordBean.WAIT_ARRIVE_STATION) {
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

    override fun batchOptionClick(status: Int?, ids: ArrayList<String>) {
        if (DispatchConstants.DISPATCH_STATUS_AGREEING == status) {
            view?.hideDialogLoading()
        } else if (DispatchConstants.DISPATCH_STATUS_TAKING == status) {
            view?.hideDialogLoading()
        } else if (DispatchConstants.DISPATCH_STATUS_DELIVERING == status) {
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

    override fun clickMenuView(target: View) {
        statusPreImpl?.showMenuPop(
            StatusMenuPop.TYPE_AGREEING or StatusMenuPop.TYPE_TAKING or StatusMenuPop.TYPE_DELIVERY,
            target) { menuType ->
            when (menuType) {
                StatusMenuPop.TYPE_AGREEING->{
                    view?.onAgreeingStatus()
                }
                StatusMenuPop.TYPE_TAKING->{
                    view?.onTakingStatus()
                }
                StatusMenuPop.TYPE_DELIVERY->{
                    view?.onDeliveryStatus()
                }
            }
        };
    }


    fun getStatusList(status: Int): MutableList<Int> {
        val mState: MutableList<Int> = mutableListOf()
        if (status == DispatchConstants.DISPATCH_STATUS_AGREEING) {
            mState.add(DispatchOrderRecordBean.WAIT_ARRANGED);
        } else if (status == DispatchConstants.DISPATCH_STATUS_TAKING) {
            mState.add(DispatchOrderRecordBean.WAIT_ARRIVE_SHOP)
            mState.add(DispatchOrderRecordBean.WAIT_PICKUP)
        } else if (DispatchConstants.DISPATCH_STATUS_DELIVERING == status) {
            mState.add(DispatchOrderRecordBean.WAIT_ARRIVE_STATION);
            mState.add(DispatchOrderRecordBean.WAIT_SIGN);
        }
        return mState;
    }

}