package com.lingmiao.distribution.ui.login.api

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.bean.PersonalDataBean
import com.lingmiao.distribution.net.Fetch
import com.lingmiao.distribution.ui.login.bean.LoginBean
import com.lingmiao.distribution.ui.login.bean.LoginVo
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse

object LoginRepository {

    val apiService by lazy {
        Fetch.createService(LoginApiService::class.java)
    }

    /**
     * 登录
     */
    suspend fun login(request: LoginVo) : HiResponse<DataVO<LoginBean>> {
        return apiService.login(request).awaitHiResponse();
    }

    /**
     * 获取个人资料数据
     */
    suspend fun queryRider() : HiResponse<DataVO<PersonalDataBean>> {
        return apiService.queryRider().awaitHiResponse();
    }

}