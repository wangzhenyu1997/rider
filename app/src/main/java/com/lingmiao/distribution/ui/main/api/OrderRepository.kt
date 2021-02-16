package com.lingmiao.distribution.ui.main.api

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.net.Fetch
import com.lingmiao.distribution.ui.main.bean.*
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import retrofit2.Call
import retrofit2.await
import retrofit2.http.Body
import retrofit2.http.POST

object OrderRepository {

    private val apiService by lazy {
        Fetch.createService(OrderApiService::class.java)
    }

    /**
     * 到达取货点
     */
    suspend fun arriveShop(body: OrderArriveShopReq): HiResponse<DataVO<Unit>> {
        return apiService.arriveShop(body).awaitHiResponse()
    }

    /**
     * 取货失败
     */
    suspend fun pickFail(body: OrderPickFailedReq): HiResponse<DataVO<Unit>> {
        return apiService.pickFail(body).awaitHiResponse()
    }

    /**
     * 取货成功
     */
    suspend fun pickup(body: OrderPickReq): HiResponse<DataVO<Unit>> {
        return apiService.pickup(body).awaitHiResponse()
    }

    /**
     * 到达收货点
     */
    suspend fun arriveStation(body: OrderArriveStationReq): HiResponse<DataVO<Unit>> {
        return apiService.arriveStation(body).awaitHiResponse()
    }

    /**
     * 到达收货点
     */
    suspend fun batchArriveStation(body: OrderBatchArriveStationReq): HiResponse<DataVO<Unit>> {
        return apiService.batchArriveStation(body).awaitHiResponse()
    }

    /**
     * 签收失败
     */
    suspend fun signFail(body: OrderSignFailedReq): HiResponse<DataVO<Unit>> {
        return apiService.signFail(body).awaitHiResponse()
    }

    /**
     * 签收成功
     */
    suspend fun signed(req: OrderSignReq): HiResponse<DataVO<Unit>> {
        return apiService.signed(req).awaitHiResponse()
    }

    /**
     * 查订单详情
     */
    suspend fun queryOrderById(id: String): HiResponse<DataVO<OrderDetail>> {
        val map = mutableMapOf<String, String>()
        map.put("id", id)
        return apiService.queryOrderById(map).awaitHiResponse()
    }


    /**
     * 签收失败
     */
    suspend fun batchSignFail(body: OrderBatchSignReq): HiResponse<DataVO<Unit>> {
        return apiService.batchSignFail(body).awaitHiResponse()
    }

    /**
     * 签收成功
     */
    suspend fun batchSigned(body : OrderBatchSignReq): HiResponse<DataVO<Unit>> {
        return apiService.batchSigned(body).awaitHiResponse()
    }

}