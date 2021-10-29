package com.lingmiao.distribution.base.bean
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   :
 */
data class PageVO<T>(
    @SerializedName("records")
    var records: List<T>? = listOf(),
    @SerializedName("pageNum")
    var pageNum: Int? = 0,
    @SerializedName("pageSize")
    var pageSize: Int? = 0,
    @SerializedName("totalCount")
    var totalCount: Int? = 0,
    @SerializedName("totalPages")
    var totalPages: Int? = 0
) : Serializable {

}