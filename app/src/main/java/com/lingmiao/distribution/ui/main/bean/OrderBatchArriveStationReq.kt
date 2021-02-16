package com.lingmiao.distribution.ui.main.bean
import com.google.gson.annotations.SerializedName


/**
Create Date : 2021/1/72:23 PM
Auther      : Fox
Desc        :
 **/
data class OrderBatchArriveStationReq(
 @SerializedName("urls")
 var urls: String? = "",
 var address: String? = "",
 @SerializedName("latitude")
 var latitude: Double? = 0.0,
 @SerializedName("longitude")
 var longitude: Double? = 0.0,
 @SerializedName("orderIds")
 var orderIds: ArrayList<String>? = null
) {

}