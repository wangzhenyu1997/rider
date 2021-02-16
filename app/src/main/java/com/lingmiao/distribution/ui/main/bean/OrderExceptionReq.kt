package com.lingmiao.distribution.ui.main.bean
import com.google.gson.annotations.SerializedName


/**
Create Date : 2021/1/72:23 PM
Auther      : Fox
Desc        :
 **/
 class OrderExceptionReq(
    @SerializedName("exceptionType")
    var exceptionType: String? = "",
    @SerializedName("exceptionTime")
    var exceptionTime: String? = "",
    @SerializedName("content")
    var content: String? = ""
) : OperateOrderReq() {

}