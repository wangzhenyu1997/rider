package com.fisheagle.mkt.base

import com.fisheagle.mkt.business.common.bean.FileResponse
import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.ui.common.bean.UploadDataBean
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET
    operator fun get(
        @Url url: String?,
        @QueryMap param: Map<String?, Any?>?
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST
    fun post(
        @Url url: String?,
        @FieldMap param: Map<String?, Any?>?
    ): Call<ResponseBody?>?

    @POST
    @Headers("Content-type:application/json;charset=UTF-8")
    fun postByJson(
        @Url url: String?,
        @Body body: RequestBody?
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @PUT
    fun put(
        @Url url: String?,
        @FieldMap param: Map<String?, Any?>?
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @DELETE
    fun delete(
        @Url url: String?,
        @FieldMap param: Map<String?, Any?>?
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @PATCH
    fun patch(
        @Url url: String?,
        @FieldMap param: Map<String?, Any?>?
    ): Call<ResponseBody?>?

    @Streaming
    @GET
    fun download(@Url url: String?): Call<ResponseBody?>?

    @POST("")
    fun sendSmsCode(phone: String): Call<ResponseBody?>?



    @Multipart
    @POST("/base-compose/file/uploadFile")
    @WithHiResponse
    fun uploadFileOfCommon(@Part file: MultipartBody.Part): Call<DataVO<UploadDataBean>>;

    @Multipart
    @POST
    @WithHiResponse
    fun uploadFile(@Url url: String?, @Part file: MultipartBody.Part,@Query("scene") scene:String="other"): Call<FileResponse>
}