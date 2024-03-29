package com.lingmiao.distribution.okhttp;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * OkHttpRequest
 *
 * @author yandaocheng <br/>
 * OkHttp3工具类
 * 2017-09-20
 * 修改者，修改日期，修改内容
 */
public class OkHttpUtils {

    private static OkHttpUtils mInstance;
    private static OkHttpClient mOkHttpClient;
    private static Platform mPlatform;

    private OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }
        mPlatform = Platform.get();
    }

    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static void sendFailResultCallback(final int code, final String message, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(() -> callback.onFailure(code, message));
    }

    public static void sendAutoLoginResultCallback(Boolean state, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(() -> callback.onAutoLogin(state));
    }

    public static void sendSuccessResultCallback(final Object object, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(() -> callback.onSuccess(object));
    }

    public static void sendCloseDialogCallback(final HttpCallback callback) {
        if (callback == null) {
            return;
        }
//        mPlatform.execute(() -> callback.onCloseDialog());
    }

    public static void sendProgressResultCallback(final long currentTotalLen, final long totalLen, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(() -> callback.onProgress(currentTotalLen, totalLen));
    }

    public static void sendBitmapSuccessResultCallback(final Bitmap bitmap, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(() -> callback.onBitmapSuccess(bitmap));
    }

    /**--------------------    同步数据请求    --------------------**/

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getSync(String url, HttpCallback callback) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null);
        OkHttpRequest.doExecute(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getSync(String url, Map<String, String> params, HttpCallback callback) {
        if (params != null && !params.isEmpty()) {
            url = OkHttpRequest.appendGetParams(url, params);
        }
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null);
        OkHttpRequest.doExecute(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description POST请求
     */
    public static void postSync(String url, Map<String, String> params, HttpCallback callback) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, url, params, null);
        OkHttpRequest.doExecute(request, callback);
    }

    /**--------------------    异步数据请求    --------------------**/

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getAsyn(String url, HttpCallback callback) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @param value    判断是否登录失效
     * @Description GET请求
     */
    public static void getAsyn(String url, HttpCallback callback, Boolean value) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null);
        OkHttpRequest.doEnqueue(request, callback, value);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getAsyn(String url, Map<String, String> params, HttpCallback callback) {
        Log.e("请求地址:", url + (params == null ? "" : params.toString()));
        if (params != null && !params.isEmpty()) {
            url = OkHttpRequest.appendGetParams(url, params);
        }
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @param value    判断是否登录失效
     * @Description GET请求
     */
    public static void getAsyn(String url, Map<String, String> params, HttpCallback callback, Boolean value) {
        Log.e("请求地址:", url + (params == null ? "" : params.toString()));
        if (params != null && !params.isEmpty()) {
            url = OkHttpRequest.appendGetParams(url, params);
        }
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null);
        OkHttpRequest.doEnqueue(request, callback, value);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description POST请求
     */
    public static void postAsyn(String url, Map<String, String> params, HttpCallback callback) {
        Log.e("请求地址:", url + (params == null ? "" : params.toString()));
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, url, params, null);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @param value    判断是否登录失效
     * @Description POST请求
     */
    public static void postAsyn(String url, Map<String, String> params, HttpCallback callback, Boolean value) {
        Log.e("请求地址:", url + (params == null ? params.toString() : ""));
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, url, params, null);
        OkHttpRequest.doEnqueue(request, callback, value);
    }

    /**
     * @param url      请求地址
     * @param json     json数据格式
     * @param callback 请求回调
     * @Description POST提交JSON数据
     */
    public static void postAync(String url, String json, HttpCallback callback) {
        Log.e("请求地址:", url + " 请求json数据=" + json);
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, url, null, json);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param json     json数据格式
     * @param callback 请求回调
     * @param value    判断是否登录失效
     * @Description POST提交JSON数据
     */
    public static void postAync(String url, String json, HttpCallback callback, Boolean value) {
        Log.e("请求地址:", url + " 请求json数据=" + json);
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, url, null, json);
        OkHttpRequest.doEnqueue(request, callback, value);
    }

    /**--------------------    文件上传    --------------------**/

    /**
     * @param url      请求地址
     * @param file     上传文件
     * @param callback 请求回调
     * @Description 单文件上传
     */
    public static void postAsynFile(String url, File file, HttpCallback callback) {
        if (!file.exists()) {
//            ToastUtil.showText(UIUtils.getString(R.string.file_does_not_exist));//文件不存在
            return;
        }
        Request request = OkHttpRequest.builderFileRequest(url, file, null, null, null, callback);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param pic_key  上传图片关键字（约定pic_key如“upload”作为后台接受多张图片的key）
     * @param files    上传文件集合
     * @param params   请求参数
     * @param callback 请求回调
     * @Description 多文件上传
     */
    public static void postAsynFiles(String url, String pic_key, List<File> files, Map<String, String> params, HttpCallback callback) {
        Request request = OkHttpRequest.builderFileRequest(url, null, pic_key, files, params, callback);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param pic_key  上传图片关键字（约定pic_key如“upload”作为后台接受多张图片的key）
     * @param files    上传文件集合
     * @param params   请求参数
     * @param callback 请求回调
     * @param value    判断是否登录失效
     * @Description 多文件上传
     */
    public static void postAsynFiles(String url, String pic_key, List<File> files, Map<String, String> params, HttpCallback callback, Boolean value) {
        Request request = OkHttpRequest.builderFileRequest(url, null, pic_key, files, params, callback);
        OkHttpRequest.doEnqueue(request, callback, value);
    }

    /**--------------------    文件下载    --------------------**/

    /**
     * @param url          请求地址
     * @param destFileDir  目标文件存储的文件夹路径，如：Environment.getExternalStorageDirectory().getAbsolutePath()
     * @param destFileName 目标文件存储的文件名，如：gson-2.7.jar
     * @param callback     请求回调
     * @Description 文件下载
     */
    public static void downloadAsynFile(String url, String destFileDir, String destFileName, HttpCallback callback) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null);
        OkHttpRequest.doDownloadEnqueue(request, destFileDir, destFileName, callback);
    }

    /**
     * @param url          请求地址
     * @param destFileDir  目标文件存储的文件夹路径
     * @param destFileName 目标文件存储的文件名
     * @param params       请求参数
     * @param callback     请求回调
     * @Description 文件下载
     */
    public void downloadAsynFile(String url, String destFileDir, String destFileName, Map<String, String> params, HttpCallback callback) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, url, params, null);
        OkHttpRequest.doDownloadEnqueue(request, destFileDir, destFileName, callback);
    }

    /**--------------------    图片显示    --------------------**/

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @Description 图片显示
     */
    public static void displayAsynImage(String url, HttpCallback callback) {
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, url, null, null);
        OkHttpRequest.doDisplayEnqueue(request, callback);
    }

    /**--------------------    流式提交    --------------------**/

    /**
     * @param url      请求地址
     * @param content  提交内容
     * @param callback 请求回调
     * @Description 使用流的方式提交POST请求
     */
    public static void postAsynStream(String url, String content, HttpCallback callback) {
        Request request = OkHttpRequest.builderStreamRequest(url, content);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param tag 请求标签
     * @Description 取消请求
     */
    public static void cancelTag(Object tag) {
        if (tag == null) {
            return;
        }
        synchronized (mOkHttpClient.dispatcher().getClass()) {
            for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }
    }
}
