package com.lingmiao.distribution.ui.login.api

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.bean.PersonalDataBean
import com.lingmiao.distribution.ui.login.bean.LoginBean
import com.lingmiao.distribution.ui.login.bean.LoginVo
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.lingmiao.distribution.ui.main.bean.UploadPointVo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {

    /**
     * 登录
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/login")
    fun login(@Body loginRequest: LoginVo): Call<DataVO<LoginBean>>

    /**
     * 获取个人资料数据
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/rider/queryRider")
    fun queryRider() : Call<DataVO<PersonalDataBean>>

    /**
     * 上传经纬度
     */
    @WithHiResponse
    @POST("/rider-mobile-compose/rider/uploadPoint")
    fun uploadPoint(@Body item : UploadPointVo) : Call<DataVO<Unit>>

}