package com.lingmiao.distribution.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.HomeParam;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityMapBinding;
import com.lingmiao.distribution.dialog.HomeConfirmDialog;
import com.lingmiao.distribution.dialog.ListDialog;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.util.map.RideRouteOverlay;
import com.lingmiao.distribution.view.LayoutTopView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 地图页面
 * niuxinyu
 */
public class MapActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityMapBinding viewBinding;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    // 地图选择
    private ArrayList<String> mapData = new ArrayList<>();
    private DispatchOrderRecordBean mData;        //总数据源（列表带过来的）    //dispatchStatus 3前往取货   5.前往送货
    private String mPhone;     //电话

    private double mlatitude;               //纬度
    private double mlongitude;              //经度
    private String mAddress;                //地址

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityMapBinding.inflate(LayoutInflater.from(this));
        mData = (DispatchOrderRecordBean) getIntent().getSerializableExtra("data");
        setContentView(viewBinding.getRoot());
        getData();
        initMap(savedInstanceState);
        initView();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        viewBinding.topView.setData(new LayoutTopView.TopCallback() {
            @Override
            public void onTopBack() {
                finish();
            }

            @Override
            public void onRightTx() {
                startActivity(new Intent(context, HomeDetailActivity.class).putExtra("id", mData.getId()).putExtra("index", 0));
            }
        }, true, (mData.getDispatchStatus() == 3 ? "前往取货" : "前往送货"), 0, "订单详情");
        mapData.add("1@百度地图");
        mapData.add("2@高德地图");
        mapData.add("3@腾讯地图");
        viewBinding.amToTel.setOnClickListener(this);
        viewBinding.amNavigation.setOnClickListener(this);
        viewBinding.mReport.setOnClickListener(this);
        viewBinding.mTakeOver.setOnClickListener(this);
    }

    /**
     * 获取数据
     */
    @SuppressLint("SetTextI18n")
    private void getData() {
        if (mData.getDispatchStatus() == 3) {
            viewBinding.mTittle.setText("当前取货点");
            viewBinding.mTakeOver.setText("到达取货点");
            if (mData.getOrderList() != null && mData.getOrderList().size() != 0) {
                viewBinding.mName.setText(mData.getOrderList().get(0).getConsignerCustomerName());
                mPhone = mData.getOrderList().get(0).getConsignerPhone();
                mAddress = PublicUtil.isNull(mData.getOrderList().get(0).getConsignerProvince()) + PublicUtil.isNull(mData.getOrderList().get(0).getConsignerCity()) +
                        PublicUtil.isNull(mData.getOrderList().get(0).getConsignerDistrict()) + PublicUtil.isNull(mData.getOrderList().get(0).getConsignerStreet()) +
                        PublicUtil.isNull(mData.getOrderList().get(0).getConsignerAddress());
                viewBinding.mAddress.setText(mAddress);
                viewBinding.mRemark.setText(mData.getOrderList().get(0).getCustomerRemark());
                mlatitude = mData.getOrderList().get(0).getConsignerLat();
                mlongitude = mData.getOrderList().get(0).getConsignerLng();
            }
        } else {
            viewBinding.mTittle.setText("当前送货点");
            viewBinding.mTakeOver.setText("到达收货点");
            if (mData.getOrderList() != null && mData.getOrderList().size() != 0) {
                viewBinding.mName.setText(mData.getOrderList().get(0).getConsigneeName() + "," + mData.getOrderList().get(0).getConsigneePhone());
                mPhone = mData.getOrderList().get(0).getConsigneePhone();
                mAddress = PublicUtil.isNull(mData.getOrderList().get(0).getConsigneeProvince()) + PublicUtil.isNull(mData.getOrderList().get(0).getConsigneeCity()) +
                        PublicUtil.isNull(mData.getOrderList().get(0).getConsigneeDistrict()) + PublicUtil.isNull(mData.getOrderList().get(0).getConsigneeStreet()) +
                        PublicUtil.isNull(mData.getOrderList().get(0).getConsigneeAddress());
                viewBinding.mAddress.setText(mAddress);
                viewBinding.mRemark.setText(mData.getOrderList().get(0).getCustomerRemark());
                mlatitude = mData.getOrderList().get(0).getConsigneeLat();
                mlongitude = mData.getOrderList().get(0).getConsigneeLng();
            }
        }
        viewBinding.mGoodInfo.setText(mData.getGoodsInfo());
    }


    /**
     * 初始化地图组件
     */
    private void initMap(Bundle savedInstanceState) {
        viewBinding.amMap.onCreate(savedInstanceState);
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //配置参数
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(3000);//可选，设置定位间隔。默认为2秒
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        locationOption = mOption;
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        // 启动定位
//        locationClient.startLocation();
        Log.e("开始位置", "纬度" + Constant.LOCATIONLATITUDE + "，精度" + Constant.LOCATIONLONGITUDE);
        Log.e("结束位置", "纬度" + mlatitude + "，精度" + mlongitude);
        setfromandtoMarker(new LatLonPoint(Constant.LOCATIONLATITUDE, Constant.LOCATIONLONGITUDE), new LatLonPoint(mlatitude, mlongitude)); //纬度，精度
//        纬度31.826304，精度117.150425
//        2020-07-27 19:49:05.281 4785-4785/com.lingmiao.distribution E/结束位置: 纬度120.952011，精度32.402906
//        setfromandtoMarker(new LatLonPoint( 31.826304,117.150425), new LatLonPoint(32.402906,120.952011)); //纬度，精度
    }


    /**
     * 设置起始覆盖物
     */
    private void setfromandtoMarker(LatLonPoint mStartPoint, LatLonPoint mEndPoint) {
        //初始化地图控制器对象
        AMap aMap = viewBinding.amMap.getMap();
        //显示定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(3000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeWidth(0);
        myLocationStyle.radiusFillColor(Color.parseColor("#333885F7"));
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_map_start)));
        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_map_end)));

        RouteSearch mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult result, int errorCode) {
                Log.e("dingwei===", "路线规划监听=====" + result + "===" + errorCode);
                aMap.clear();// 清理地图上的所有覆盖物
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getPaths() != null) {

                        if (result.getPaths().size() > 0) {
//                            mRideRouteResult = result;
                            final RidePath ridePath = result.getPaths()
                                    .get(0);
                            RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(
                                    context, aMap, ridePath,
                                    result.getStartPos(),
                                    result.getTargetPos());
                            rideRouteOverlay.removeFromMap();
                            rideRouteOverlay.addToMap();
                            rideRouteOverlay.zoomToSpan();
                        } else if (result != null && result.getPaths() == null) {
//                            ToastUtil.show(mContext, R.string.no_result);
                        }
                    } else {
//                        ToastUtil.show(mContext, R.string.no_result);
                    }
                } else {
//                    ToastUtil.showerror(this.getApplicationContext(), errorCode);
                }
            }
        });
        aMap.setOnMapLoadedListener(() -> {
            final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
            RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT);
            mRouteSearch.calculateRideRouteAsyn(query);// 异步路径规划骑行模式查询
        });
    }


    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    sb.append("定位成功" + "==");
                    sb.append("经度: " + location.getLongitude() + " ");
                    sb.append("纬度: " + location.getLatitude() + "\n");
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                //解析定位结果，
                String result = sb.toString();
                Log.e("dingwei===", result);
            } else {
                Log.e("dingwei===", "定位失败，loc is null");
                ToastUtil.showToast(context, "");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewBinding.amMap.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewBinding.amMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewBinding.amMap.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewBinding.amMap.onSaveInstanceState(outState);
    }

    /**
     * 到达取货点
     */
    private void arriveShop(String id) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("dispatchId", id);
        mMap.put("address", PublicUtil.isNull(Constant.LOCATIONADDRESS));
        mMap.put("latitude", Constant.LOCATIONLATITUDE);
        mMap.put("longitude", Constant.LOCATIONLONGITUDE);
        OkHttpUtils.postAync(Constant.AppArriveShop, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    EventBus.getDefault().post(new PublicBean(10));
                    EventBus.getDefault().post(new PublicBean(12));
                    finish();
                }
            }
        });
    }

    /**
     * 到达送货点
     */
    private void arriveStation(String id) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("dispatchId", id);
        mMap.put("address", PublicUtil.isNull(Constant.LOCATIONADDRESS));
        mMap.put("latitude", Constant.LOCATIONLATITUDE);
        mMap.put("longitude", Constant.LOCATIONLONGITUDE);
        OkHttpUtils.postAync(Constant.AppArriveStation, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    EventBus.getDefault().post(new PublicBean(10));
                    EventBus.getDefault().post(new PublicBean(12));
                    finish();
                }
            }
        });
    }

    /**
     * 地图选择弹窗
     */
    private void chooseMapDialog() {
        ListDialog dialog = new ListDialog(context, (ListDialog.DialogItemClickListener) (position, id, text, chooseIndex) -> {//1:百度   2：高德   3.腾讯
            if (id.equals("1")) {
                if (PublicUtil.isInstalled(context, "com.baidu.BaiduMap")) {
                    Intent intent = new Intent();
//                    intent.setData(Uri.parse("baidumap://map/direction?destination=39.98871,116.43234&mode=driving&coord_type=bd09ll&src=" + context.getPackageName()));
                    intent.setData(Uri.parse("baidumap://map/direction?destination=" + mlatitude + "," + mlongitude + "&mode=riding&coord_type=bd09ll&src=" + context.getPackageName()));
                    startActivity(intent);
                } else {
                    ToastUtil.showToast(context, "请先安装百度地图客户端");
                }
            } else if (id.equals("2")) {
                if (PublicUtil.isInstalled(context, "com.autonavi.minimap")) {
//                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("amapuri://route/plan/?dlat=" + mlongitude + "&dlon=" + mlatitude + "&dname=B&dev=0&t=0"));
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("amapuri://openFeature?featureName=OnRideNavi&rideType=elebike&sourceApplication=" + context.getPackageName() + "&lat=" + mlatitude + "&lon=" + mlongitude + "&dev=0"));
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setPackage("com.autonavi.minimap");
                    startActivity(intent);
                } else {
                    ToastUtil.showToast(context, "请先安装高德地图客户端");
                }
            } else {
                if (PublicUtil.isInstalled(context, "com.tencent.map")) {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("qqmap://map/routeplan?type=drive&fromcoord=CurrentLocation&to=" + mAddress + "&tocoord=" + mlatitude + "," + mlongitude + "&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77"));
                    startActivity(intent);
                } else {
                    ToastUtil.showToast(context, "请先安装腾讯地图客户端");
                }
            }
        }, mapData, 0);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.am_to_tel:
                if (mPhone.isEmpty()) {
                    ToastUtil.showToast(context, "暂无号码！");
                    return;
                }
                PublicUtil.callPhone(mPhone, this);
                break;
            case R.id.m_report:     //上报异常
                startActivity(new Intent(context, ReportExceptionActivity.class).putExtra("id", mData.getId()));
                break;
            case R.id.m_take_over: //取货点/送货点
                if (mData.getDispatchStatus() == 3) {
                    HomeConfirmDialog dialog = new HomeConfirmDialog(context, value -> {
                        if (value) {
                            arriveShop(mData.getId());
                        }
                    }, "到达提示", "请确认是否已到达取货点？", "取消", "确认到达");
                    dialog.show();
                } else {
                    HomeConfirmDialog dialog = new HomeConfirmDialog(context, value -> {
                        if (value) {
                            arriveStation(mData.getId());
                        }
                    }, "送达提示", "请确认是否已送达收货点？", "取消", "确认送达");
                    dialog.show();
                }
                break;
            case R.id.am_navigation:
                chooseMapDialog();
                break;
            default:
                break;
        }
    }
}