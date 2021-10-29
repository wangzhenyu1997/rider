package com.lingmiao.distribution.util.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.util.AppUtils;
import com.lingmiao.distribution.dialog.ListDialog;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;

import java.util.ArrayList;

/**
 * Create Date : 2021/10/233:05 下午
 * Auther      : Fox
 * Desc        :
 **/
public class MapNav {

    private static ArrayList<String> mapData = new ArrayList<>();

    static {
        mapData.add("1@百度地图");
        mapData.add("2@高德地图");
        mapData.add("3@腾讯地图");
    }

    /**
     * 地图选择弹窗
     */
    public static void chooseMapDialog(Context context, String address, double mlatitude, double mlongitude) {
        if (context == null) {
            return;
        }
        ListDialog dialog = new ListDialog(context, (ListDialog.DialogItemClickListener) (position, id, text, chooseIndex) -> {//1:百度   2：高德   3.腾讯
            if (id.equals("1")) {
                if (PublicUtil.isInstalled(context, "com.baidu.BaiduMap")) {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("baidumap://map/direction?destination=" + address + "&mode=riding&coord_type=bd09ll&src=" + context.getPackageName()));
                    context.startActivity(intent);
                } else {
                    ToastUtil.showToast(context, "请先安装百度地图客户端");
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("market://details?id=com.baidu.BaiduMap"));
                    context.startActivity(intent);
                }
            } else if (id.equals("2")) {
                if (PublicUtil.isInstalled(context, "com.autonavi.minimap")) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("amapuri://openFeature?featureName=OnRideNavi&rideType=elebike&sourceApplication=" + context.getPackageName() + "&lat=" + mlatitude + "&lon=" + mlongitude + "&dev=0"));
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setPackage("com.autonavi.minimap");
                    context.startActivity(intent);
                } else {
                    ToastUtil.showToast(context, "请先安装高德地图客户端");
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("market://details?id=com.autonavi.minimap"));
                    context.startActivity(intent);
                }
            } else {
                if (PublicUtil.isInstalled(context, "com.tencent.map")) {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("qqmap://map/routeplan?type=drive&fromcoord=CurrentLocation&to=" + address + "&tocoord=" + mlatitude + "," + mlongitude + "&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77"));
                    context.startActivity(intent);
                } else {
                    ToastUtil.showToast(context, "请先安装腾讯地图客户端");
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("market://details?id=com.tencent.map"));
                    context.startActivity(intent);
                }
            }
        }, mapData, 0);
        dialog.show();
    }

}
