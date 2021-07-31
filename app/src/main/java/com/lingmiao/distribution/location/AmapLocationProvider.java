package com.lingmiao.distribution.location;

import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lingmiao.distribution.app.MyApp;
import com.lingmiao.distribution.app.MyApplication;

/**
 * Create Date : 2021/5/281:56 PM
 * Auther      : Fox
 * Desc        :
 **/
public class AmapLocationProvider {

    private AMapLocationClient mAMapLocationClient;

    private static AmapLocationProvider instance;

    public static AmapLocationProvider getInstance() {
        if(instance == null) {
            synchronized (AmapLocationProvider.class) {
                if(instance == null) {
                    instance = new AmapLocationProvider();
                }
            }
        }
        return instance;
    }

    private AmapLocationProvider(){
        Log.d("service Provider", "init");
        mAMapLocationClient = new AMapLocationClient(MyApp.getInstance());
        //配置参数
        mAMapLocationClient.setLocationOption(getOption());
    }

    public void startLocation(AMapLocationListener locationListener) {
        Log.d("service Provider", "start");
        if(mAMapLocationClient != null) {
            mAMapLocationClient.setLocationListener(locationListener);
            mAMapLocationClient.startLocation();
        }
    }

    public void stopLocation() {
        Log.d("service Provider", "stop");
        if(mAMapLocationClient != null) {
            mAMapLocationClient.stopLocation();
        }
    }

    private AMapLocationClientOption getOption() {
        AMapLocationClientOption mClientOption = new AMapLocationClientOption();
        //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mClientOption.setGpsFirst(true);
        //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mClientOption.setHttpTimeOut(15000);
        //可选，设置定位间隔。默认为2秒
        mClientOption.setInterval(15000);
        //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        //可选，设置是否使用传感器。默认是false
        mClientOption.setSensorEnable(false);
        //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mClientOption.setWifiScan(true);
        //可选，设置是否使用缓存定位，默认为true
        mClientOption.setLocationCacheEnable(false);
        //可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        mClientOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);
        return mClientOption;
    }

}
