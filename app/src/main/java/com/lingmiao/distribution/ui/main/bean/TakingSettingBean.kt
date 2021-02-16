package com.lingmiao.distribution.ui.main.bean
import com.google.gson.annotations.SerializedName
import com.lingmiao.distribution.bean.HomeModelEvent
import java.io.Serializable


/**
Create Date : 2020/12/301:14 AM
Auther      : Fox
Desc        :
 **/
data class TakingSettingBean(
    @SerializedName("setting")
    var setting: HomeModelEvent? = null
)
