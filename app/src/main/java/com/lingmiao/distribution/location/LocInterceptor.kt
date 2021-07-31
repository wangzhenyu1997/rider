package com.lingmiao.distribution.location

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.Interceptor
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.login.api.LoginRepository
import com.lingmiao.distribution.ui.main.bean.UploadPointVo
import kotlinx.coroutines.launch

/**
Create Date : 2021/5/285:13 PM
Auther      : Fox
Desc        :
 **/

class LocInterceptor(val context: Context) : Interceptor() {

    protected var mCoroutine: CoroutineSupport = CoroutineSupport();

    override fun intercept(success: ((Any?) -> Unit)?, failed: ((Any?) -> Unit)?){
        AmapLocationProvider.getInstance().startLocation(AMapLocationListener(){
            if(it.errorCode == 0) {
                doWork(it);
                success?.invoke(it);
            } else {
                failed?.invoke(it);
            }
        });
    }

    fun doWork(location : AMapLocation) {
        if(Constant.user.id != null) {
            mCoroutine.launch {
                val item = UploadPointVo();
                item.riderId = Constant.user.id;
                item.lat = location.latitude;
                item.lng = location.longitude;
                val resp = LoginRepository.apiService.uploadPoint(item).awaitHiResponse();
            }
        }
    }

}