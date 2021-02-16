package com.lingmiao.distribution.ui.common.api

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.bean.BasicListParam
import com.lingmiao.distribution.net.Fetch
import com.lingmiao.distribution.ui.common.bean.ListTypeReq
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse

object CommonRepository {

    val apiService by lazy {
        Fetch.createService(CommonApiService::class.java)
    }

    /**
     * 获取个人资料数据
     */
    suspend fun queryListByType(type : String) : HiResponse<DataVO<List<BasicListParam>>> {
        val map = mutableMapOf<String, String>()
        map.put("type", type)
        return apiService.queryListByType(map).awaitHiResponse();
    }

}