package com.lingmiao.distribution.ui.main.bean
import com.google.gson.annotations.SerializedName


/**
Create Date : 2021/1/712:24 PM
Auther      : Fox
Desc        :
 **/
data class OrderDetail(
    @SerializedName("dispatch")
    var dispatch: DispatchOrderRecordBean? = null,
    @SerializedName("order")
    var order: DispatchOrderItemBean? = null,
    @SerializedName("orderTrackList")
    var orderTrackList: List<OrderTrack>? = listOf()
)

data class OrderTrack(
    @SerializedName("content")
    var content: String? = "",
    @SerializedName("createTime")
    var createTime: String? = "",
    @SerializedName("createrName")
    var createrName: String? = "",
    @SerializedName("eventType")
    var eventType: Int? = 0,
    @SerializedName("orderId")
    var orderId: String? = "",
    @SerializedName("urls")
    var urls: String? = ""
)