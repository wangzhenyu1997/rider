package com.lingmiao.distribution.location;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.lingmiao.distribution.R;

/**
 * Create Date : 2021/5/285:53 PM
 * Auther      : Fox
 * Desc        :
 **/
public class NotificationProvider {


    //这里的id里面输入自己的项目的包的路径
    public static String ID = "com.lingmiao.distribution.service";
    public static String NAME = "Channel One";

    public static void show(Service context) {
//        Intent intent = new Intent(this, LocationService.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder notification; //创建服务对象
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
            notification = new NotificationCompat.Builder( context, ID).setChannelId(ID);
        } else {
            notification = new NotificationCompat.Builder(context);
        }
        notification.setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText("请勿关闭程序，以保证推送及时到达")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();

        Notification notification1 = notification.build();
        notification1.flags|= Notification.FLAG_ONGOING_EVENT;
        notification1.flags|= Notification.FLAG_NO_CLEAR;
        notification1.flags|= Notification.FLAG_FOREGROUND_SERVICE;
        context.startForeground(1,notification1);
    }

}
