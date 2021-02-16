package com.lingmiao.distribution.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.Window;
import android.view.WindowManager;

import com.lingmiao.distribution.app.MyApplication;
import com.lingmiao.distribution.dialog.ConfirmDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.graphics.BitmapFactory.decodeStream;

/**
 * PublicUtil
 *
 * @author yandaocheng <br/>
 * 公共方法
 * 2018-08-14
 * 修改者，修改日期，修改内容
 */
public class PublicUtil {
    /**
     * 判断是否是null值，主要是防止接口传值null
     */
    public static Boolean haveCard(Context context) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastUtil.showToast(context, "sd卡不存在，请检查手机相关sd卡！");
            return false;
        }
        return true;
    }

    /**
     * 判断是否是null值，主要是防止接口传值null
     */
    public static String isNull(String s) {
        if (s == null || s.equals("null")) {
            return "";
        }
        return s;
    }

    /**
     * 截取时间字符串
     */
    public static String subTime(String s, int start, int end) {
        if (s == null || s == "") {
            return "";
        }
        if (s.length() < end) {
            return s;
        }
        return s.substring(start, end);
    }

    /**
     * 将时间戳转换为时间,可为"yyyyMMdd","yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy年MM月dd日 HH:mm:ss",等等
     */
    public static String stampToDate(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    /**
     * 将时间戳转换为时间,可为"yyyyMMdd","yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy年MM月dd日 HH:mm:ss",等等
     */
    public static String formatDate(String dateStr, String format) {
        if (dateStr == null || dateStr.isEmpty() || dateStr.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(date);
    }

    /**
     * 将时间戳转换为时间,可为"yyyyMMdd","yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy年MM月dd日 HH:mm:ss",等等
     */
    public static Date formatToDate(String dateStr, String format) {
        if (dateStr == null || dateStr.isEmpty() || dateStr.equals("null")) {
            return null;
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 将时间date转换为时间,可为"yyyyMMdd","yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy年MM月dd日 HH:mm:ss",等等
     */
    public static String getTime(Date date, String format) {
        if (date == null) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 获取当前系统时间，可为"yyyyMMdd","yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy年MM月dd日 HH:mm:ss",等等
     *
     * @return
     */
    public static String getNowTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }


    /**
     * 输入日期字符串比如201703，返回当月第一天的Date
     */
    public static String getMinDateMonth(long stamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = new Date(stamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return format.format(calendar.getTime());
    }

    /**
     * 输入日期字符串，返回当月最后一天的Date
     */
    public static String getMaxDateMonth(long stamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = new Date(stamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format.format(calendar.getTime());
    }

    /**
     * 获取两个日期之间的相差天数
     *
     * @return 天数
     */
    public static int getDaysBetween(String starStr, String endStr) {
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null; //开始时间
        Date endDate = null; //结束时间
        try {
            startDate = sdf.parse(starStr);
            endDate = sdf.parse(endStr);
            //得到相差的天数 betweenDate
            long betweenDate = (endDate.getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
            return (int) betweenDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取两个日期之间的所有日期
     *
     * @return 天数
     */
    public static List<String> getDaysListBetween(String starStr, String endStr) {
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null; //开始时间
        Date endDate = null; //结束时间
        try {
            startDate = sdf.parse(starStr);
            endDate = sdf.parse(endStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(sdf.format(result));
            calendar.add(Calendar.DATE, 1);
        }
        datesInRange.add(sdf.format(endCalendar.getTime()));
        return datesInRange;
    }


    /**
     * 获取两个日期之间的较小的时间
     *
     * @return 天数
     */
    public static String getMinDate(String dateStr1, String dateStr2) {
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null; //开始时间
        Date date2 = null; //结束时间
        try {
            date1 = sdf.parse(dateStr1);
            date2 = sdf.parse(dateStr2);
            //得到相差的天数 betweenDate
            long betweenDate = (date2.getTime() - date1.getTime()) / (60 * 60 * 24 * 1000);
            if (betweenDate > 0) {
                return dateStr1;
            }
            return dateStr2;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr2;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 处理html文件保证图片宽度充满
     */
    public static String getNewContent(String content) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"UTF-8\">\n" +
                "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no\">\n" +
                "\t\t<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
                "\t\t<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n" +
                "\t\t<title></title>\n" +
                "\t\t<style>" +
                "img{max-width:100%!important; height:auto;}" +
                "video{max-width:100%!important; height:auto;}" +
                "p{word-break:break-all}" +
                "</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                content +
                "\t</body>\n" +
                "</html>";
    }

    /**
     * 修改价格字体大小
     */
    public static SpannableString modifyTextSize(String text) {
        if (text == "" || text == null)
            return null;
        String mCopPrice = "￥" + MathExtend.round(text, 2);
        int mIndex = mCopPrice.indexOf(".");
        SpannableString spanString = new SpannableString(mCopPrice);
        spanString.setSpan(new AbsoluteSizeSpan(20, true), 1, mIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }

    /**
     * 修改状态栏颜色
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 千分位转换
     */
    public static String thousandthNum(String num) {
        if (num == null || num.equals("")) {
            return "0.00";
        }
        // DecimalFormat df2 = new DecimalFormat("#,###.00");
        DecimalFormat df2 = new DecimalFormat(",##0.00");
        return df2.format(Double.valueOf(num));
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {

        }
        return versionName;
    }

    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {

        }
        return versionCode;
    }


    /**
     * 两个时间比较,如果第一个时间比第二个时间小，返回true
     */
    public static boolean getTwoDateResult(String time1, String time2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d2 = df.parse((time1 + " 23:59:59"));
            Date d3 = df.parse((time2 + " 00:00:00"));
            if (d2.getTime() < d3.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间 "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间 "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true昨天 false不是
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    /**
     * apk进行安装，兼容8.0以上手机
     */
    public static void installProcess(String path) {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = MyApplication.DHActivityManager.getManager().getTopActivity().getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限
                ConfirmDialog dialog = new ConfirmDialog(MyApplication.DHActivityManager.getManager().getTopActivity(), "安装应用需要打开未知来源权限，请去设置中开启权限", new ConfirmDialog.DialogConfirmClick() {
                    @Override
                    public void ConfirmClick(Boolean value) {
                        if (value) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startInstallPermissionSettingActivity(MyApplication.DHActivityManager.getManager().getTopActivity());
                            }
                        }
                    }
                });
                dialog.show();
                return;
            }
        }
        //有权限，开始安装应用程序
        installNormal(path);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallPermissionSettingActivity(Context contextNow) {
        Uri packageURI = Uri.parse("package:" + contextNow.getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        ((Activity) contextNow).startActivityForResult(intent, 10086);
    }

    /**
     * apk进行安装，兼容7.0以上手机
     * apkPath:apk本地文件绝对路径
     */
    public static void installNormal(String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT < 24) {
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        } else {
            File file = (new File(apkPath));
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致 参数3 共享的文件
            Uri apkUri = FileProvider.getUriForFile(MyApplication.DHActivityManager.getManager().getTopActivity(), "com.lingmiao.distribution.fileprovider", file);
            // 添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }
        MyApplication.DHActivityManager.getManager().getTopActivity().startActivity(intent);
    }

    /**
     * android判断一个Service是否存在
     * className:service name
     */
    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;
            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检测程序是否安装
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals(packageName))
                    return true;
            }
        }
        return false;
    }

    /**
     * pathName：传值本地图片绝对路径
     */
    public static Bitmap decodeFile(String pathName, BitmapFactory.Options opts) {
        Bitmap bm = null;
        InputStream stream = null;
        try {
            stream = new FileInputStream(pathName);
            bm = decodeStream(stream, null, opts);
        } catch (Exception e) {
            /*  do nothing.If the exception happened on open, bm will be null.*/
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // do nothing here
                }
            }
        }
        return bm;
    }

    /**
     * 给一张Bitmap添加水印文字。
     *
     * @param src      源图片
     * @param user     用户名
     * @param time     上传时间
     * @param address  上传地址
     * @param textSize 水印字体大小 ，单位pix。
     * @param color    水印字体颜色。
     * @param recycle  是否回收
     * @return 已经添加水印后的Bitmap。
     */
    public static Bitmap addTextWatermark(Bitmap src, String user, String time, String address, int textSize, int color, boolean recycle, int angle) {
        if (isEmptyBitmap(src) || address == null)
            return null;
//        Bitmap ret = src.copy(src.getConfig(), true);
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        Canvas canvas = new Canvas(ret);
//        paint.setColor(color);
//        paint.setTextSize(textSize);
//
//        Rect bounds = new Rect();
//        //绘制地址
//        paint.getTextBounds(address, 0, address.length(), bounds);
//        canvas.drawText(address, 10, src.getHeight()-10, paint);
//        //绘制时间
//        paint.getTextBounds(time, 0, time.length(), bounds);
//        canvas.drawText(time, 10, src.getHeight()-60, paint);
//        //绘制姓名
//        paint.getTextBounds(user, 0, user.length(), bounds);
//        canvas.drawText(user, 10, src.getHeight()-110, paint);
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            returnBm = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) returnBm = src;

        Bitmap ret = Bitmap.createBitmap(returnBm.getWidth(), returnBm.getHeight(), returnBm.getConfig());
        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint();
        canvas.drawBitmap(returnBm, new Matrix(), paint);
        paint.setColor(Color.rgb(30, 185, 238));//取系统色
//        paint.setColor(color);
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        //绘制地址
        paint.getTextBounds(address, 0, address.length(), bounds);
        canvas.drawText(address, 10, returnBm.getHeight() - 10, paint);
        //绘制时间
        paint.getTextBounds(time, 0, time.length(), bounds);
        canvas.drawText(time, 10, returnBm.getHeight() - 60, paint);
        //绘制姓名
        paint.getTextBounds(user, 0, user.length(), bounds);
        canvas.drawText(user, 10, returnBm.getHeight() - 110, paint);
        if (!returnBm.isRecycled()) returnBm.recycle();
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * 保存图片到文件File。
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return true 成功 false 失败
     */
    public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src))
            return false;

        OutputStream os;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled())
                src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Bitmap对象是否为空。
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }


    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(String phoneNum, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        activity.startActivity(intent);
    }

    /**
     * unicode转字符串
     *
     * @param unicode
     * @return
     */
    public static String unicodeToString(String unicode) {
        StringBuffer sb = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int index = Integer.parseInt(hex[i], 16);
            sb.append((char) index);
        }
        return sb.toString();
    }

    /**
     * 获取本地文件相关绝对路径
     *
     * @return
     */
    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;//sdk版本是否大于4.4
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri)) return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 得到传入文件的大小
     */
    public static long getFileSizes(File f) {
        long s = 0;
        if (f.exists() && f.isFile()) {
            return f.length();
        }
//        if (f.exists()) {
//            FileInputStream fis = null;
//            fis = new FileInputStream(f);
//            s = fis.available();
//            fis.close();
//        } else {
//            f.createNewFile();
//        }
        return s;
    }

    /**
     * 转换文件大小成KB  M等
     */
    public static String FormentFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
//        if (fileS < 1024) {
//            fileSizeString = df.format((double) fileS) + "B";
//        } else if (fileS < 1048576) {
//            fileSizeString = df.format((double) fileS / 1024) + "K";
//        } else if (fileS < 1073741824) {
//            fileSizeString = df.format((double) fileS / 1048576) + "M";
        fileSizeString = df.format((double) fileS / 1048576);
//        } else {
//            fileSizeString = df.format((double) fileS / 1073741824) + "G";
//        }
        return fileSizeString;
    }

    private static final int NETWORK_NONE = -1;//没有连接网络
    private static final int NETWORK_MOBILE = 0;//移动网络
    private static final int NETWORK_WIFI = 1;//无线网络

    /**
     * 判断相关网络状态
     */
    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }
}
