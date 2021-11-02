package com.lingmiao.distribution.ui.main.bean

import com.lingmiao.distribution.base.IConstant
import com.lingmiao.distribution.config.Constant
import java.io.Serializable

/**
Create Date : 2020/12/2810:48 AM
Auther      : Fox
Desc        :
 **/
open class DispatchListReq : Serializable {
    var pageNum : Int ? = 0;
    var pageSize : Int ? = IConstant.PAGE_SIZE_DEFAULT;
    var dispatchStatusArray : List<Int> ?= arrayListOf();
    var longitude : Double ?=  Constant.LOCATIONLONGITUDE;
    var latitude : Double ?= Constant.LOCATIONLATITUDE;
    var workStatus : Int ? = 0;
    var deliveryOrder : Int ?= 0;
    var deliveryOrderSort : Int ?= 0;
    var pickOrderSort : Int ?= 0;
    var pickOrder : Int ?= 0;
    // 综合查询字段，可以输入发/收货人的自提点、姓名、电话
    var remarks : String ?= null
}