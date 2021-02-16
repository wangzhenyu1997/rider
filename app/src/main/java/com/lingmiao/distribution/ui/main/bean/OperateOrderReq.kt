package com.lingmiao.distribution.ui.main.bean
import com.google.gson.annotations.SerializedName


/**
Create Date : 2021/1/72:16 PM
Auther      : Fox
Desc        :
 **/
open class OperateOrderReq(
    @SerializedName("orderId")
    var orderId: String? = "",
    @SerializedName("dispatchId")
    var dispatchId: String? = "",
    @SerializedName("address")
    var address: String? = "",
    @SerializedName("latitude")
    var latitude: Double? = 0.0,
    @SerializedName("longitude")
    var longitude: Double? = 0.0,
    @SerializedName("urls")
    var urls: String? = ""
)