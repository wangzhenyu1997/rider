package com.lingmiao.distribution.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.blankj.utilcode.util.ProcessUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.lingmiao.distribution.BuildConfig;
import com.lingmiao.distribution.okhttp.Https;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.ui.activity.WelcomeActivity;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Cache;
import okhttp3.OkHttpClient;


public class MyApplication extends Application {

    public static Context mContext;

    private static List<Activity> activityList = new ArrayList<>();
    // 表示应用是被杀死后在启动的
    public final static int APP_STATUS_KILLED = 0;
    // 表示应用时正常的启动流程
    public final static int APP_STATUS_NORMAL = 1;
    // 记录App的启动状态
    public static int APP_STATUS = APP_STATUS_KILLED;

    protected static MyApplication mApplication;

    public MyApplication() {
        synchronized (MyApplication.class) {
            if(mApplication == null) {
                mApplication = this;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initOkHttp();
        initJPush();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {
                    DHActivityManager.getManager().setCurrentActivity(activity);
                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            });
        }
        if(ProcessUtils.isMainProcess()){
            // bugly
            CrashReport.initCrashReport(this, "75784ebd17", BuildConfig.DEBUG);
        }
    }

    public static class DHActivityManager {

        private static DHActivityManager manager = new DHActivityManager();

        private WeakReference<Activity> current;

        private DHActivityManager() {

        }

        public static DHActivityManager getManager() {
            return manager;
        }

        public Activity getTopActivity() {
            if (current != null) {
                return current.get();
            }
            return null;
        }

        public void setCurrentActivity(Activity obj) {
            current = new WeakReference<Activity>(obj);
        }
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 初始化光推送
     */
    private void initJPush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);    // 初始化
        JPushInterface.setLatestNotificationNumber(this, 1);
        JPushInterface.stopCrashHandler(this);
    }

    /**
     * 初始化OkHttp
     */
    private void initOkHttp() {
        File cache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext));
        Https.SSLParams sslParams = Https.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)//连接超时(单位:秒)
                .writeTimeout(20, TimeUnit.SECONDS)//写入超时(单位:秒)
                .readTimeout(20, TimeUnit.SECONDS)//读取超时(单位:秒)
                .pingInterval(20, TimeUnit.SECONDS) //websocket轮训间隔(单位:秒)
                .cache(new Cache(cache.getAbsoluteFile(), cacheSize))//设置缓存
                .cookieJar(cookieJar)//Cookies持久化
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)//https配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        Glide.get(mContext).getRegistry().replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }

    /**
     * 添加Activity到activityList，在onCreate()中调用
     */
    public static void addActivity(Activity activity) {
        if (activityList != null && activityList.size() > 0) {
            if (!activityList.contains(activity)) {
                activityList.add(activity);
            }
        } else {
            activityList.add(activity);
        }
    }

    /**
     * 结束Activity到activityList，在onDestroy()中调用
     */
    public static void finishActivity(Activity activity) {
        if (activity != null && activityList != null && activityList.size() > 0) {
            activityList.remove(activity);
        }
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (null != activity) {
                    activity.finish();
                }
            }
        }
        activityList.clear();
    }

    /**
     * 获取所有activity
     */
    public static List<Activity> getAllActivity() {
        return activityList;
    }

    /**
     * 重新初始化应用界面，清空当前Activity棧，并启动欢迎页面
     */
    public static void reInitApp() {
        Intent intent = new Intent(mContext, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public static MyApplication getInstance() {
        if(mApplication == null) {
            throw new IllegalStateException("Application is'nt init!");
        }
        return mApplication;
    }
}

