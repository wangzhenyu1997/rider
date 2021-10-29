package com.lingmiao.distribution.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDialog
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import cn.jpush.android.api.JPushInterface
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.UIData
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.blankj.utilcode.util.*
import com.fisheagle.mkt.base.UserManager
import com.google.zxing.integration.android.IntentIntegrator
import com.jaeger.library.StatusBarUtil
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.doIntercept
import com.james.common.utils.permission.interceptor.LocationInterceptor
import com.james.common.utils.permission.interceptor.StorageInterceptor
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.PersonalDataParam
import com.lingmiao.distribution.bean.UpdateBean
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.location.AmapLocationProvider
import com.lingmiao.distribution.location.LocInterceptor
import com.lingmiao.distribution.location.WorkProvider
import com.lingmiao.distribution.ui.activity.*
import com.lingmiao.distribution.ui.login.bean.LoginBean
import com.lingmiao.distribution.ui.main.bean.UploadPointVo
import com.lingmiao.distribution.ui.main.event.MainToSearchEvent
import com.lingmiao.distribution.ui.main.fragment.DispatchTabFragment
import com.lingmiao.distribution.ui.main.presenter.IMainPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.MainPreImpl
import com.lingmiao.distribution.util.GlideUtil
import com.lingmiao.distribution.util.PublicUtil
//import com.lingmiao.distribution.util.StatusBarUtil
import com.lingmiao.distribution.util.VoiceUtils
import kotlinx.android.synthetic.main.main_activity_main.*
import kotlinx.android.synthetic.main.main_layout_left_drawer.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
Create Date : 2020/12/263:38 PM
Auther      : Fox
Desc        :
 **/
class MainActivity : BaseActivity<IMainPresenter>(), IMainPresenter.View {

    // 抽屉打开状态
    private var openState = false

    //双击退出应用
    private var mExitTime: Long = 0

    private var locationClient: AMapLocationClient? = null

    private var locationOption: AMapLocationClientOption? = null

    private var versionUpdateDialog: AppCompatDialog? = null

    override fun createPresenter(): IMainPresenter {
        return MainPreImpl(this)
    }

    override fun getLayoutId() = R.layout.main_activity_main


    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(context, ContextCompat.getColor(context, R.color.colorPrimary))

        initHeader()
        initDrawer()

        useStoragePermission()

        initMap()

        initRequest()

