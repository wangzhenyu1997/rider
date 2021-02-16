package com.lingmiao.distribution.ui.main.bean
import com.google.gson.annotations.SerializedName


/**
Create Date : 2021/1/72:23 PM
Auther      : Fox
Desc        :
 **/
 class OrderPickFailedReq(
    @SerializedName("explainReason")
    var explainReason: String? = "",
    @SerializedName("pickupFailType")
    var pickupFailType: String? = ""
) : OperateOrderReq() {

}