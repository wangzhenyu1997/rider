package com.lingmiao.distribution.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.MyApplication;
import com.lingmiao.distribution.config.Constant;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * UpdateUtil
 * 2018-2-28
 * 下载单类模式
 *
 * @author yandaocheng <br/>
 */
public class UpdateUtil {
    private static UpdateUtil update;
    private Context context;
    private String mFileNameUrl = "";            //下载文件绝对路径

    private NotificationManager nm;
    private Notification notification;
    private int progress = 0;

    public static UpdateUtil getInstance(Context context) {
        if (null == update) {
            update = new UpdateUtil(context);
        }
        return update;
    }

    public UpdateUtil(Context context) {
        this.context = context;
        creatNotice();
    }

    public void downAPK() {
        Message message = new Message();
        message.what = -1;
        handler.sendMessage(message);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    URL url = new URL(Constant.downLoadUrl);// 创建url资源
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 建立http连接
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");
                    conn.setRequestProperty("Accept-Encoding", "identity");
                    // 设置字符编码
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.connect();
                    //获取内容长度
                    int contentLength = conn.getContentLength();
//			         // 请求返回的状态
                    if (conn.getResponseCode() == 200) {
                        File file = new File(Constant.downLoadPath);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        File file2 = new File(Constant.apkDownLoadPath);
                        if (!file2.exists()) {
                            file2.mkdir();
                        }
                        mFileNameUrl = Constant.apkDownLoadPath + "yyps.apk";
                        File file1 = new File(mFileNameUrl);
                        if (file1.exists()) {
                            file1.delete();
                        }
                        InputStream is = conn.getInputStream();//打开输入流
                        //创建缓冲输入流对象，相对于inputStream效率要高一些
                        BufferedInputStream bfi = new BufferedInputStream(is);
                        //创建字节流
                        byte[] bs = new byte[1024];
                        int totalReaded = 0;
                        int len = 0;
                        OutputStream os = new FileOutputStream(mFileNameUrl);
                        while ((len = bfi.read(bs)) != -1) { //写数据
                            totalReaded += len;
                            int mProgress = (int) (totalReaded * 100 / contentLength);
                            if (progress != mProgress) {
                                progress = mProgress;
                                Log.e("查看", progress + "");
                                updateNotification(progress);
                            }
//		                	Message message=new Message();
//				           	message.what = 1;
//				           	handler.sendMessage(message);
                            os.write(bs, 0, len);
                        }
                        //完成后关闭流
                        os.flush();
                        os.close();
                        is.close();
                        bfi.close();
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1://开始下载
                    Constant.downLoadState = -1;
//                    ToastUtil.showToast(MyApplication.DHActivityManager.getManager().getTopActivity(), "开始下载,请打开通知栏查看进度！");
                    nm.notify(1, notification);
                    break;
                case 0://下载中

                    break;
                case 1://下载成功
                    Constant.downLoadState = 2;
                    nm.cancel(1);
                    if (MyApplication.DHActivityManager.getManager().getTopActivity() != null) {
                        ToastUtil.showToast(MyApplication.DHActivityManager.getManager().getTopActivity(), "下载成功！");
                    }
                    PublicUtil.installProcess(mFileNameUrl);
                    break;
                case 2:    //下载失败
                    Constant.downLoadState = 2;
                    nm.cancel(1);
                    if (MyApplication.DHActivityManager.getManager().getTopActivity() != null) {
                        ToastUtil.showToast(MyApplication.DHActivityManager.getManager().getTopActivity(), "下载失败！");
                    }
                    deleteWrongFile();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 定义通知栏
     */
    @SuppressWarnings("deprecation")
    private void creatNotice() {
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String id = "my_channel_01";
        String name = "我是渠道名字";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId(id)
                    .setContentTitle("新消息")
                    .setSmallIcon(R.mipmap.app_icon).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("新消息")
                    .setSmallIcon(R.mipmap.app_icon)
                    .setOngoing(true);//无效
            notification = notificationBuilder.build();
        }
//        notification = new Notification(R.mipmap.app_icon, "新消息", System.currentTimeMillis());
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.update_notice_progress);
        contentView.setImageViewResource(R.id.iv, R.mipmap.app_icon);
        contentView.setProgressBar(R.id.pb, 100, progress, false);
        contentView.setTextViewText(R.id.tv, "0%");
        notification.contentView = contentView;
    }

    /**
     * 更新进度条
     */
    private void updateNotification(int progress) {
        notification.contentView.setProgressBar(R.id.pb, 100, progress, false);
        notification.contentView.setTextViewText(R.id.tv, progress + "%");
        nm.notify(1, notification);// 更新notification,即更新进度条
    }

    /**
     * 下载文件异常删除
     */
    private void deleteWrongFile() {
        File file2 = new File(mFileNameUrl);
        if (file2.exists()) {
            file2.delete();
        }
    }
}
