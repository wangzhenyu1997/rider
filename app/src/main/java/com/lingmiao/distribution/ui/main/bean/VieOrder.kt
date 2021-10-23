package com.lingmiao.distribution.ui.main.bean


import com.google.gson.annotations.SerializedName

data class VieOrder(
    @SerializedName("collectingFreightCost")
    var collectingFreightCost: Int? = 0,
    @SerializedName("collectingGoodsCost")
    var collectingGoodsCost: Int? = 0,
    @SerializedName("consigneeAddress")
    var consigneeAddress: String? = "",
    @SerializedName("consigneeCity")
    var consigneeCity: String? = "",
    @SerializedName("consigneeCustomerMobile")
    var consigneeCustomerMobile: String? = "",
    @SerializedName("consigneeCustomerName")
    var consigneeCustomerName: String? = "",
    @SerializedName("consigneeDistrict")
    var consigneeDistrict: String? = "",
    @SerializedName("consigneeLat")
    var consigneeLat: Double? = 0.0,
    @SerializedName("consigneeLng")
    var consigneeLng: Double? = 0.0,
    @SerializedName("consigneeName")
    var consigneeName: String? = "",
    @SerializedName("consigneePhone")
    var consigneePhone: String? = "",
    @SerializedName("consigneeProvince")
    var consigneeProvince: String? = "",
    @SerializedName("consigneeStreet")
    var consigneeStreet: String? = "",
    @SerializedName("consignerAddress")
    var consignerAddress: String? = "",
    @SerializedName("consignerCity")
    var consignerCity: String? = "",
    @SerializedName("consignerCustomerId")
    var consignerCustomerId: String? = "",
    @SerializedName("consignerCustomerMobile")
    var consignerCustomerMobile: String? = "",
    @SerializedName("consignerCustomerName")
    var consignerCustomerName: String? = "",
    @SerializedName("consignerDistrict")
    var consignerDistrict: String? = "",
    @SerializedName("consignerLat")
    var consignerLat: Double? = 0.0,
    @SerializedName("consignerLng")
    var consignerLng: Double? = 0.0,
    @SerializedName("consignerName")
    var consignerName: String? = "",
    @SerializedName("consignerPhone")
    var consignerPhone: String? = "",
    @SerializedName("consignerProvince")
    var consignerProvince: String? = "",
    @SerializedName("consignerStreet")
    var consignerStreet: String? = "",
    @SerializedName("createTime")
    var createTime: String? = "",
    @SerializedName("createrId")
    var createrId: Any? = Any(),
    @SerializedName("customerRemark")
    var customerRemark: Any? = Any(),
    @SerializedName("dispatchId")
    var dispatchId: Any? = Any(),
    @SerializedName("dispatchNo")
    var dispatchNo: Any? = Any(),
    @SerializedName("distance")
    var distance: Double? = 0.0,
    @SerializedName("estimateCost")
    var estimateCost: Int? = 0,
    @SerializedName("goodsName")
    var goodsName: String? = "",
    @SerializedName("goodsPiece")
    var goodsPiece: Int? = 0,
    @SerializedName("goodsType")
    var goodsType: String? = "",
    @SerializedName("goodsVolume")
    var goodsVolume: Int? = 0,
    @SerializedName("goodsWeight")
    var goodsWeight: Int? = 0,
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("insuranceCost")
    var insuranceCost: Int? = 0,
    @SerializedName("isAppointment")
    var isAppointment: Int? = 0,
    @SerializedName("isCollecting")
    var isCollecting: Int? = 0,
    @SerializedName("isComment")
    var isComment: Int? = 0,
    @SerializedName("isDelete")
    var isDelete: Int? = 0,
    @SerializedName("isSelfTake")
    var isSelfTake: Int? = 0,
    @SerializedName("labels")
    var labels: String? = "",
    @SerializedName("merchantId")
    var merchantId: String? = "",
    @SerializedName("orderNo")
    var orderNo: String? = "",
    @SerializedName("orderStatus")
    var orderStatus: Int? = 0,
    @SerializedName("orderType")
    var orderType: Int? = 0,
    @SerializedName("orgId")
    var orgId: String? = "",
    @SerializedName("originDistance")
    var originDistance: Any? = Any(),
    @SerializedName("planDeliveryTime")
    var planDeliveryTime: String? = "",
    @SerializedName("remarks")
    var remarks: Any? = Any(),
    @SerializedName("showTimeRequire")
    var showTimeRequire: Any? = Any(),
    @SerializedName("siteManName")
    var siteManName: Any? = Any(),
    @SerializedName("targetDistance")
    var targetDistance: Any? = Any(),
    @SerializedName("timeRequire")
    var timeRequire: Any? = Any(),
    @SerializedName("totalCost")
    var totalCost: Int? = 0,
    @SerializedName("updateTime")
    var updateTime: Any? = Any(),
    @SerializedName("updaterId")
    var updaterId: Any? = Any(),
    @SerializedName("upsBillId")
    var upsBillId: String? = "",
    @SerializedName("upsBillNo")
    var upsBillNo: String? = ""
)