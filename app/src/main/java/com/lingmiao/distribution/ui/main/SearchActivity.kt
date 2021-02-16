package com.lingmiao.distribution.ui.main

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.app.MyApp
import com.lingmiao.distribution.bean.PublicBean
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.dialog.HomeConfirmDialog
import com.lingmiao.distribution.ui.activity.*
import com.lingmiao.distribution.ui.main.adapter.SearchAdapter
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.event.DispatchSingleNumberEvent
import com.lingmiao.distribution.ui.main.event.RefreshDispatchStatusEvent
import com.lingmiao.distribution.ui.main.fragment.DispatchListFragment
import com.lingmiao.distribution.ui.main.presenter.ISearchPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.SearchPreImpl
import com.lingmiao.distribution.util.PublicUtil
import com.lingmiao.distribution.widget.EmptyView
import com.jaeger.library.StatusBarUtil
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.view.EmptyLayout
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.main_activity_search.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
Create Date : 2021/1/2010:51 AM
Auther      : Fox
Desc        :
 **/
class SearchActivity : BaseLoadMoreActivity<DispatchOrderRecordBean, ISearchPresenter>(),ISearchPresenter.StatusView  {

    private var status : Int ? = DispatchConstants.DISPATCH_STATUS_AGREEING;

    companion object {

        fun newInstance(context: Activity, status : Int, code : Int) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra("status", status)
            context.startActivityForResult(intent, code);
        }

        fun agreeing(context: Activity, code : Int) {
            newInstance(context, DispatchConstants.DISPATCH_STATUS_AGREEING, code);
        }

        fun taking(context: Activity, code : Int) {
            newInstance(context, DispatchConstants.DISPATCH_STATUS_TAKING, code);
        }

