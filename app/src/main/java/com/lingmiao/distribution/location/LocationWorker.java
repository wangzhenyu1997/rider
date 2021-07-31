package com.lingmiao.distribution.location;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Create Date : 2021/5/285:04 PM
 * Auther      : Fox
 * Desc        :
 **/
public class LocationWorker extends Worker {

    private Context mContext;

    private WorkerParameters workerParams;

    // 有构造函数
    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
        this.workerParams = workerParams;
    }

    Result result = null;

    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public Result doWork() {
        // 获取传参
        // String dataString = workerParams.getInputData().getString("data");
        //AmapLocationProvider.getInstance().startLocation(mLocationListener);

        // 传参
        // Data outputData = new Data.Builder().putString("data", "三分归元气").build();
        // Result.Success success = new Result.Success(outputData);

        LocInterceptor item = new LocInterceptor(mContext);

        item.intercept((it)->{
            result = new Result.Success();
            return null;
        },(it2)->{
            result = new Result.Failure();
            return null;
        });
        return result;
    }

}
