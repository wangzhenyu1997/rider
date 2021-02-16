package com.lingmiao.distribution.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.mapcore.util.it
import com.blankj.utilcode.util.KeyboardUtils
import com.fisheagle.mkt.business.photo.PhotoHelper
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.PublicBean
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.dialog.DialogShowImageFragment
import com.lingmiao.distribution.dialog.HomeConfirmDialog
import com.lingmiao.distribution.ui.common.bean.UploadDataBean
import com.lingmiao.distribution.ui.common.pop.MediaMenuPop
import com.lingmiao.distribution.ui.main.adapter.ImageUploadAdapter
import com.lingmiao.distribution.ui.main.adapter.RemarkAdapter
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.event.RefreshDispatchStatusEvent
import com.lingmiao.distribution.ui.main.presenter.IOperateSignPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.OperateSignPreImpl
import com.lingmiao.distribution.ui.photo.convert2GalleryVO
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import kotlinx.android.synthetic.main.main_activity_operate_sign.*
import kotlinx.android.synthetic.main.main_adapter_remark.*
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList

/**
Create Date : 2021/1/57:32 PM
Auther      : Fox
Desc        :
 **/
class OperateSignActivity : BaseActivity<IOperateSignPresenter>(), IOperateSignPresenter.View {

    private var mImageAdapter: ImageUploadAdapter? = null;

    private var remarkAdapter : RemarkAdapter? = null;

    private var id : String? = "";

    private var ids : ArrayList<String>?= null;

    private var type : Int? = 0;

    private var locationClient: AMapLocationClient? = null

    private var locationOption: AMapLocationClientOption? = null

    private var currentLocation : AMapLocation ? = null;

    private var isSelfTake : Int ? = null

    companion object {

        fun batch(context: Context, isSelfTake : Int, ids: ArrayList<String>, code : Int) {
            if(context is Activity) {
                val intent = Intent(context, OperateSignActivity::class.java)
                intent.putStringArrayListExtra("ids", ids);
                intent.putExtra("signType", DispatchConstants.TYPE_ORDER_BATCH)
                intent.putExtra("isSelfTake", isSelfTake)
                context.startActivityForResult(intent, code);
            }
        }
    }

    override fun initBundles() {
        ids = intent?.getStringArrayListExtra("ids")
        id = intent?.getStringExtra("id");
        type = intent?.getIntExtra("signType", DispatchConstants.TYPE_DISPATCH);
        isSelfTake = intent?.getIntExtra("isSelfTake", 1);
    }

