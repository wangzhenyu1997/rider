package com.lingmiao.distribution.ui.main.api

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.bean.BasicListParam
import com.lingmiao.distribution.ui.main.bean.*
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderApiService {

    /**
     * 查订单
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/order/queryOrderById")
    fun queryOrderById(@Body body: Map<String, String>): Call<DataVO<OrderDetail>>;

    /**
     * 到达取货点
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/order/arriveShop")
    fun arriveShop(@Body body: OrderArriveShopReq): Call<DataVO<Unit>>;

    /**
     * 取货失败
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/order/pickupFail")
    fun pickFail(@Body body: OrderPickFailedReq): Call<DataVO<Unit>>;

    /**
     * 取货成功
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/order/pickup")
    fun pickup(@Body body: OrderPickReq): Call<DataVO<Unit>>;

    /**
     * 到达收货点
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/order/arriveStation")
    fun arriveStation(@Body body: OrderArriveStationReq): Call<DataVO<Unit>>;

    @WithHiResponse
    @POST("/rider-mobile-compose/order/batchArriveStation")
    fun batchArriveStation(@Body body: OrderBatchArriveStationReq): Call<DataVO<Unit>>;

    /**
     * 签收失败
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/order/signFail")
    fun signFail(@Body body: OrderSignFailedReq): Call<DataVO<Unit>>;

    /**
     * 签收成功
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/order/signed")
    fun signed(@Body body: OrderSignReq): Call<DataVO<Unit>>;

    /**
     * 签收失败
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/order/batchSignFail")
    fun batchSignFail(@Body body: OrderBatchSignReq): Call<DataVO<Unit>>;

    /**
     * 签收成功
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/order/batchSigned")
    fun batchSigned(@Body body: OrderBatchSignReq): Call<DataVO<Unit>>;

}