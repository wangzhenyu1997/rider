package com.lingmiao.distribution.ui.main.api

import com.lingmiao.distribution.base.IConstant
import com.lingmiao.distribution.base.bean.PageVO
import com.lingmiao.distribution.base.bean.BasePageReqVO
import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.bean.UpdateBean
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.net.Fetch
import com.lingmiao.distribution.ui.main.bean.*
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import java.util.*

object DispatchRepository {

    private val apiService by lazy {
        Fetch.createService(DispatchApiService::class.java)
    }

    /**
     * 调度单列表
     */
    suspend fun queryDispatchList(req : BasePageReqVO<DispatchListReq>): HiResponse<DataVO<PageVO<DispatchOrderRecordBean>>> {
        return apiService.queryDispatchList(req).awaitHiResponse();
    }

    suspend fun queryDispatchList(req : DispatchListReq): HiResponse<DataVO<PageVO<DispatchOrderRecordBean>>> {
        val body = BasePageReqVO<DispatchListReq>();
        body.body = req;
        body.pageNum = req.pageNum;
        body.pageSize = IConstant.PAGE_SIZE_DEFAULT;
        return apiService.queryDispatchList(body).awaitHiResponse();
    }

    /**
     * 调度单列表
     */
    suspend fun queryOrderListByRiderId(pageNum : Int): HiResponse<DataVO<PageVO<DispatchOrderItemBean>>> {
        val req = BasePageReqVO<DispatchListReq>();
        req.pageSize = IConstant.PAGE_SIZE_DEFAULT;
        req.pageNum = pageNum;
        return apiService.queryOrderListByRiderId(req).awaitHiResponse();
    }

    /**
     * 调度单列表
     */
    suspend fun queryDispatchList(pageNum : Int, mState : MutableList<Int>, mListModel : HomeModelEvent?, remarks : String?): HiResponse<DataVO<PageVO<DispatchOrderRecordBean>>> {
        var resq = DispatchListReq();
        resq.dispatchStatusArray = mState;
        resq.longitude = Constant.LOCATIONLONGITUDE;
        resq.latitude = Constant.LOCATIONLATITUDE;
        resq.workStatus = mListModel?.workStatus;
        resq.pickOrder = mListModel?.pickOrder;
        resq.pickOrderSort = mListModel?.pickOrderSort;
        resq.deliveryOrder = mListModel?.deliveryOrder;
        resq.deliveryOrder = mListModel?.deliveryOrderSort;
        resq.remarks = remarks;

        val req = BasePageReqVO<DispatchListReq>();
        req.body = resq;
        req.pageSize = IConstant.PAGE_SIZE_DEFAULT;
        req.pageNum = pageNum;
        return queryDispatchList(req);
    }

    /**
     * 调度单列表
     */
    suspend fun queryDispatchList(pageNum : Int, mState : MutableList<Int>, mListModel : HomeModelEvent?): HiResponse<DataVO<PageVO<DispatchOrderRecordBean>>> {
        return queryDispatchList(pageNum, mState, mListModel, null);
    }

    suspend fun queryDispatchById(id : String): HiResponse<DataVO<DispatchOrderRecordBean>> {
        val map = mutableMapOf<String, String>()
        map.put("id", id)
        return apiService.queryDispatchById(map).awaitHiResponse();
    }

    suspend fun queryOrderListByPickOrderFlag(id : String, req : DispatchListReq?): HiResponse<DataVO<DispatchOrderRecordBean>> {
        var data = OrderFlagVo();
        data.convert(req)
        data.pickOrderFlag = id;

        val body = BasePageReqVO<OrderFlagVo>();
        body.body = data;
        body.pageSize = req?.pageSize
        body.pageNum = req?.pageNum
        return apiService.queryOrderListByPickOrderFlag(body).awaitHiResponse();
    }

