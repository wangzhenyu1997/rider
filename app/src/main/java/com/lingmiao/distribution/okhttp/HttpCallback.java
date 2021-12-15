package com.lingmiao.distribution.okhttp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.internal.$Gson$Types;
import com.lingmiao.distribution.util.ToastUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * HttpCallback
 *
 * @author yandaocheng <br/>
 * 自定义抽象类请求回调
 * 2017-09-20
 * 修改者，修改日期，修改内容
 */
public abstract class HttpCallback<T> {
    Type mType;
    private Context mContext;
    private Dialog mDialog;

    public HttpCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    public HttpCallback(Context context) {
        this.mContext = context;
        mType = getSuperclassTypeParameter(getClass());
    }

    public HttpCallback(Context context, Dialog dialog) {
        this.mContext = context;
        this.mDialog = dialog;
        mType = getSuperclassTypeParameter(getClass());
        mDialog.show();
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * @Description 请求成功时回调
     */
    public void onSuccess(T response) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    /**
     * @param code    状态码
     * @param message 状态消息
     * @Description 请求失败时回调
     */
    public void onFailure(int code, String message) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (mContext != null) {
            ToastUtil.showToast(mContext, "网络请求失败，请稍后再试！");
        }
    }

    /**
     * @param state 自动登录成功回调
     */
    public void onAutoLogin(Boolean state) {
    }

    /**
     * @param currentTotalLen 进度
     * @param totalLen        总量
     * @Description 上传或下载时进度回调
     */
    public void onProgress(long currentTotalLen, long totalLen) {
    }

    /**
     * @param bitmap 图片bitmap
     * @Description 显示图片成功回调
     */
    public void onBitmapSuccess(Bitmap bitmap) {
    }
}
