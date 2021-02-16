package com.lingmiao.distribution.ui.common.api

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.bean.BasicListParam
import com.lingmiao.distribution.ui.common.bean.ListTypeReq
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
Create Date : 2021/1/711:32 PM
Auther      : Fox
Desc        :
 **/
interface CommonApiService {

    /**
     * 签收成功
     */
    @WithHiResponse
    @POST("/base-compose/dict/queryListByType")
    fun queryListByType(@Body body: Map<String, String>): Call<DataVO<List<BasicListParam>>>;

}