package com.lingmiao.distribution.ui.main.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.app.MyApp
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.bean.PublicBean
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.dialog.HomeConfirmDialog
import com.lingmiao.distribution.dialog.HomeConfirmDialog.DialogHomeConfirmClick
import com.lingmiao.distribution.ui.activity.*
import com.lingmiao.distribution.ui.main.DispatchDetailActivity
import com.lingmiao.distribution.ui.main.OperatePickFailActivity
import com.lingmiao.distribution.ui.main.OperateSignActivity
import com.lingmiao.distribution.ui.main.OperateSignFailActivity
import com.lingmiao.distribution.ui.main.adapter.DispatchAdapter
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.event.DispatchNewOrderEvent
import com.lingmiao.distribution.ui.main.event.DispatchSingleNumberEvent
import com.lingmiao.distribution.ui.main.event.RefreshDispatchStatusEvent
import com.lingmiao.distribution.ui.main.presenter.IDispatchListPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.DispatchAgreeingListPreImpl
import com.lingmiao.distribution.ui.main.presenter.impl.DispatchDeliveringListPreImpl
import com.lingmiao.distribution.ui.main.presenter.impl.DispatchTakingListPreImpl
import com.lingmiao.distribution.util.PublicUtil
import com.lingmiao.distribution.widget.EmptyView
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.distribution.util.map.MapNav
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
Create Date : 2020/12/2710:40 AM
Auther      : Fox
Desc        :
 **/
