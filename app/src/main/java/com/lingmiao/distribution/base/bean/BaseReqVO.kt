package com.lingmiao.distribution.base.bean
import com.google.gson.annotations.SerializedName
import java.io.Serializable


open class BaseReqVO<T> : Serializable {
    @SerializedName("body")
    var body : T? = null;
}