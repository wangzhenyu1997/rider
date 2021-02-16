package com.lingmiao.distribution.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.mapcore.util.id
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.BasicListParam
import com.lingmiao.distribution.bean.PublicBean
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.main.adapter.ReasonAdapter
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.OrderSignFailedReq
import com.lingmiao.distribution.ui.main.event.RefreshDispatchStatusEvent
import com.lingmiao.distribution.ui.main.presenter.IOperateSignFailPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.OperateSignFailPreImpl
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.main_activity_operate_fail.*
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList

/**
Create Date : 2021/1/77:30 PM
Auther      : Fox
Desc        : 签收失败
 **/
class OperateSignFailActivity : BaseActivity<IOperateSignFailPresenter>(), IOperateSignFailPresenter.View {

    private var mAdapter : ReasonAdapter? = null

    private var id : String? = "";

    private var ids : ArrayList<String>?= null;

    private var type : Int? = 0;

    companion object {

        fun open(context: Context, id : String, type : Int, code : Int) {
            if(context is Activity) {
                val intent = Intent(context, OperateSignFailActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("signType", type)
                context.startActivityForResult(intent, code);
            }
        }

        fun order(context: Context, id : String, code : Int) {
            open(context, id, DispatchConstants.TYPE_ORDER, code);
        }

        fun dispatch(context: Context, id : String, code : Int) {
            open(context, id, DispatchConstants.TYPE_DISPATCH, code);
        }

        fun batch(context: Context, ids: ArrayList<String>, code : Int) {
            if(context is Activity) {
                val intent = Intent(context, OperateSignFailActivity::class.java)
                intent.putStringArrayListExtra("ids", ids);
                intent.putExtra("signType", DispatchConstants.TYPE_ORDER_BATCH)
                context.startActivityForResult(intent, code);
            }
        }
    }

    override fun initBundles() {
        ids = intent?.getStringArrayListExtra("ids")
        id = intent?.getStringExtra("id");
        type = intent?.getIntExtra("signType", DispatchConstants.TYPE_DISPATCH);
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_operate_fail
    }

    override fun createPresenter(): IOperateSignFailPresenter {
        return OperateSignFailPreImpl(this);
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle("签收失败")

        mAdapter = ReasonAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                var item = getItem(position);
                when (view.id) {
                    R.id.m_content -> {
                        if(item?.isSelectState == true) {
                            item?.isSelectState = false;
                        } else {
                            data?.forEachIndexed { index, it ->
                                it.isSelectState = false;
                            }
                            item?.isSelectState = true;
                        }
                        notifyDataSetChanged()
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->
                var item = getItem(position);
            }
        }
        rvReason.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        tvSubmit.setOnClickListener {
            val list = mAdapter?.data?.filter { it?.isSelectState() == true }
            if(list?.size?:0 == 0) {
                showToast("请选择失败原因！")
                return@setOnClickListener
            }
            var mRefuseAcceptType = list?.get(0)?.value
            var mRefuseAcceptTypeName = list?.get(0)?.label
            if(mRefuseAcceptType?.isEmpty() == true) {
                showToast("请选择失败原因！")
                return@setOnClickListener
            }
            if (mRefuseAcceptTypeName != null && mRefuseAcceptTypeName == "其他") {
                if(etContent.text.toString().isEmpty()) {
                    showToast("请输入详细内容")
                    return@setOnClickListener
                }
            }
            if(type == DispatchConstants.TYPE_ORDER_BATCH && (ids == null || ids?.size == 0)) {
                return@setOnClickListener
            }
            if(type == DispatchConstants.TYPE_ORDER_BATCH) {
                mPresenter?.batchSignFail(ids, mRefuseAcceptType, mRefuseAcceptTypeName, etContent.text.toString())
            } else {
                mPresenter?.signFail(id, type, mRefuseAcceptType, mRefuseAcceptTypeName, etContent.text.toString())
            }
        }
        mPresenter?.getReasonList()
    }

    override fun setReasonList(list: List<BasicListParam>?) {
        mAdapter?.replaceData(list ?: arrayListOf())
    }

    override fun getReasonFail() {
        EventBus.getDefault().post(PublicBean(10))
        EventBus.getDefault().post(PublicBean(12))
        finish()
    }

    override fun signed() {
        EventBus.getDefault().post(PublicBean(10))
        EventBus.getDefault().post(PublicBean(12))
        setResult(Activity.RESULT_OK)
        finish()
    }

}