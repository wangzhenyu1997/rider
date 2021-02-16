package com.lingmiao.distribution.base.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
Create Date : 2020/12/294:29 PM
Auther      : Fox
Desc        :
 **/
class BasePageReqVO<T> : BaseReqVO<T>(), Serializable {
    @SerializedName("pageNum")
    var pageNum: Int? = 0
    @SerializedName("pageSize")
    var pageSize: Int? = 0
}