    override fun createPresenter(): IOperateSignPresenter {
        return OperateSignPreImpl(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_operate_sign;
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle("拍照签收");

        addressDetailTv.setText(Constant.LOCATIONADDRESS);

        initImageAdapter();

        batchSignedTv.setOnClickListener {
            if(type == DispatchConstants.TYPE_ORDER_BATCH) {
                if(ids == null || ids?.size == 0) {
                    return@setOnClickListener;
                }
            } else {
                if(id?.isEmpty() == true) {
                    finish();
                    return@setOnClickListener;
                }
            }
            if(mImageAdapter?.data?.size == 1) {
                showToast("请上传照片");
                return@setOnClickListener;
            }
            val dialog = HomeConfirmDialog(
                context,
                HomeConfirmDialog.DialogHomeConfirmClick { value: Boolean ->
                    if (value) {
                        var urlsStr = mImageAdapter?.data?.map { it?.url }?.joinToString(separator = ",") ?: "";

                        if(type == DispatchConstants.TYPE_ORDER_BATCH) {
                            mPresenter?.batchSign(ids, urlsStr, searchSignRemarksEt?.text.toString(), if(shortMessageCb.isChecked) 1 else 0);
                        } else {
                            mPresenter?.sign(id!!, urlsStr, type!!);
                        }
                    }
                }, "签收提示", "签收后将完成不可更改，是否确认签收？", "取消", "确认签收"
            )
            dialog.show()
        }
        clearSearchTv.setOnClickListener {
            searchSignRemarksEt.setText("")
        }

        initRemarksAdapter();
//        initMap();
    }

    private fun initRemarksAdapter() {
        remarkAdapter = RemarkAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.getItem(position)
                searchSignRemarksEt.setText(String.format("%s%s%s", searchSignRemarksEt.text, if(searchSignRemarksEt.text.isNotEmpty()) "," else "", item));
            }
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.getItem(position)
            }
        }
        remarkAdapter?.addData("尊敬的客户您好，由于您现在不方便收货");

        remarkAdapter?.addData(String.format("我是门口淘配送员%s（电话%s）", Constant.user?.name, Constant.user?.mobile))

        remarkAdapter?.addData("包裹给您放在小区门卫了，请记得及时领取")

        remarkAdapter?.addData("如有问题，可随时联系我或反馈团长及平台客服")

        remarksRv?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = remarkAdapter
        }

        if(isSelfTake == 0) {
            remarksRv.visiable()
            shortMessageCb.visiable()
            clearSearchTv.visiable()
        }
    }

    fun initImageAdapter() {
        mImageAdapter = ImageUploadAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                var item = getItem(position);
                if(view?.id == R.id.imageIv) {
                    if(item?.path?.isNotBlank() == true) {
                        val imageFragment = DialogShowImageFragment()
                        imageFragment.setData(arrayListOf(item?.path), position)
                        imageFragment.show(supportFragmentManager, null)
                    } else {
                        openGallery();
                    }
                } else if(view?.id == R.id.deleteIv) {
                    mImageAdapter?.remove(position)
                    addNewNode();
                }
            }
            setOnItemClickListener { adapter, view, position ->
                var item = getItem(position);
            }
        }

        rvImage?.apply {
            layoutManager = GridLayoutManager(context, UploadDataBean.COLUMN_COUNT_5)
            adapter = mImageAdapter
        }
        addNewNode();
    }

    fun addNewNode() {
        // 是否为空数据
        val isEmpty = (mImageAdapter?.data?.size ?:0) == 0;

        // 最后一个是否为空
        val hasNext = mImageAdapter?.itemCount?:0 < UploadDataBean.MAX_SIZE_5 && (!isEmpty && mImageAdapter?.data?.last()?.path?.isEmpty() == false)

        if(isEmpty || hasNext) {
            val item = UploadDataBean();
            item.id = String.format("%s", mImageAdapter?.itemCount ?: 0);
            mImageAdapter?.addData(item);
        }
    }

    fun addDataList(list: List<UploadDataBean>?) {
        if (list.isNullOrEmpty() || list?.size == 0) {
            return
        }
        mPresenter?.uploadFile(list?.get(0)?.path!!);
    }

    /**
     * 当次最多可选择的照片数
     */
    fun getOnceMaxPickerSize() : Int {
        //MAX_SIZE - (mImageAdapter?.itemCount?:0
        return UploadDataBean.MAX_ONCE_PICKER_COUNT;
    }

    private fun openGallery() {
        val menus = MediaMenuPop.TYPE_SELECT_PHOTO or MediaMenuPop.TYPE_PLAY_PHOTO
        MediaMenuPop(context, menus).apply {
            setOnClickListener { type ->
                when (type) {
                    // 相册
                    MediaMenuPop.TYPE_SELECT_PHOTO -> {
                        PhotoHelper.openAlbum(context as Activity, getOnceMaxPickerSize(), null) {
                            addDataList(convert2GalleryVO(it))
                        }
                    }
                    // 拍照
                    MediaMenuPop.TYPE_PLAY_PHOTO -> {
                        PhotoHelper.openCamera(context as Activity, null) {
                            addDataList(convert2GalleryVO(it))
                        }
                    }
                }
            }
        }.showPopupWindow()
    }

    override fun onUploadFileSuccess(path : String, data: UploadDataBean) {
        val position = (mImageAdapter?.itemCount?:1) -1;
        mImageAdapter?.data?.get(position)?.path = path;
        mImageAdapter?.data?.get(position)?.url = data?.url;

        addNewNode();
        mImageAdapter?.notifyDataSetChanged();
    }

    override fun signSuccess() {
        EventBus.getDefault().post(PublicBean(10))
        EventBus.getDefault().post(PublicBean(12))
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onSignedFail() {

    }

    /**
     * 初始化地图组件
     */
    private fun initMap() {
        //初始化client
        locationClient = AMapLocationClient(this.applicationContext)
        //配置参数
        val mOption = AMapLocationClientOption()
        //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.isGpsFirst = true
        //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.httpTimeOut = 30000
        //可选，设置定位间隔。默认为2秒
        mOption.interval = 60000
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
        showDialogLoading();
    }

    /**
     * 定位监听
     */
    var locationListener = AMapLocationListener { location: AMapLocation? ->
        if (null != location) {
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.errorCode == 0) {
                currentLocation = location;
                setLocatinInfo();

                Constant.LOCATIONADDRESS = location.address
                Constant.LOCATIONLONGITUDE = location.longitude
                Constant.LOCATIONLATITUDE = location.latitude
            }
            hideDialogLoading()
        }
    }

    private fun setLocatinInfo() {
        addressDetailTv.setText(currentLocation?.address);
        addressNameTv.setText(currentLocation?.aoiName);
        if(currentLocation?.aoiName?.isNotBlank() == true) {
            addressNameTv.visiable();
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationClient != null) {
            locationClient?.onDestroy()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            // 当键盘未关闭时先拦截事件
            if(KeyboardUtils.isSoftInputVisible(context)) {
                KeyboardUtils.hideSoftInput(context);
//                return true;
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}