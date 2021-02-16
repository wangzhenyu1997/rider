package com.lingmiao.distribution.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.fisheagle.mkt.business.photo.PhotoHelper
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.BasicListParam
import com.lingmiao.distribution.bean.PublicBean
import com.lingmiao.distribution.dialog.DialogShowImageFragment
import com.lingmiao.distribution.dialog.ListDialog
import com.lingmiao.distribution.dialog.ListDialog.DialogItemClickListener
import com.lingmiao.distribution.ui.common.bean.UploadDataBean
import com.lingmiao.distribution.ui.common.pop.MediaMenuPop
import com.lingmiao.distribution.ui.main.adapter.ImageUploadAdapter
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.OrderExceptionReq
import com.lingmiao.distribution.ui.main.presenter.IOperateExceptionPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.OperateExceptionPreImpl
import com.lingmiao.distribution.ui.photo.convert2GalleryVO
import com.lingmiao.distribution.util.InputUtil
import com.lingmiao.distribution.util.PublicUtil
import com.lingmiao.distribution.util.ToastUtil
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.main_activity_operate_exception.*
import kotlinx.android.synthetic.main.main_activity_operate_fail.*
import kotlinx.android.synthetic.main.main_activity_operate_sign.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
Create Date : 2021/1/77:27 PM
Auther      : Fox
Desc        : 异常上报
 **/
class OperateExceptionActivity : BaseActivity<IOperateExceptionPresenter>(), IOperateExceptionPresenter.View {

    private var mImageAdapter: ImageUploadAdapter? = null;

    private var mTimeChoose: TimePickerView? = null

    private var dialog: ListDialog? = null
    // 异常数据
    private var mExceptionData: MutableList<String>? = null

    private var id : String? = "";

    private var type : Int? = 0;

    private var mExceptionTypeId : String?= "";

    companion object {

        fun open(context: Context, id : String, type : Int, code : Int) {
            if(context is Activity) {
                val intent = Intent(context, OperateExceptionActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("sourceType", type)
                context.startActivityForResult(intent, code);
            }
        }

        fun order(context: Context, id : String, code : Int) {
            open(context, id, DispatchConstants.TYPE_ORDER, code);
        }

        fun dispatch(context: Context, id : String, code : Int) {
            open(context, id, DispatchConstants.TYPE_DISPATCH, code);
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_operate_exception;
    }

    override fun createPresenter(): IOperateExceptionPresenter {
        return OperateExceptionPreImpl(this)
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle("上报异常");


        initImageAdapter();

        initLunarPicker()

        tvExceptionType.setOnClickListener {
            if (mExceptionData?.size?:0 == 0) {
                ToastUtil.showToast(context, "暂无异常类型！")
                return@setOnClickListener
            }
            if (dialog == null) dialog = ListDialog(
                context,
                DialogItemClickListener { position: Int, id: String, text: String?, chooseIndex: Int ->
                    mExceptionTypeId = id
                    tvExceptionType.setText(text)
                },
                mExceptionData,
                0
            )
            dialog!!.show()
        }
        tvSubmit.setOnClickListener {
            if (!InputUtil.isEmpty(context, mExceptionTypeId, "请选择异常类型！")) {
                return@setOnClickListener
            }
            if (!InputUtil.isEmpty(context, tvExceptionTime.getText().toString(),"请选择异常时间！")) {
                return@setOnClickListener
            }
            if (!InputUtil.isEmpty(context, etDiscription.getText().toString(), "请输入异常描述！")) {
                return@setOnClickListener
            }

            if (mImageAdapter?.data?.size == 0) {
                ToastUtil.showToast(context, "请上传照片！")
                return@setOnClickListener
            }
            val req = OrderExceptionReq();
            req.exceptionTime = tvExceptionTime.text.toString();
            req.exceptionType = mExceptionTypeId;
            if(type == DispatchConstants.TYPE_ORDER) {
                req.orderId = id;
            } else {
                req.dispatchId = id;
            }
            req.content = etDiscription.text.toString()
            req.urls = mImageAdapter?.data?.map { it?.url }?.joinToString(separator = ",") ?: "";
            mPresenter?.submit(req, type!!);
        }
    }

    override fun setExceptionList(list: List<BasicListParam>?) {
        mExceptionData = arrayListOf();
        list?.forEachIndexed { index, item ->
            mExceptionData?.add(item?.getValue() + "@" + item?.getLabel())
        }

    }

    override fun getExceptionFail() {
        finish()
    }

    fun initLunarPicker() {
        val selectedDate = Calendar.getInstance() //系统当前时间
        val startDate = Calendar.getInstance()
        startDate[2019, 0] = 1
        val endDate = Calendar.getInstance()
        endDate[2029, 11] = 31
        //时间选择器 ，自定义布局
        mTimeChoose = TimePickerBuilder(
            context,
            OnTimeSelectListener { date: Date?, v: View ->  //选中事件回调
                if (v.id == R.id.ac_exception_time) {
                    tvExceptionTime.setText(
                        PublicUtil.getTime(
                            date,
                            "yyyy-MM-dd HH:mm:ss"
                        )
                    )
                }
            }
        )
        .setDate(selectedDate)
        .setRangDate(startDate, endDate)
        .setType(booleanArrayOf(true, true, true, true, true, true))
        .setLayoutRes(R.layout.pickerview_custom_lunar) { v: View ->
            val tvSubmit = v.findViewById<TextView>(R.id.tv_finish)
            val ivCancel =
                v.findViewById<ImageView>(R.id.iv_cancel)
            tvSubmit.setOnClickListener { v1: View? ->
                mTimeChoose!!.returnData()
                mTimeChoose!!.dismiss()
            }
            ivCancel.setOnClickListener { v12: View? -> mTimeChoose!!.dismiss() }
        }
        .isDialog(true)
        .build()

        tvExceptionTime.setOnClickListener {
            mTimeChoose?.show(it)
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
            layoutManager = GridLayoutManager(context, UploadDataBean.COLUMN_COUNT_3)
            adapter = mImageAdapter
        }
        addNewNode();
    }

    fun addNewNode() {
        // 是否为空数据
        val isEmpty = (mImageAdapter?.data?.size ?:0) == 0;

        // 最后一个是否为空
        val hasNext = mImageAdapter?.itemCount?:0 < UploadDataBean.MAX_SIZE_3 && (!isEmpty && mImageAdapter?.data?.last()?.path?.isEmpty() == false)

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

    override fun submitSuccess() {
        EventBus.getDefault().post(PublicBean(10))
        EventBus.getDefault().post(PublicBean(11))
        EventBus.getDefault().post(PublicBean(12))
        setResult(Activity.RESULT_OK)
        finish()
    }

}