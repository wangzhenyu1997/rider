package com.lingmiao.distribution.ui.main

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.lingmiao.distribution.R
import com.lingmiao.distribution.app.MyApp
import com.lingmiao.distribution.bean.PublicBean
import com.lingmiao.distribution.dialog.HomeConfirmDialog
import com.lingmiao.distribution.ui.activity.*
import com.lingmiao.distribution.ui.main.adapter.OrderAdapter
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.DispatchListReq
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.event.RefreshDispatchStatusEvent
import com.lingmiao.distribution.ui.main.presenter.IDispatchDetailPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.DispatchDetailPreImpl
import com.lingmiao.distribution.widget.EmptyView
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import kotlinx.android.synthetic.main.main_activity_dispatch_detail.*
import kotlinx.android.synthetic.main.main_activity_order_detail.*
import kotlinx.android.synthetic.main.main_view_dispatch_option_detail.*
import kotlinx.android.synthetic.main.main_view_dispatch_option_multiple.*
import kotlinx.android.synthetic.main.main_view_dispatch_option_multiple.ll_dispatch_arrive_shop
import kotlinx.android.synthetic.main.main_view_dispatch_option_multiple.ll_dispatch_arrive_station
import kotlinx.android.synthetic.main.main_view_dispatch_option_multiple.ll_dispatch_wait_pickup
import kotlinx.android.synthetic.main.main_view_dispatch_option_multiple.ll_dispatch_wait_sign
import kotlinx.android.synthetic.main.main_view_dispatch_option_multiple.tv_batch_option_arrive_shop_no
import kotlinx.android.synthetic.main.main_view_dispatch_option_multiple.tv_batch_option_arrive_station_no
import org.greenrobot.eventbus.EventBus

/**
Create Date : 2020/12/297:23 PM
Auther      : Fox
Desc        :
 **/
class DispatchDetailActivity : BaseActivity<IDispatchDetailPresenter>(), IDispatchDetailPresenter.View {

    private var id : String? = "";

    private var type : Int? = -1;

    private var mAdapter : OrderAdapter?= null;

    private var mItem : DispatchOrderRecordBean ? = null;

    private var mReq : DispatchListReq ? = null;

    companion object {

        const val CODE_SIGN = 101;
        const val CODE_SIGN_FAIL = 102;
        const val CODE_PICK_FAIL = 200;
        const val CODE_DETAIL = 1011;

        fun history(context: Context, id : String, resultValue : Int) {
            forResult(context, id, DispatchConstants.DISPATCH_LIST_HISTORY, resultValue);
        }

        fun delivery(context: Context, id : String, type : Int, req : DispatchListReq?, resultValue : Int) {
            if(context is Activity) {
                val intent = Intent(context, DispatchDetailActivity::class.java)
                intent.putExtra("dispatchId", id)
                intent.putExtra("type", type)
                intent.putExtra("req", req);
                context.startActivityForResult(intent, resultValue)
            }
        }

        fun forResult(context: Context, id : String, type : Int, resultValue : Int) {
            if(context is Activity) {
                val intent = Intent(context, DispatchDetailActivity::class.java)
                intent.putExtra("dispatchId", id)
                intent.putExtra("type", type)
                context.startActivityForResult(intent, resultValue)
            }
        }

        fun open(context: Context, id : String, type : Int) {
            if(context is Activity) {
                val intent = Intent(context, DispatchDetailActivity::class.java)
                intent.putExtra("dispatchId", id)
                intent.putExtra("type", type)
                context.startActivity(intent);
            }
        }
    }

    override fun initBundles() {
        id = intent?.getStringExtra("dispatchId");
        type = intent?.getIntExtra("type", -1);
        if(intent?.hasExtra("req") == true) {
            mReq = intent?.getSerializableExtra("req") as DispatchListReq;
        }
    }

    override fun useBaseLayout(): Boolean {
        return false;
    }