        FragmentUtils.replace(
            supportFragmentManager,
            DispatchTabFragment.newInstance(),
            R.id.frDispatchTab
        )
    }

    private fun initRequest() {
        mPresenter?.checkVersion(PublicUtil.getAppVersionName(context));
    }

    private fun initHeader() {
        iv_top_back.setOnClickListener {
            trade_drawer.openDrawer(Gravity.LEFT)
        }
        top_right_image.setOnClickListener {
            startActivity(Intent(context, MessageListActivity::class.java))
        }
        top_scan_image.setOnClickListener {
            // 设置自定义扫描Activity
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.captureActivity = ScanOrderActivity::class.java
            intentIntegrator.initiateScan();
        }
        top_search_image.setOnClickListener {
            EventBus.getDefault().post(MainToSearchEvent())
        }
    }

    private fun initDrawer() {
        //我
        m_user_re.setOnClickListener {
            startActivityForResult(Intent(context, PersonalInfoActivity::class.java), 100)
        }
        // 历史订单
        m_order_history.setOnClickListener {
            startActivity(Intent(context, HistoryListActivity::class.java))
        }
        // 订单费用
        m_order_fee.setOnClickListener {
            startActivity(Intent(context, OrderFeeListActivity::class.java))
        }
        // 结算对账
        m_settlement.setOnClickListener {
            startActivity(Intent(context, BillListActivity::class.java))
        }
        // 我的钱包
        m_money.setOnClickListener {
            startActivity(Intent(context, MyWalletActivity::class.java))
        }
        // 平台规则
        m_pingtai.setOnClickListener {
            startActivity(Intent(context, PlatformRulesActivity::class.java))
        }
        // 帮助中心
        m_help.setOnClickListener {
            startActivity(Intent(context, HelpCenterActivity::class.java))
        }
        // 设置
        m_setting.setOnClickListener {
            startActivity(Intent(context, SettingActivity::class.java))
        }

        var listener = object : DrawerListener {
            /**
             * Called when the drawer motion state changes. The new state will
             * be one of [.STATE_IDLE], [.STATE_DRAGGING] or [.STATE_SETTLING].
             *
             * @param newState The new drawer motion state
             */
            override fun onDrawerStateChanged(newState: Int) {

            }

            /**
             * Called when a drawer's position changes.
             * @param drawerView The child view that was moved
             * @param slideOffset The new offset of this drawer within its range, from 0-1
             */
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            /**
             * Called when a drawer has settled in a completely closed state.
             *
             * @param drawerView Drawer view that is now closed
             */
            override fun onDrawerClosed(drawerView: View) {
                openState = false
            }

            /**
             * Called when a drawer has settled in a completely open state.
             * The drawer is interactive at this point.
             *
             * @param drawerView Drawer view that is now open
             */
            override fun onDrawerOpened(drawerView: View) {
                openState = true
            }

        };
        trade_drawer.addDrawerListener(listener);

        val param = LinearLayout.LayoutParams(
            ScreenUtils.getScreenWidth() / 6 * 4,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        m_all_view.setLayoutParams(param)
        m_all_view.setOnTouchListener { v, event -> true };
    }

    override fun onResume() {
        super.onResume()
        if (Constant.user == null) {
            getUserInfo();
        } else {
            setUserView();
        }
        val rid = JPushInterface.getRegistrationID(applicationContext)
        LogUtils.e("push", "[registrationID] $rid")
        WorkProvider.post(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeTabPosition(event: LoginBean) {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (openState) {
                trade_drawer.closeDrawers()
            } else if (System.currentTimeMillis() - mExitTime > 2000) {
                showToast("再按一次返回桌面")
                mExitTime = System.currentTimeMillis()
            } else {
                AppUtils.exitApp();
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun useStoragePermission() {
        doIntercept(StorageInterceptor(this), failed = {}) {

        }
    }

    /**
     * 初始化地图组件
     */
    private fun initMap() {
        doIntercept(LocationInterceptor(context), failed = {}) {
        }
        locate();

        val manager = NotificationManagerCompat.from(this)
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
        val isOpened: Boolean = manager.areNotificationsEnabled()
        if (!isOpened) {
            try {
                // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                val intent = Intent()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, applicationInfo.uid)

                    //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                    intent.putExtra("app_package", packageName)
                    intent.putExtra("app_uid", applicationInfo.uid)
                } else {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName(
                        "com.android.settings",
                        "com.android.settings.InstalledAppDetails"
                    );
                    intent.putExtra("com.android.settings.ApplicationPkgName", packageName);
                }
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
                val intent = Intent()

                //下面这种方案是直接跳转到当前应用的设置界面。
                //https://blog.csdn.net/ysy950803/article/details/71910806
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }

    fun locate() {
        //初始化client
        locationClient = AMapLocationClient(this.applicationContext)
        //配置参数
        val mOption = AMapLocationClientOption()
        //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.isGpsFirst = true
        //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.httpTimeOut = 15000
        //可选，设置定位间隔。默认为2秒
        mOption.interval = 15000
        //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP)
        //可选，设置是否使用传感器。默认是false
        mOption.isSensorEnable = false
        //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.isWifiScan = true
        //可选，设置是否使用缓存定位，默认为true
        mOption.isLocationCacheEnable = true
        //可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        mOption.geoLanguage = AMapLocationClientOption.GeoLanguage.DEFAULT
        locationOption = mOption
        //设置定位参数
        locationClient?.setLocationOption(locationOption)
        // 设置定位监听
        locationClient?.setLocationListener(locationListener)
        // 启动定位
        locationClient?.startLocation()
    }

    /**
     * 定位监听
     */
    var locationListener = AMapLocationListener { location: AMapLocation? ->
        if (null != location) {
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.errorCode == 0) {
                Constant.LOCATIONADDRESS = location.address
                Constant.LOCATIONLONGITUDE = location.longitude
                Constant.LOCATIONLATITUDE = location.latitude


                val item = UploadPointVo();
                item.riderId = Constant.user.id;
                item.lat = location.latitude;
                item.lng = location.longitude;
                mPresenter?.uploadPoint(item);
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 200) {
            getUserInfo()
        }
    }

    fun getUserInfo() {
        mPresenter?.getUser();
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationClient != null) {
            locationClient?.onDestroy()
        }
        VoiceUtils.release()
        AmapLocationProvider.getInstance().stopLocation();
    }

    override fun checkVersionSuccess(response: UpdateBean?) {
        if (response == null || response.downloadUrl?.isEmpty() == true || !response.isNeedUpgrade) {
            return;
        }
        versionUpdateDialog = DialogUtils.showVersionUpdateDialog(
            context!!,
            "版本更新",
            if (response.upgradeContent?.isNotBlank() == true) response.upgradeContent else "检测到新版本，是否立即更新？",
            "暂不更新",
            null,
            "立即更新",
            View.OnClickListener {
                val builder = AllenVersionChecker
                    .getInstance()
                    .downloadOnly(crateUIData(response))
                builder.isDirectDownload = true
                builder.isShowNotification = false
                builder.isShowDownloadingDialog = true
                builder.isShowDownloadFailDialog = false
                builder.executeMission(Utils.getApp())
            }, response.isCastUpdate
        )
    }

    private fun crateUIData(bean: UpdateBean): UIData? {
        return UIData.create().setTitle("版本更新").setContent(bean.upgradeContent)
            .setDownloadUrl(bean.downloadUrl)
    }

    override fun setUser(user: PersonalDataParam?) {
        if (Constant.user != null) {
            UserManager.setUserInfo(Constant.user);
        }
        setUserView();
    }

    fun setUserView() {
        Constant.user?.apply {
            m_name.setText(name);
            m_phone.setText(mobile);
            GlideUtil.load(
                context,
                getHeadImgUrl(),
                lpc_img,
                GlideUtil.getHeadImgOption()
            )
        }
    }

}