    suspend fun loadDispatchNumber(mState : MutableList<Int>?, mListModel : HomeModelEvent?): HiResponse<DataVO<DispatchNumberBean>> {
        var resq = DispatchListReq();
//        resq.dispatchStatusArray = mState;
        resq.longitude = Constant.LOCATIONLONGITUDE;
        resq.latitude = Constant.LOCATIONLATITUDE;
        resq.workStatus = mListModel?.workStatus;
        resq.pickOrder = mListModel?.pickOrder;
        resq.pickOrderSort = mListModel?.pickOrderSort;
        resq.deliveryOrder = mListModel?.deliveryOrder;
        resq.deliveryOrder = mListModel?.deliveryOrderSort;

        val body = ReqBodyVo<DispatchListReq>();
        body.body = resq;
        return apiService.loadDispatchNumber(body).awaitHiResponse();
    }

    suspend fun updateWorkStatus(status : Int): HiResponse<DataVO<Unit>> {
        val map = mutableMapOf<String, Int>()
        map.put("workStatus", status)
        return apiService.updateWorkStatus(map).awaitHiResponse();
    }

    suspend fun agreeAccept(id : String): HiResponse<DataVO<Unit>> {
        val map = mutableMapOf<String, String>()
        map.put("id", id)
        return apiService.agreeAccept(map).awaitHiResponse();
    }

    suspend fun assignAndAccept(ids : MutableList<String>): HiResponse<DataVO<Unit>> {
        var data = TakeOrderReq();
        data.ids = ids;
        return apiService.assignAndAccept(data).awaitHiResponse();
    }

    suspend fun refuse(refuseOrderReq: RefuseOrderReq): HiResponse<DataVO<Unit>> {
        return apiService.refuse(refuseOrderReq).awaitHiResponse();
    }

    suspend fun arriveShop(req : OrderArriveShopReq): HiResponse<DataVO<Unit>> {
        return apiService.arriveShop(req).awaitHiResponse();
    }

    suspend fun pickup(req : OrderPickReq): HiResponse<DataVO<Unit>> {
        return apiService.pickup(req).awaitHiResponse();
    }

    suspend fun pickFail(req : OrderPickFailedReq): HiResponse<DataVO<Unit>> {
        return apiService.pickFail(req).awaitHiResponse();
    }

    suspend fun arriveStation(req : OrderArriveStationReq): HiResponse<DataVO<Unit>> {
        return apiService.arriveStation(req).awaitHiResponse();
    }

    suspend fun signed(req : OrderSignReq): HiResponse<DataVO<Unit>> {
        return apiService.signed(req).awaitHiResponse();
    }

    suspend fun signFail(req : OrderSignFailedReq): HiResponse<DataVO<Unit>> {
        return apiService.signFail(req).awaitHiResponse();
    }

    suspend fun uploadException(body: OrderExceptionReq): HiResponse<DataVO<Unit>> {
        return apiService.uploadException(body).awaitHiResponse();
    }

    suspend fun getOrderByUpsBillNo(id : String): HiResponse<DataVO<DispatchOrderRecordBean>> {
        val map = mutableMapOf<String, String>()
        map.put("upsBillNo", id)
        return apiService.getOrderByUpsBillNo(map).awaitHiResponse();
    }

    suspend fun getOrderDetailByUpsBillNo(id : String): HiResponse<DataVO<DispatchOrderRecordBean>> {
        val map = mutableMapOf<String, String>()
        map.put("upsBillNo", id)
        return apiService.getOrderDetailsByUpsBillNo(map).awaitHiResponse();
    }

    suspend fun queryOrderById(id : String): HiResponse<DataVO<OrderDetail>> {
        val map = mutableMapOf<String, String>()
        map.put("id", id)
        return apiService.queryOrderById(map).awaitHiResponse();
    }

    suspend fun upgrade(version : String) : HiResponse<DataVO<UpdateBean>> {
        val mMap: MutableMap<String, String> = HashMap()
        mMap["appType"] = "1";
        mMap["currentVersion"] = version;
        return apiService.upgrade(mMap).awaitHiResponse();
    }

    /**
     * 修改接单设置
     */
    suspend fun updateSetting(data : HomeModelEvent) : HiResponse<DataVO<Unit>> {
        return apiService.updateSetting(data).awaitHiResponse();
    }

    /**
     * 修改接单设置
     */
    suspend fun querySetting() : HiResponse<DataVO<TakingSettingBean>> {
        return apiService.querySetting().awaitHiResponse();
    }

}