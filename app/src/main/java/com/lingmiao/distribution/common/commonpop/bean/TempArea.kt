package com.lingmiao.distribution.common.commonpop.bean

import java.io.Serializable

/**
Create Date : 2021/9/911:54 下午
Auther      : Fox
Desc        :
 **/
data class TempArea(
    /**
     * 所有解释的区域
     */
    var allParsedAreas : MutableList<Map<String, Map<String, Any>>> ?= arrayListOf(),
    /**
     * 所有选中的区域
     */
    var selectedIds : MutableList<String>
) : Serializable {

    /**
     * 是否存在于所有已选中的区域中
     */
    fun existId(id : Int?) : Boolean {
        return selectedIds?.contains(String.format("%s", id));
    }
}
