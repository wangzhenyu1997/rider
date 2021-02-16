package com.james.common.net;


import com.blankj.utilcode.util.AppUtils;
import com.james.common.netcore.networking.http.core.HiConverterFactory;
import com.james.common.netcore.networking.http.core.PassThroughExecutor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lqx
 */
public class RetrofitUtil {
    private static volatile RetrofitUtil instance;
    private static volatile Retrofit retrofit;
    private static final int DEFAULT_TIMEOUT = 90;
    private static volatile Interceptor interceptor;
    private static volatile OkHttpClient okHttpClient;

    private static String baseUrl = "";//IConstant getSellerUrl()

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        RetrofitUtil.baseUrl = baseUrl;
    }

    public static Interceptor getInterceptor() {
        return interceptor;
    }

    public static void setInterceptor(Interceptor interceptor) {
        RetrofitUtil.interceptor = interceptor;
    }

    public RetrofitUtil() {
        initRetrofit();
    }

    public static RetrofitUtil getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new RetrofitUtil();
                }
            }
        }
        return instance;
    }


    public static Retrofit initRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        httpClientBuilder.addInterceptor(interceptor);
        if (AppUtils.isAppDebug()) {
            httpClientBuilder.addInterceptor(logging);
        }
        OkHttpClient client = httpClientBuilder.build();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(HiConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(new PassThroughExecutor())
                .client(client)
                .build();
        RetrofitUtil.okHttpClient = client;
        retrofit = mRetrofit;
        return mRetrofit;
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        RetrofitUtil.okHttpClient = okHttpClient;
    }

    public  Retrofit getRetrofit() {
        return retrofit;
    }

    public  void setRetrofit(Retrofit retrofit) {
        RetrofitUtil.retrofit = retrofit;
    }
}