        fun delivering(context: Activity, code : Int) {
            newInstance(context, DispatchConstants.DISPATCH_STATUS_DELIVERING, code);
        }

    }

    override fun initBundles() {
        status = intent?.getIntExtra("status", DispatchConstants.DISPATCH_STATUS_AGREEING);
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_search;
    }

    override fun createPresenter(): ISearchPresenter {
        return SearchPreImpl(this, this);
    }

    override fun autoRefresh(): Boolean {
        return false;
    }

    override fun useBaseLayout(): Boolean {
        return false;
    }

    override fun initOthers() {
        StatusBarUtil.setColor(context, ContextCompat.getColor(context, R.color.white));
        super.initOthers()

        top_back.setOnClickListener {
            onBackPressed()
        }
        searchStatusTv.setOnClickListener {
            mPresenter?.clickMenuView(it);
        }
        searchTv.setOnClickListener {
            if(KeyboardUtils.isSoftInputVisible(context)) {
                KeyboardUtils.hideSoftInput(context);
            }
            mLoadMoreDelegate?.refresh()
        }
//        searchRemarksEt.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(s: Editable?) {
//                mLoadMoreDelegate?.refresh()
//            }
//
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })
        when(status) {
            DispatchConstants.DISPATCH_STATUS_AGREEING -> {
                onAgreeingStatus()
            }
            DispatchConstants.DISPATCH_STATUS_TAKING ->{
                onTakingStatus()
            }
            DispatchConstants.DISPATCH_STATUS_DELIVERING -> {
                onDeliveryStatus()
            }
        }
    }

    override fun initAdapter(): BaseQuickAdapter<DispatchOrderRecordBean, BaseViewHolder> {
        return SearchAdapter().apply {
            setStatus(status!!)
            setOnItemChildClickListener { adapter, view, position ->
                var item = getItem(position);
                when(view.id) {
                    R.id.iv_dispatch_order_num_copy -> {
                        val cm: ClipboardManager = MyApp.getInstance().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.setPrimaryClip(
                            ClipData.newPlainText(
                                MyApp.getInstance().getPackageName(),
                                item?.dispatchNo
                            )
                        )
                        showToast("复制成功")
                    }
                    R.id.tv_batch_user_phone -> {
                        item?.getFirstOrder()?.apply {
                            val dialog = HomeConfirmDialog(
                                context,
                                HomeConfirmDialog.DialogHomeConfirmClick { value: Boolean ->
                                    if (value) {
                                        PublicUtil.callPhone(consignerPhone, context);
                                    }
                                }, "确定提示", "确认需要拨打？", "取消", "确认拨打"
                            )
                            dialog.show()
                        }
                    }
                    R.id.tv_batch_option_agree_no -> {
                        // 拒绝接单
                        startActivity(
                            Intent(context, RejectionActivity::class.java).putExtra(
                                "id", item?.id
                            )
                        )
                    }
                    R.id.tv_batch_option_agree_yes -> {
                        // 确认接单
                        val dialog = HomeConfirmDialog(
                            context,
                            HomeConfirmDialog.DialogHomeConfirmClick { value: Boolean ->
                                if (value) {
                                    showDialogLoading()
                                    mPresenter?.itemOptionClick(item!!);
                                }
                            }, "接单提示", "请确认是否接单配送？", "取消", "确认接单"
                        )
                        dialog.show()
                    }
                    R.id.tv_batch_option_arrive_shop_no -> {
                        // 上报异常
                        startActivity(
                            Intent(context, ReportExceptionActivity::class.java).putExtra("id", item?.id)
                        )
                    }
                    R.id.tv_batch_option_arrive_shop_yes -> {
                        // 到达取货点
                        val dialog = HomeConfirmDialog(
                            context,
                            HomeConfirmDialog.DialogHomeConfirmClick { value: Boolean ->
                                if (value) {
                                    showDialogLoading()
                                    mPresenter?.itemOptionClick(item!!);
                                }
                            }, "到达提示", "请确认是否已到达取货点？", "取消", "确认到达"
                        )
                        dialog.show()
                    }
                    R.id.tv_batch_option_wait_pickup_no -> {
                        // 取货失败
                        OperatePickFailActivity.dispatch(context!!, item?.id!!,
                            DispatchListFragment.CODE_PICK_FAIL
                        )
                    }
                    R.id.tv_batch_option_wait_pickup_yes -> {
                        // 取货成功
                        val dialog = HomeConfirmDialog(
                            context,
                            HomeConfirmDialog.DialogHomeConfirmClick { value: Boolean ->
                                if (value) {
                                    showDialogLoading()
                                    mPresenter?.itemOptionClick(item!!);
                                }
                            }, "取货提示", "请确认是否已取货成功？", "取消", "确认成功"
                        )
                        dialog.show()
                    }
                    R.id.tv_batch_option_arrive_station_no -> {
                        // 上报异常
                        startActivity(
                            Intent(
                                context,
                                ReportExceptionActivity::class.java
                            ).putExtra("id", item?.id)
                        )
                    }
                    R.id.tv_batch_option_arrive_station_yes -> {
                        val ids = getOrderIds(item);
                        if(ids == null || ids.size == 0) {
                            return@setOnItemChildClickListener
                        }
                        // 送达收货点
                        val dialog = HomeConfirmDialog(
                            context,
                            HomeConfirmDialog.DialogHomeConfirmClick { value: Boolean ->
                                if (value) {
                                    showDialogLoading()
                                    mPresenter?.batchOptionClick(status, ids);
                                }
                            }, "送达提示", "请确认是否已送达收货点？", "取消", "确认送达"
                        )
                        dialog.show()
                    }
                    R.id.tv_batch_option_wait_sign_no -> {
                        // 签收失败
                        val ids = getOrderIds(item);
                        if(ids == null || ids.size == 0) {
                            return@setOnItemChildClickListener
                        }
                        OperateSignFailActivity.batch(context!!, ids,
                            DispatchListFragment.CODE_SIGN_FAIL
                        )
                    }
                    R.id.tv_batch_option_wait_sign_yes -> {
                        // 签收成功
                        val ids = getOrderIds(item);
                        if(ids == null || ids.size == 0) {
                            return@setOnItemChildClickListener
                        }
                        OperateSignActivity.batch(context!!, item?.getFirstOrder()?.isSelfTake?:0, ids, DispatchListFragment.CODE_SIGN);
                    }
                    R.id.tv_batch_option_signed_no -> {
                        // 投诉客户
                        startActivity(
                            Intent(
                                context,
                                ComplaintActivity::class.java
                            ).putExtra("id", item?.id)
                        )
                    }
                    R.id.tv_batch_option_signed_yes -> {
                        // 评价客户
                        startActivity(
                            Intent(
                                context,
                                EvaluateCustomerActivity::class.java
                            ).putExtra("id", item?.id)
                        )
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->
                var item = getItem(position);
                item?.apply {
                    if(DispatchConstants.isDeliveringTab(status!!)) {
                        if(item?.pickOrderFlag?.isNotBlank() == true) {
                            DispatchDetailActivity.delivery(context!!, item?.pickOrderFlag!!, status!!, item?.req,
                                DispatchListFragment.CODE_DETAIL
                            );
                        }
                    } else {
                        if(item?.id?.isNotBlank() == true) {
                            DispatchDetailActivity.forResult(context!!, item?.id!!, status!!,
                                DispatchListFragment.CODE_DETAIL
                            );
                        }
                    }
                }
            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    fun getOrderIds(item : DispatchOrderRecordBean?) : ArrayList<String> {
        val ids = arrayListOf<String>()
        item?.orderList?.forEachIndexed { index, item ->
            if(item?.id?.isNotBlank() == true) {
                ids.add(item?.id!!);
            }
        }
        return ids;
    }

    override fun onItemOptionSuccess() {
        mLoadMoreDelegate?.refresh()
        EventBus.getDefault().post(PublicBean(12))
    }

    override fun onAgreeingStatus() {
        searchStatusTv?.text = "待接单"
        status = DispatchConstants.DISPATCH_STATUS_AGREEING;
        (mAdapter as SearchAdapter)?.setStatus(status!!)
        if(searchRemarksEt?.text?.isNotBlank() == true) {
            mLoadMoreDelegate?.refresh()
        }
    }

    override fun onTakingStatus() {
        searchStatusTv?.text = "待取货"
        status = DispatchConstants.DISPATCH_STATUS_TAKING
        (mAdapter as SearchAdapter)?.setStatus(status!!)
        if(searchRemarksEt?.text?.isNotBlank() == true) {
            mLoadMoreDelegate?.refresh()
        }
    }

    override fun onDeliveryStatus() {
        searchStatusTv?.text = "待送达"
        status = DispatchConstants.DISPATCH_STATUS_DELIVERING
        (mAdapter as SearchAdapter)?.setStatus(status!!)
        if(searchRemarksEt?.text?.isNotBlank() == true) {
            mLoadMoreDelegate?.refresh()
        }
    }

    override fun onLoadMoreSuccess(list: List<DispatchOrderRecordBean>?, hasMore: Boolean) {
        if (mLoadMoreDelegate?.getPage()?.isRefreshing() == true) {
            mAdapter.replaceData(list ?: arrayListOf())
        } else {
            mAdapter.addData(list ?: arrayListOf())
        }
        mLoadMoreDelegate?.refresh();
        mLoadMoreDelegate?.loadFinish(hasMore, !list.isNullOrEmpty())
//        EventBus.getDefault().post(DispatchSingleNumberEvent(status!!, list?.size?:0));
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(status!!, page, searchRemarksEt.text.toString(), mAdapter.data, Constant.Home_Model_Event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshDispatchStatusEvent) {
        if (event.isRefresh(status)) {
            mLoadMoreDelegate?.refresh()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if((requestCode == DispatchListFragment.CODE_SIGN
                    || requestCode == DispatchListFragment.CODE_DETAIL
                    || requestCode == DispatchListFragment.CODE_PICK_FAIL
                    || requestCode == DispatchListFragment.CODE_SIGN_FAIL
                    || requestCode == DispatchListFragment.CODE_SIGNED) && resultCode == Activity.RESULT_OK) {
            mLoadMoreDelegate?.refresh()
        }
    }

}