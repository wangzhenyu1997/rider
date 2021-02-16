package com.lingmiao.distribution.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.BasicListParam
import com.lingmiao.distribution.bean.PublicBean
import com.lingmiao.distribution.ui.main.adapter.ReasonAdapter
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.presenter.IOperatePickFailPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.OperatePickFailPreImpl
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.main_activity_operate_fail.*
import org.greenrobot.eventbus.EventBus

/**
Create Date : 2021/1/77:29 PM
Auther      : Fox
Desc        : 取货失败
 **/
class OperatePickFailActivity : BaseActivity<IOperatePickFailPresenter>(), IOperatePickFailPresenter.View {

    private var mAdapter : ReasonAdapter? = null

    private var id : String? = "";

    private var type : Int? = 0;

    companion object {

        fun open(context: Context, id : String, type : Int, code : Int) {
            if(context is Activity) {
                val intent = Intent(context, OperatePickFailActivity::class.java)
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
    }

    override fun initBundles() {
        id = intent?.getStringExtra("id");
        type = intent?.getIntExtra("signType", DispatchConstants.TYPE_DISPATCH);
    }

    override fun createPresenter(): IOperatePickFailPresenter {
        return OperatePickFailPreImpl(this);
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle("取货失败")

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
            mPresenter?.pickFail(id, type, mRefuseAcceptType, mRefuseAcceptTypeName, etContent.text.toString())
        }

        mPresenter?.getReasonList()
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_operate_fail;
    }

    override fun setReasonList(list: List<BasicListParam>?) {
        mAdapter?.replaceData(list ?: arrayListOf())
    }

    override fun picked() {
        EventBus.getDefault().post(PublicBean(10))
        EventBus.getDefault().post(PublicBean(12))
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun getReasonFail() {
        finish()
    }

}