    override fun createPresenter(): IDispatchDetailPresenter {
        return DispatchDetailPreImpl(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_dispatch_detail;
    }

    override fun initView() {
        val drawable = getDrawable(R.mipmap.scan_code)
        drawable?.setBounds(0, 0, 48, 48);


        toolbarView?.apply {
            setTitleContent("列表详情");
            setRightListener(drawable, {
                startActivity(Intent(context, OrderScanActivity::class.java).putExtra("type", type));
            })
        };
        initAdapter();
        initRecycleView();
        setBottomOptionClickListener();
        loadData();
    }

    fun initRecycleView() {
        rvLoadMore.apply {
            layoutManager = LinearLayoutManager(context);
            adapter = mAdapter;
        }
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnRefreshListener {
            loadData();
            smartRefreshLayout?.finishRefresh();
        }
    }

    fun initAdapter() {
        mAdapter = OrderAdapter(type!!).apply {
            setOnItemChildClickListener { adapter, view, position ->
                var item = getItem(position);
                when(view.id) {
                    R.id.iv_dispatch_order_num_copy -> {
                        val cm: ClipboardManager = MyApp.getInstance().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.setPrimaryClip(ClipData.newPlainText(MyApp.getInstance().getPackageName(), item?.dispatchNo))
                        showToast("复制成功")
                    }
                    R.id.tv_batch_option_re_arrive_shop_no,
                    R.id.tv_batch_option_arrive_shop_no,
                    R.id.tv_batch_option_re_arrive_station_no,
                    R.id.tv_batch_option_arrive_station_no -> {
                        // 上报异常
                        orderException();
                    }
                    R.id.tv_batch_option_re_wait_pickup_no -> {
                        // 取货失败
                        orderPickupFailed(item?.id, DispatchConstants.TYPE_ORDER);
                    }
                    R.id.tv_batch_option_re_wait_sign_no -> {
                        // 签收失败
                        orderSignFailed(item?.id, DispatchConstants.TYPE_ORDER);
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->
                var item = getItem(position);
                if(item != null && item?.id != null) {
                    OrderDetailActivity.open(context, item?.id!!, type!!, CODE_DETAIL);
                }
            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    fun getOrderIds() : ArrayList<String> {
        val ids = arrayListOf<String>()
        mAdapter?.data?.forEachIndexed { index, item ->
            if(item?.id?.isNotBlank() == true) {
                ids.add(item?.id!!);
            }
        }
        return ids;
    }

    fun setBottomOptionClickListener() {
        // 拒绝
        tv_batch_option_agree_no.setOnClickListener {
            orderReject(id);
        };
        // 接单
        tv_batch_option_agree_yes.setOnClickListener {
            orderConfirm(id);
        }
        // 上报异常
        tv_batch_option_arrive_shop_no.setOnClickListener {
            orderException();
        }
        // 到达取货点
        tv_batch_option_arrive_shop_yes.setOnClickListener {
            orderArriveShop(id, DispatchConstants.TYPE_DISPATCH);
        }
        // 取货失败
        tv_batch_option_wait_pickup_no.setOnClickListener {
            orderPickupFailed(id, DispatchConstants.TYPE_DISPATCH);
        }
        // 取货成功
        tv_batch_option_wait_pickup_yes.setOnClickListener {
            orderPickupSuccess(id, DispatchConstants.TYPE_DISPATCH);
        }
        // 上报异常
        tv_batch_option_arrive_station_no.setOnClickListener {
            orderException();
        }
        // 送达收货点
        tv_batch_option_arrive_station_yes.setOnClickListener {
            val ids = getOrderIds();
            if(ids == null || ids.size == 0) {
                return@setOnClickListener
            }
            orderArriveStation(ids, DispatchConstants.TYPE_DISPATCH);
        }
        // 签收失败
        tv_batch_option_wait_sign_no.setOnClickListener {
            val ids = getOrderIds();
            if(ids == null || ids.size == 0) {
                return@setOnClickListener
            }
            OperateSignFailActivity.batch(context, ids, CODE_SIGN_FAIL)
        }
        // 签收成功
        tv_batch_option_wait_sign_yes.setOnClickListener {
            val ids = getOrderIds();
            if(ids == null || ids.size == 0) {
                return@setOnClickListener
            }
            OperateSignActivity.batch(context, mAdapter?.data?.get(0)?.isSelfTake ?: 0, ids, CODE_SIGN);
        }
        // 投诉客户
        tv_batch_option_signed_no.setOnClickListener {
            orderComplaint(id);
        }
        // 评价客户
        tv_batch_option_signed_yes.setOnClickListener {
            orderComment(id);
        }
    }

    fun orderReject(id : String?) {
        startActivity(
            Intent(context, RejectionActivity::class.java).putExtra(
                "id", id
            )
        )
    }

    private fun orderConfirm(id: String?) {
        val dialog = HomeConfirmDialog(
            context,
            HomeConfirmDialog.DialogHomeConfirmClick { value: Boolean ->
                if (value) {
                    showDialogLoading()
                    mPresenter?.sureOrder(id!!, {
                        optionSuccess()
                    }, {
                        optionFailed()
                    })
                }
            }, "接单提示", "请确认是否接单配送？", "取消", "确认接单"
        )
        dialog.show()
    }

    private fun orderArriveShop(id: String?, type : Int) {
        val dialog = HomeConfirmDialog(
            context,
            HomeConfirmDialog.DialogHomeConfirmClick { value: Boolean ->
                if (value) {
                    showDialogLoading()
                    mPresenter?.arriveShop(id!!, type, {
                        optionSuccess();
                    }, {
                        optionFailed()
                    });
                }
            }, "到达提示", "请确认是否已到达取货点？", "取消", "确认到达"
        )
        dialog.show()
    }

    private fun orderException() {
        if(mItem?.id?.isNotBlank() == true) {
            startActivity(
                Intent(context, ReportExceptionActivity::class.java).putExtra("id", mItem?.id)
            )
        }
    }

    private fun orderPickupSuccess(id: String?, type : Int) {
        val dialog = HomeConfirmDialog(
            context,
            HomeConfirmDialog.DialogHomeConfirmClick { value: Boolean ->
                if (value) {
                    showDialogLoading()
                    mPresenter?.pickup(id!!, type, {
                        optionSuccess()
                    },{
                        optionFailed()
                    });
                }
            }, "取货提示", "请确认是否已取货成功？", "取消", "确认成功"
        )
        dialog.show()
    }

    private fun orderPickupFailed(id: String?, type : Int) {
        id?.apply {
            OperatePickFailActivity.open(context, id!!, type, CODE_PICK_FAIL);
        }
    }

    private fun orderArriveStation(ids: ArrayList<String>?, type : Int) {
        val dialog = HomeConfirmDialog(
            context,
            HomeConfirmDialog.DialogHomeConfirmClick { value: Boolean ->
                if (value) {
                    showDialogLoading()
                    mPresenter?.batchArriveStation(ids!!, {
                        optionSuccess()
                    }, {
                        optionFailed()
                    });
                }
            }, "送达提示", "请确认是否已送达收货点？", "取消", "确认送达"
        )
        dialog.show()
    }

    private fun orderSignFailed(id: String?, type : Int) {
        OperateSignFailActivity.open(context, id!!, type, CODE_SIGN_FAIL)
    }

    private fun orderComment(id: String?) {
        startActivity(
            Intent(
                context,
                EvaluateCustomerActivity::class.java
            ).putExtra("id", id)
        )
    }

    private fun orderComplaint(id: String?) {
        startActivity(
            Intent(
                context,
                ComplaintActivity::class.java
            ).putExtra("id", id)
        )
    }

    fun loadData() {
        if(id?.isNotBlank() == true) {
            mPresenter?.loadDetail(id!!, mReq, type!!);
        }
    }

    override fun loadDetailSuccess(bean: DispatchOrderRecordBean) {
        this.mItem = bean;
        this.mItem?.apply {
            resetBottomOption(dispatchStatus!!);
            mAdapter?.setItem(this);
            mAdapter?.replaceData(orderList ?: arrayListOf());
        }
    }

    override fun loadDetailFail() {
        // 拆分的调度单刷不出来
        showToast("调度单已操作完成")
        finish();
    }

    override fun optionSuccess() {
        hideDialogLoading()
        loadData();
        setResult(RESULT_OK);
    }

    override fun optionFailed() {
        hideDialogLoading()
        loadData();
        setResult(RESULT_OK);
    }

    fun resetBottomOption(dispatchStatus : Int) {
        ll_dispatch_wait_agree.gone();
        ll_dispatch_arrive_shop.gone();
        ll_dispatch_wait_pickup.gone();
        ll_dispatch_arrive_station.gone();
        ll_dispatch_wait_sign.gone();
        ll_dispatch_signed.gone();

        when(dispatchStatus) {
            DispatchOrderRecordBean.CANCEL,
            DispatchOrderRecordBean.CREATE,
            DispatchOrderRecordBean.REFUSE -> {
            }
            // 确认
            DispatchOrderRecordBean.WAIT_ARRANGED -> {
                if(DispatchConstants.isAgreeingTab(type!!)) {
                    ll_dispatch_wait_agree.visiable();
                }
            }
            // 待到店
            DispatchOrderRecordBean.WAIT_ARRIVE_SHOP -> {
                if(DispatchConstants.isTakingTab(type!!)) {
                    ll_dispatch_arrive_shop.visiable();
                }
            }
            // 待取货
            DispatchOrderRecordBean.WAIT_PICKUP -> {
                if(DispatchConstants.isTakingTab(type!!)) {
                    ll_dispatch_wait_pickup.visiable();
                }
            }
            // 待送达
            DispatchOrderRecordBean.WAIT_ARRIVE_STATION -> {
                if(DispatchConstants.isDeliveringTab(type!!)) {
                    if(DispatchOrderItemBean.isWaitSign(mItem?.getFirstOrder()?.orderStatus)) {
                        ll_dispatch_wait_sign.visiable();
                    } else {
                        ll_dispatch_arrive_station.visiable();
                    }
                }
            }
            // 待签收
            DispatchOrderRecordBean.WAIT_SIGN -> {
                if(DispatchConstants.isDeliveringTab(type!!)) {
                    ll_dispatch_wait_sign.visiable();
                }
            }
            DispatchOrderRecordBean.SIGNED -> {
                ll_dispatch_signed.visiable();
            }
            DispatchOrderRecordBean.SIGN_FAIL,
            DispatchOrderRecordBean.PICKUP_FAIL -> {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if((requestCode == CODE_SIGN
                    || requestCode == CODE_SIGN_FAIL
                    || requestCode == CODE_PICK_FAIL)
            && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            EventBus.getDefault().post(PublicBean(10))
            loadData();
        }
    }
}