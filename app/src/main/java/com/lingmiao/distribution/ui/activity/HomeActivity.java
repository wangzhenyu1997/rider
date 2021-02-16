package com.lingmiao.distribution.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.ScreenUtils;
import com.fisheagle.mkt.base.UserManager;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.adapter.VPHomeAdapter;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.app.MyApplication;
import com.lingmiao.distribution.bean.HomeModelEvent;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.bean.OrderNumBean;
import com.lingmiao.distribution.bean.PersonalBean;
import com.lingmiao.distribution.bean.PersonalDataParam;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.bean.PushBean;
import com.lingmiao.distribution.bean.UpdateBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityHomeBinding;
import com.lingmiao.distribution.dialog.HomeConfirmDialog;
import com.lingmiao.distribution.dialog.HomePushDialog;
import com.lingmiao.distribution.dialog.TakeOrderSettingDialog;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.GlideUtil;
import com.lingmiao.distribution.util.PreferUtil;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.SystemAppUtils;
import com.lingmiao.distribution.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * HomeActivity
 *
 * @author yandaocheng <br/>
 * 首页
 * 2020-07-11
 * 修改者，修改日期，修改内容
 */

public class HomeActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityHomeBinding viewBinding;
    private Boolean openState = false;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private int mIndex = 0;
    private HomePushDialog mPushDialog;
    private HomePushDialog.DialogPushConfirmClick click;

    //    private AudioPlaybackManager.OnPlayingListener mListener;  //您有新的订单语音文件
    MediaPlayer mediaPlayer;

    private HomeModelEvent mListModel = null;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityHomeBinding.inflate(LayoutInflater.from(this));
        EventBus.getDefault().register(this);
        setContentView(viewBinding.getRoot());
        initView();
        initMap();
        getNum();
        checkVersion();
        getUserInfo();
    }

    /**
     * 初始化控件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
//        viewBinding.topView.setData(new LayoutTopTwoView.TopCallback() {
//            @Override
//            public void onTopBack() { //侧滑菜单
//                viewBinding.tradeDrawer.openDrawer(Gravity.START);
//            }
//
//            @Override
//            public void onRightImage() {
//                startActivity(new Intent(context, MessageListActivity.class));
//            }
//        }, true, "鱼燕配送", R.mipmap.home_right_image, null);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemAppUtils.getStatusHeight(this) + PublicUtil.dip2px(Objects.requireNonNull(this), 44));
        viewBinding.tTopView.setLayoutParams(param1);
        viewBinding.ivTopBack.setOnClickListener(this);
        viewBinding.topRightImage.setOnClickListener(this);
        viewBinding.topTitle.setOnClickListener(this);

        VPHomeAdapter adapter = new VPHomeAdapter(getSupportFragmentManager(), 0);
        viewBinding.mViewpager.setAdapter(adapter);
//        viewBinding.mViewpager.setOffscreenPageLimit(2);
        viewBinding.mViewpager.setOffscreenPageLimit(1);
        viewBinding.mViewpager.setCurrentItem(0, false);
        viewBinding.mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mIndex = i;
                updateTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth() / 6 * 4, LinearLayout.LayoutParams.MATCH_PARENT);
        viewBinding.mAllView.setLayoutParams(param);
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //屏蔽穿透事件
        viewBinding.mAllView.setOnTouchListener((v, event) -> true);
        viewBinding.tradeDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                openState = true;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                openState = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        viewBinding.mSelectOneRe.setOnClickListener(this);
        viewBinding.mSelectTwoRe.setOnClickListener(this);
        viewBinding.mSelectThreeRe.setOnClickListener(this);
        viewBinding.mSelectOne.setSelected(true);
        viewBinding.mOneNum.setSelected(true);
        viewBinding.mTwoNum.setSelected(true);
        viewBinding.mThreeNum.setSelected(true);
        viewBinding.mOneView.setVisibility(View.VISIBLE);
        viewBinding.mOrderFee.setOnClickListener(this);
        viewBinding.mUserRe.setOnClickListener(this);
        viewBinding.mSettlement.setOnClickListener(this);
//        viewBinding.mZizhi.setOnClickListener(this);
        viewBinding.mMoney.setOnClickListener(this);
        viewBinding.mPingtai.setOnClickListener(this);
        viewBinding.mHelp.setOnClickListener(this);
        viewBinding.mSetting.setOnClickListener(this);
        viewBinding.topScanImage.setOnClickListener(this);
        viewBinding.tvHomeTakeSetting.setOnClickListener(this);
        viewBinding.btnHomeRefresh.setOnClickListener(this);
        viewBinding.btnHomeWork.setOnClickListener(this);
        viewBinding.mOrderHistory.setOnClickListener(this);
        click = new HomePushDialog.DialogPushConfirmClick() {
            @Override
            public void sure(String id) {
                HomeConfirmDialog dialog = new HomeConfirmDialog(MyApplication.DHActivityManager.getManager().getTopActivity(), value -> {
                    if (value) {
                        mPushDialog.dismiss();
                        agreeAccept(id);
                    }
                }, "接单提示", "请确认是否接单配送？", "取消", "确认接单");
                dialog.show();
            }

            @Override
            public void refuse(String id) {
                mPushDialog.dismiss();
                startActivity(new Intent(context, RejectionActivity.class).putExtra("id", id));
            }
        };
        setWorkStatus();

        mListModel = UserManager.Companion.getTakingModel();
    }

    private void setWorkStatus() {
        if (Constant.WORKSTATES == 1) {
            Drawable drawable = getDrawable(R.mipmap.ic_rest);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

            viewBinding.btnHomeWork.setText("收工");
            viewBinding.btnHomeWork.setCompoundDrawables(drawable, null , null, null);
        } else {
            Drawable drawable = getDrawable(R.mipmap.ic_rider);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            viewBinding.btnHomeWork.setText("上班");
            viewBinding.btnHomeWork.setCompoundDrawables(drawable, null , null, null);
        }
    }

    /**
     * 获取订单数量
     */
    private void getNum() {
        Map<String, Object> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppDispatchNum, new Gson().toJson(mMap), new HttpCallback<OrderNumBean>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(OrderNumBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null) {
                    if (response.getData().getReceivedNum() > 0) {
                        viewBinding.mOneNum.setVisibility(View.VISIBLE);
                        viewBinding.mOneNum.setText(response.getData().getReceivedNum() > 99 ? "(99+)" : "(" + response.getData().getReceivedNum()+")");
                    } else {
                        viewBinding.mOneNum.setVisibility(View.GONE);
                    }
                    if (response.getData().getPickupNum() > 0) {
                        viewBinding.mTwoNum.setVisibility(View.VISIBLE);
                        viewBinding.mTwoNum.setText(response.getData().getPickupNum() > 99 ? "(99+)" : "(" + response.getData().getPickupNum()+")");
                    } else {
                        viewBinding.mTwoNum.setVisibility(View.GONE);
                    }
                    if (response.getData().getArriveNum() > 0) {
                        viewBinding.mThreeNum.setVisibility(View.VISIBLE);
                        viewBinding.mThreeNum.setText(response.getData().getArriveNum() > 99 ? "(99+)" : "(" + response.getData().getArriveNum()+")");
                    } else {
                        viewBinding.mThreeNum.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * 检测app版本信息
     */
    private void checkVersion() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("appType", "1");
        mMap.put("currentVersion", PublicUtil.getAppVersionName(context));
        OkHttpUtils.postAync(Constant.AppUpdateOnLine, new Gson().toJson(mMap), new HttpCallback<UpdateBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(UpdateBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if (response.getData().isNeedUpgrade()) {
                        //检测到新版本
                        HomeConfirmDialog dialog = new HomeConfirmDialog(context, value -> {
                            if (value) {
                                Constant.downLoadUrl = response.getData().getDownloadUrl();
                                ToastUtil.showToast(context, "开始下载,请打开通知栏查看进度！");
                                if (PublicUtil.isServiceExisted(Objects.requireNonNull(context), "com.lingmiao.distribution.service.UpdateService")) {
                                    stopService();
                                    startService();
                                } else {
                                    startService();
                                }
                            }
                        }, "版本更新", "检测到新版本，是否立即更新？", "暂不更新", "立即更新");
                        dialog.show();
                    }
                }
            }
        });
    }

    /**
     * 获取个人资料数据
     */
    private void getUserInfo() {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppUserInfoDetail, new Gson().toJson(mMap), new HttpCallback<PersonalBean>() {
            @Override
            public void onSuccess(PersonalBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().getRider() != null) {
                    PersonalDataParam data = response.getData().getRider();
                    GlideUtil.load(context, data.getHeadImgUrl(), viewBinding.lpcImg, GlideUtil.getHeadImgOption());
                    viewBinding.mName.setText(data.getName());
                    viewBinding.mPhone.setText(data.getMobile());
                }
            }
        });
    }

    /**
     * 确认接单
     */
    private void agreeAccept(String id) {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("id", id);
        OkHttpUtils.postAync(Constant.AppAgreeAccept, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    EventBus.getDefault().post(new PublicBean(10));
                    getNum();
                }
            }
        });
    }

    /**
     * 初始化地图组件
     */
    private void initMap() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //配置参数
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(60000);//可选，设置定位间隔。默认为2秒
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
        locationClient.startLocation();
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = location -> {
        if (null != location) {
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.getErrorCode() == 0) {
                Constant.LOCATIONADDRESS = location.getAddress();
                Constant.LOCATIONLONGITUDE = location.getLongitude();
                Constant.LOCATIONLATITUDE = location.getLatitude();
            }
        }
    };

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void shiftModel(HomeModelEvent e) {
        if(e != null) {
            this.mListModel = e;
        }
    }

    /**
     * 收到推送消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(PublicBean event) {
        if (event.getMessage() != null && !event.getMessage().equals("")) {
            PushBean param = new Gson().fromJson(event.getMessage(), PushBean.class);
            if (param.getType() == 1) {
                try {
                    AssetFileDescriptor fd = getAssets().openFd("newinfo.mp3");
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mPushDialog == null) {
                    mPushDialog = new HomePushDialog(MyApplication.DHActivityManager.getManager().getTopActivity(), click, param.getDispatchId());
                    mPushDialog.show();
                } else {
                    if (mPushDialog.isShowing()) mPushDialog.dismiss();
                    mPushDialog = new HomePushDialog(MyApplication.DHActivityManager.getManager().getTopActivity(), click, param.getDispatchId());
                    mPushDialog.show();
                }
            }
        } else if (event.getCode() == 12) {  //获取相关订单数量
            getNum();
        } else if (event.getCode() == 20) {
            updateTab(0);
        } else if (event.getCode() == 21) {
            updateTab(1);
        }
    }

    /**
     * 更换工作状态
     */
    private void updateWorkstatus(int state) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("workStatus", state);
        OkHttpUtils.postAync(Constant.AppUpdateWorkStatus, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    Constant.WORKSTATES = state;
                    setWorkStatus();
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_back:             //侧滑菜单
                viewBinding.tradeDrawer.openDrawer(Gravity.START);
                break;
            case R.id.tv_home_take_setting:
                TakeOrderSettingDialog takeOrderSettingDialog = TakeOrderSettingDialog.newInstance();
                takeOrderSettingDialog.show(getSupportFragmentManager(), "TakeOrderSettingDialog");
                break;
            case R.id.btn_home_work:
//            case R.id.top_title:            //头部
                HomeConfirmDialog dialog = new HomeConfirmDialog(this, value -> {
                    if (value) {
                        if (Constant.WORKSTATES == 0) {
                            updateWorkstatus(1);
                        } else {
                            updateWorkstatus(0);
                        }
                    }
                }, "温馨提示", Constant.WORKSTATES == 0 ? "上班后将会接受派单，确定要上班吗？" : "下班后将不能再接到派单，确定要下班吗？", "取消", "确定");
                dialog.show();
                break;
            case R.id.top_right_image:      //消息
                startActivity(new Intent(context, MessageListActivity.class));
                break;
            case R.id.m_select_one_re:      //待取货
                viewBinding.mViewpager.setCurrentItem(0);
                break;
            case R.id.m_select_two_re:      //待送达
                viewBinding.mViewpager.setCurrentItem(1);
                break;
            case R.id.m_select_three_re:    //已结束
                viewBinding.mViewpager.setCurrentItem(2);
                break;
            case R.id.m_user_re:            //用户信息
                startActivityForResult(new Intent(context, PersonalInfoActivity.class), 100);
                break;
            case R.id.m_order_fee:          //订单费用
                startActivity(new Intent(context, OrderFeeListActivity.class));
                break;
            case R.id.m_settlement:         //结算对账
                startActivity(new Intent(context, BillListActivity.class));
                break;
//            case R.id.m_zizhi:              //我的资质
////                startActivity(new Intent(context, ComplaintActivity.class));
////                break;
            case R.id.m_money:              //我的钱包
                startActivity(new Intent(context, MyWalletActivity.class));
                break;
            case R.id.m_pingtai:            //平台规则
                startActivity(new Intent(context, PlatformRulesActivity.class));
                break;
            case R.id.m_help:               //帮助中心
                startActivity(new Intent(context, HelpCenterActivity.class));
                break;
            case R.id.m_setting:            //设置
                startActivity(new Intent(context, SettingActivity.class));
                break;
            case R.id.top_scan_image:
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                // 设置自定义扫描Activity
                intentIntegrator.setCaptureActivity(ScanOrderActivity.class);
                intentIntegrator.initiateScan();
                break;
            case R.id.m_order_history:
                startActivity(new Intent(context, HistoryOrderActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 更改tab效果
     */
    private void updateTab(int index) {
        if (index == 0) {
            viewBinding.mSelectOne.setSelected(true);
            viewBinding.mSelectTwo.setSelected(false);
            viewBinding.mSelectThree.setSelected(false);
            viewBinding.mOneView.setVisibility(View.VISIBLE);
            viewBinding.mTwoView.setVisibility(View.GONE);
            viewBinding.mThreeView.setVisibility(View.GONE);
        } else if (index == 1) {
            viewBinding.mSelectOne.setSelected(false);
            viewBinding.mSelectTwo.setSelected(true);
            viewBinding.mSelectThree.setSelected(false);
            viewBinding.mOneView.setVisibility(View.GONE);
            viewBinding.mTwoView.setVisibility(View.VISIBLE);
            viewBinding.mThreeView.setVisibility(View.GONE);
        } else {
            viewBinding.mSelectOne.setSelected(false);
            viewBinding.mSelectTwo.setSelected(false);
            viewBinding.mSelectThree.setSelected(true);
            viewBinding.mOneView.setVisibility(View.GONE);
            viewBinding.mTwoView.setVisibility(View.GONE);
            viewBinding.mThreeView.setVisibility(View.VISIBLE);
        }
        getNum();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            getUserInfo();
        }
    }

    public int getIndex() {
        return mIndex;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (null != locationClient) {
            locationClient.onDestroy();
        }
    }

    //双击退出应用
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (openState) {
                viewBinding.tradeDrawer.closeDrawers();
            } else if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showToast(this, "再按一次返回桌面");
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