class DispatchListFragment :
    BaseLoadMoreFragment<DispatchOrderRecordBean, IDispatchListPresenter>(),
    IDispatchListPresenter.View {

    private var mDispatchStatus: Int? = null;

    private var mHomeModelEvent: HomeModelEvent? = null;

    companion object {
        const val CODE_SIGN = 101;
        const val CODE_DETAIL = 102;
        const val CODE_PICK_FAIL = 1011
        const val CODE_SIGN_FAIL = 104;
        const val CODE_SIGNED = 106
        const val KEY_DISPATCH_STATUS = "KEY_DISPATCH_STATUS"
        const val KEY_DISPATCH_EVENT = "KEY_DISPATCH_EVENT"

        fun newInstance(status: Int, event: HomeModelEvent): DispatchListFragment {
            return DispatchListFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_DISPATCH_STATUS, status);
                    putSerializable(KEY_DISPATCH_EVENT, event);
                }
            }
        }

        fun agreeing(event: HomeModelEvent): DispatchListFragment {
            return newInstance(DispatchConstants.DISPATCH_STATUS_AGREEING, event);
        }

        fun taking(event: HomeModelEvent): DispatchListFragment {
            return newInstance(DispatchConstants.DISPATCH_STATUS_TAKING, event);
        }

        fun delivering(event: HomeModelEvent): DispatchListFragment {
            return newInstance(DispatchConstants.DISPATCH_STATUS_DELIVERING, event);
        }

    }

    override fun initBundles() {
        mDispatchStatus = arguments?.getInt(KEY_DISPATCH_STATUS);
        mHomeModelEvent = arguments?.getSerializable(KEY_DISPATCH_EVENT) as HomeModelEvent;
    }

    override fun getLayoutId() = R.layout.main_fragment_dispatch_list;


    override fun useEventBus()= true


    override fun initOthers(rootView: View) {

    }

    fun getOrderIds(item: DispatchOrderRecordBean?): ArrayList<String> {
        val ids = arrayListOf<String>()
        item?.orderList?.forEachIndexed { index, item ->
            if (item?.id?.isNotBlank() == true) {
                ids.add(item?.id!!);
            }
        }
        return ids;
    }

    override fun initAdapter(): BaseQuickAdapter<DispatchOrderRecordBean, BaseViewHolder> {
        return DispatchAdapter(mDispatchStatus!!).apply {
            setOnItemChildClickListener { adapter, view, position ->
                var item = getItem(position);
                when (view.id) {
                    R.id.iv_dispatch_order_num_copy -> {
                        val cm: ClipboardManager = MyApp.getInstance()
                            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
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
                                activity,
                                DialogHomeConfirmClick { value: Boolean ->
                                    if (value) {
                                        if (DispatchOrderRecordBean.isDeliveryStatus(
                                                item?.dispatchStatus ?: 0
                                            )
                                        ) {
                                            if (item?.getFirstOrder()?.isSelfTake == 1) {
                                                PublicUtil.callPhone(
                                                    consigneeCustomerMobile,
                                                    activity
                                                );
                                            } else {
                                                PublicUtil.callPhone(consignerPhone, activity);
                                            }
                                        } else {
                                            PublicUtil.callPhone(consignerPhone, activity);
                                        }
                                    }
                                }, "确定提示", "确认需要拨打？", "取消", "确认拨打"
                            )
                            dialog.show()
                        }
                    }
                    R.id.tv_batch_option_agree_no -> {
                        // 拒绝接单
                        startActivity(
                            Intent(activity, RejectionActivity::class.java).putExtra(
                                "id", item?.id
                            )
                        )
                    }
                    R.id.tv_batch_option_agree_yes -> {
                        // 确认接单
                        val dialog = HomeConfirmDialog(
                            activity,
                            DialogHomeConfirmClick { value: Boolean ->
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
                            Intent(activity, ReportExceptionActivity::class.java).putExtra(
                                "id",
                                item?.id
                            )
                        )
                    }
                    R.id.tv_batch_option_arrive_shop_yes -> {
                        // 到达取货点
                        val dialog = HomeConfirmDialog(
                            activity,
                            DialogHomeConfirmClick { value: Boolean ->
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
                        OperatePickFailActivity.dispatch(context!!, item?.id!!, CODE_PICK_FAIL)
                    }
                    R.id.tv_batch_option_wait_pickup_yes -> {
                        // 取货成功
                        val dialog = HomeConfirmDialog(
                            activity,
                            DialogHomeConfirmClick { value: Boolean ->
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
                                activity,
                                ReportExceptionActivity::class.java
                            ).putExtra("id", item?.id)
                        )
                    }
                    R.id.tv_batch_option_arrive_station_yes -> {
                        val ids = getOrderIds(item);
                        if (ids == null || ids.size == 0) {
                            return@setOnItemChildClickListener
                        }
                        // 送达收货点
                        val dialog = HomeConfirmDialog(
                            activity,
                            DialogHomeConfirmClick { value: Boolean ->
                                if (value) {
                                    showDialogLoading()
                                    mPresenter?.batchOptionClick(ids);
                                }
                            }, "送达提示", "请确认是否已送达收货点？", "取消", "确认送达"
                        )
                        dialog.show()
                    }
                    R.id.tv_batch_option_wait_sign_no -> {
                        // 签收失败
                        val ids = getOrderIds(item);
                        if (ids == null || ids.size == 0) {
                            return@setOnItemChildClickListener
                        }
                        OperateSignFailActivity.batch(context!!, ids, CODE_SIGN_FAIL)
                    }
                    R.id.tv_batch_option_wait_sign_yes -> {
                        // 签收成功
                        val ids = getOrderIds(item);
                        if (ids == null || ids.size == 0) {
                            return@setOnItemChildClickListener
                        }
                        OperateSignActivity.batch(
                            context!!,
                            item?.getFirstOrder()?.isSelfTake ?: 0,
                            ids,
                            CODE_SIGN
                        );
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
                                activity,
                                EvaluateCustomerActivity::class.java
                            ).putExtra("id", item?.id)
                        )
                    }
                    R.id.tv_dispatch_start_distance -> {
                        // 导航
                        MapNav.chooseMapDialog(
                            requireContext(),
                            item?.getConsignerAddress(),
                            item?.orderList?.get(0)?.consignerLat ?: 0.0,
                            item?.orderList?.get(0)?.consignerLng ?: 0.0
                        )
                    }
                    R.id.tv_dispatch_end_distance -> {
                        // 导航
                        MapNav.chooseMapDialog(
                            requireContext(),
                            item?.getConsignerAddress(),
                            item?.orderList?.get(0)?.consigneeLat ?: 0.0,
                            item?.orderList?.get(0)?.consigneeLng ?: 0.0
                        )
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->
                var item = getItem(position);
                item?.apply {
                    if (viewModelType == 0) {
                        if (DispatchConstants.isDeliveringTab(type)) {
                            if (item?.pickOrderFlag?.isNotBlank() == true) {
                                DispatchDetailActivity.delivery(
                                    context!!,
                                    item?.pickOrderFlag!!,
                                    type,
                                    item?.req,
                                    CODE_DETAIL
                                );
                            }
                        } else {
                            if (item?.id?.isNotBlank() == true) {
                                DispatchDetailActivity.forResult(
                                    context!!,
                                    item?.id!!,
                                    type,
                                    CODE_DETAIL
                                );
                            }
                        }
                    } else {
                        // 订单详情 && 前往取货 && 前往送货
                        if (item?.dispatchStatus == 3 || item?.dispatchStatus == 5) {
                            // 3.前往取货
                            // 5.前往送货
                            startActivity(
                                Intent(activity, MapActivity::class.java).putExtra(
                                    "data",
                                    item
                                )
                            )
                        } else {
                            //跳转订单详细
                            startActivity(
                                Intent(
                                    activity,
                                    HomeDetailActivity::class.java
                                ).putExtra("id", item?.id)
                                    .putExtra("index", mDispatchStatus!!)
                            )
                        }
                    }
                }
            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    override fun createPresenter(): IDispatchListPresenter? {
        return when (mDispatchStatus) {
            DispatchConstants.DISPATCH_STATUS_AGREEING -> DispatchAgreeingListPreImpl(this);
            DispatchConstants.DISPATCH_STATUS_TAKING -> DispatchTakingListPreImpl(this);
            DispatchConstants.DISPATCH_STATUS_DELIVERING -> DispatchDeliveringListPreImpl(this);
            else -> null;
        }
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(page, mAdapter.data, Constant.Home_Model_Event);
    }

    override fun onItemOptionSuccess() {
        mLoadMoreDelegate?.refresh()
        EventBus.getDefault().post(PublicBean(12))
    }

    override fun onLoadMoreSuccess(list: List<DispatchOrderRecordBean>?, hasMore: Boolean) {
        if (mLoadMoreDelegate?.getPage()?.isRefreshing() == true) {
            mAdapter.replaceData(list ?: arrayListOf())
        } else {
            mAdapter.addData(list ?: arrayListOf())
        }
        mLoadMoreDelegate?.refresh();
        mLoadMoreDelegate?.loadFinish(hasMore, !list.isNullOrEmpty())
        //
        EventBus.getDefault().post(DispatchSingleNumberEvent(-1, 0));
//        EventBus.getDefault().post(DispatchSingleNumberEvent(mDispatchStatus!!, list?.size?:0));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onModelEvent(tempEvent: HomeModelEvent) {
        if (tempEvent != null) {
            this.mHomeModelEvent = tempEvent;
            mLoadMoreDelegate?.refresh()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshDispatchStatusEvent) {
        if (event.isRefresh(mDispatchStatus)) {
            mLoadMoreDelegate?.refresh()
        }
    }

    private var newOrderId: String? = "";

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNewOrder(event: DispatchNewOrderEvent) {
        if (event?.id?.isNotBlank()) {
            newOrderId = event.id;
            (mAdapter as DispatchAdapter).setOrderId(newOrderId!!);
            mLoadMoreDelegate?.refresh()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == CODE_SIGN
                    || requestCode == CODE_DETAIL
                    || requestCode == CODE_PICK_FAIL
                    || requestCode == CODE_SIGN_FAIL
                    || requestCode == CODE_SIGNED) && resultCode == Activity.RESULT_OK
        ) {
            mLoadMoreDelegate?.refresh()
        }
    }

}