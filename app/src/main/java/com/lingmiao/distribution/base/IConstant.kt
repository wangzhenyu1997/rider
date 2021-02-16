package com.fisheagle.mkt.base

object IConstant {

    const val PAGE_SIZE_10 = 10
    const val PAGE_SIZE = 20
    const val PAGE_SIZE_DEFAULT = PAGE_SIZE_10;
    const val SERVICE_PHONE = "15901899796"

    var official = false


    fun getServerUrl(): String {
        var baseUrl = "http://8.136.135.41:8778/"
        if (official) baseUrl = "http://8.136.135.41:8778/"
        return baseUrl
    }

    /**
     * 传递的不同类型
     */
    const val BUNDLE_KEY_OF_VIEW_TYPE = "KEY_VIEW_TYPE";

    /**
     * 传递的内容项
     */
    const val BUNDLE_KEY_OF_ITEM = "KEY_ITEM";
    /**
     * 传递的内容项唯一码
     */
    const val BUNDLE_KEY_OF_ITEM_ID = "KEY_ITEM_ID";
}