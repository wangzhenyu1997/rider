package com.lingmiao.distribution.location;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.blankj.utilcode.util.LogUtils;

import java.util.concurrent.TimeUnit;

/**
 * Create Date : 2021/5/286:02 PM
 * Auther      : Fox
 * Desc        :
 **/
public class WorkProvider {

    public static void post(Context context) {
        // 重复的任务  多次/循环/轮询
        // 最少轮询/循环一次 15分钟（Google规定的）
        // 不能小于15分钟，否则默认修改成 15分钟
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                LocationWorker.class,
                15,
                TimeUnit.SECONDS
        ).build();

        // 【状态机】
        // 轮询的任务，没有 SUCCESS
        // 单个任务 SUCCESS
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(periodicWorkRequest.getId())
                .observe((LifecycleOwner) context, new Observer<WorkInfo>(){
                    /**
                     * Called when the data is changed.
                     *
                     * @param workInfo The new data
                     */
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        // ENQUEEN   SUCCESS
                        Log.d("service","状态：" + workInfo.getState().name());
                        if (workInfo.getState().isFinished()) {
                            Log.d("service", "状态：isFinished=true ");
                        }
                    }
                });

        WorkManager.getInstance(context).enqueue(periodicWorkRequest);
    }
}
