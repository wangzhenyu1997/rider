package com.lingmiao.distribution.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lingmiao.distribution.util.UpdateUtil;


/**
 * UpdateService
 *
 * @author yandaocheng <br/>
 * apk下载service服务
 * 2018-2-28
 * 修改者，修改日期，修改内容
 */
public class UpdateService extends Service {

    private UpdateUtil update;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        update = UpdateUtil.getInstance(this);
        update.downAPK();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
