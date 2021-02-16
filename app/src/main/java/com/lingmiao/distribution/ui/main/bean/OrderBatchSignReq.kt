package com.lingmiao.distribution.ui.main.bean

import com.google.gson.annotations.SerializedName


/**
Create Date : 2021/1/72:23 PM
Auther      : Fox
Desc        :
 **/
class OrderBatchSignReq {

    /**
     * 短信标识 0 不发 1发短信
     */
    @SerializedName("shortMessageFlag")
    var shortMessageFlag: Int? = 0

    /**
     * 签收备注
     */
    @SerializedName("remarks")
    var remarks: String? = ""

    /**
     * 签收地址
     */
    @SerializedName("signAddress")
    var signAddress: String? = ""

    @SerializedName("address")
    var address: String? = ""

    @SerializedName("explainReason")
    var explainReason: String? = ""

    @SerializedName("latitude")
    var latitude: Double? = 0.0

    @SerializedName("longitude")
    var longitude: Double? = 0.0

    @SerializedName("signFailType")
    var signFailType: String? = ""

    @SerializedName("urls")
    var urls: String? = ""

    @SerializedName("orderIds")
    var orderIds: ArrayList<String>? = null
}