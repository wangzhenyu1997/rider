package com.lingmiao.distribution.ui.main.api

import com.fisheagle.mkt.business.common.bean.PageVO
import com.lingmiao.distribution.base.bean.BasePageReqVO
import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.bean.UpdateBean
import com.lingmiao.distribution.ui.main.bean.*
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DispatchApiService {

    /**
     * 更新工作状态
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/rider/updateWorkStatus")
    fun updateWorkStatus(@Body map: Map<String, Int>): Call<DataVO<Unit>>;

    /**
     * 抢单列表
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/order/queryOrderListByRiderId")
    fun queryOrderListByRiderId(@Body map: BasePageReqVO<DispatchListReq>): Call<DataVO<PageVO<DispatchOrderItemBean>>>;

    /**
     * 调度单列表
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/queryDispatchList")
    fun queryDispatchList(@Body map: BasePageReqVO<DispatchListReq>): Call<DataVO<PageVO<DispatchOrderRecordBean>>>;

    /**
     * 调度单详情
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/queryDispatchById")
    fun queryDispatchById(@Body map: Map<String, String>): Call<DataVO<DispatchOrderRecordBean>>;
    /**
     * 待送达拆分的调度单详情
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/queryOrderListByPickOrderFlag")
    fun queryOrderListByPickOrderFlag(@Body map: BasePageReqVO<OrderFlagVo>): Call<DataVO<DispatchOrderRecordBean>>;

    /**
     * 获取调度单列表数
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/countDispatchNum")
    fun loadDispatchNumber(@Body map: ReqBodyVo<DispatchListReq>): Call<DataVO<DispatchNumberBean>>;

    /**
     * 确认接单
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/agreeAccept")
    fun agreeAccept(@Body map: Map<String, String>): Call<DataVO<Unit>>;

    /**
     * 确认抢单
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/assignAndAccept")
    fun assignAndAccept(@Body data: TakeOrderReq): Call<DataVO<Unit>>;

    /**
     * 拒单接单
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/refuseAccept")
    fun refuse(@Body body : RefuseOrderReq): Call<DataVO<Unit>>;

    /**
     * 到达取货点
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/arriveShop")
    fun arriveShop(@Body body: OrderArriveShopReq): Call<DataVO<Unit>>;

    /**
     * 取货成功
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/pickup")
    fun pickup(@Body body: OrderPickReq): Call<DataVO<Unit>>;

    /**
     * 取货失败
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/pickupFail")
    fun pickFail(@Body body: OrderPickFailedReq): Call<DataVO<Unit>>;

    /**
     * 到达收货点
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/arriveStation")
    fun arriveStation(@Body body: OrderArriveStationReq): Call<DataVO<Unit>>;

    /**
     * 签收成功
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/signed")
    fun signed(@Body body: OrderSignReq): Call<DataVO<Unit>>;

    /**
     * 签收失败
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/signFail")
    fun signFail(@Body body: OrderSignFailedReq): Call<DataVO<Unit>>;


    /**
     * 上传异常
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/uploadException")
    fun uploadException(@Body body: OrderExceptionReq): Call<DataVO<Unit>>;

    /**
     * 跟据上游订单号查询订单
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/queryOrderByUpsBillNo")
    fun getOrderByUpsBillNo(@Body body: Map<String, String>): Call<DataVO<DispatchOrderRecordBean>>;

    /**
     * 跟据上游订单号查询订单
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/dispatch/queryOrderDetailsByUpsBillNo")
    fun getOrderDetailsByUpsBillNo(@Body body: Map<String, String>): Call<DataVO<DispatchOrderRecordBean>>;

    /**
     * 升级
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/app/upgrade")
    fun upgrade(@Body data : Map<String, String>) : Call<DataVO<UpdateBean>>

    /**
     * 修改接单设置
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/setting/updateSetting")
    fun updateSetting(@Body data : HomeModelEvent) : Call<DataVO<Unit>>

    /**
     * 修改接单设置
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/setting/querySetting")
    fun querySetting() : Call<DataVO<TakingSettingBean>>

    /**
     * 查订单
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/order/queryOrderById")
    fun queryOrderById(@Body body: Map<String, String>): Call<DataVO<OrderDetail>>;

}