package com.lingmiao.distribution.base.bean
import com.google.gson.annotations.SerializedName
import com.lingmiao.distribution.config.Constant
import java.io.Serializable


data class DataVO<T>(
    @SerializedName("data")
    var `data`: T?,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("code")
    var code: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
) : Serializable {

    fun isRespSuccess() : Boolean {
        return Constant.SUCCESS.equals(code);
    }

    fun isSuccessAndData(): Boolean {
        return isRespSuccess() && data != null
    }

}