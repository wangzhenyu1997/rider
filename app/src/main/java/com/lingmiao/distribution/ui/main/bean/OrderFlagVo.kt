package com.lingmiao.distribution.ui.main.bean

import com.fisheagle.mkt.base.IConstant
import com.lingmiao.distribution.config.Constant

/**
Create Date : 2021/1/127:45 PM
Auther      : Fox
Desc        :
 **/
class OrderFlagVo : DispatchListReq() {
    var pickOrderFlag : String?= ""

    fun convert(req : DispatchListReq?) {
        this.dispatchStatusArray = req?.dispatchStatusArray;
        this.longitude = Constant.LOCATIONLONGITUDE;
        this.latitude = Constant.LOCATIONLATITUDE;
        this.workStatus = req?.workStatus;
        this.pickOrder = req?.pickOrder;
        this.pickOrderSort = req?.pickOrderSort;
        this.deliveryOrder = req?.deliveryOrder;
        this.deliveryOrder = req?.deliveryOrderSort;
    }
}