package com.lingmiao.distribution.ui.main.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.blankj.utilcode.util.AppUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.bean.PublicBean
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.main.adapter.DispatchAdapter
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.event.DispatchNewOrderEvent
import com.lingmiao.distribution.ui.main.event.DispatchSingleNumberEvent
import com.lingmiao.distribution.ui.main.event.RefreshDispatchStatusEvent
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.distribution.app.MyApp
import com.lingmiao.distribution.dialog.HomeConfirmDialog
import com.lingmiao.distribution.ui.activity.RejectionActivity
import com.lingmiao.distribution.ui.main.adapter.OrderVieAdapter
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean
import com.lingmiao.distribution.ui.main.pop.TakeOrderDialog
import com.lingmiao.distribution.ui.main.presenter.IOrderViePresenter
import com.lingmiao.distribution.ui.main.presenter.impl.*
import com.lingmiao.distribution.util.PublicUtil
import com.lingmiao.distribution.util.map.MapNav
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
Create Date : 2020/12/2710:40 AM
Auther      : Fox
Desc        :
 **/
class OrderListFragment : BaseLoadMoreFragment<DispatchOrderItemBean, IOrderViePresenter>(), IOrderViePresenter.View {

    private var mDispatchStatus : Int? = null;

    private var mHomeModelEvent : HomeModelEvent?= null;

    companion object {
        const val CODE_SIGN = 101;
        const val CODE_DETAIL = 102;
        const val CODE_PICK_FAIL = 1011
        const val CODE_SIGN_FAIL = 104;
        const val CODE_SIGNED = 106
        const val KEY_DISPATCH_STATUS = "KEY_DISPATCH_STATUS"
        const val KEY_DISPATCH_EVENT = "KEY_DISPATCH_EVENT"

        fun newInstance(status : Int, event : HomeModelEvent) : OrderListFragment {
            return OrderListFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_DISPATCH_STATUS, status);
                    putSerializable(KEY_DISPATCH_EVENT, event);
                }
            }
        }

        fun vie(event : HomeModelEvent) : OrderListFragment {
            return newInstance(DispatchConstants.DISPATCH_STATUS_VIE, event);
        }

    }

    override fun initBundles() {
        mDispatchStatus = arguments?.getInt(KEY_DISPATCH_STATUS);
        mHomeModelEvent = arguments?.getSerializable(KEY_DISPATCH_EVENT) as HomeModelEvent;
    }

    override fun getLayoutId(): Int? {
        return R.layout.main_fragment_dispatch_list;
    }

    override fun useEventBus(): Boolean {
        return true;
    }

    override fun initOthers(rootView: View) {

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

    override fun initAdapter(): BaseQuickAdapter<DispatchOrderItemBean, BaseViewHolder> {
        return OrderVieAdapter().apply {
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
                    R.id.tv_batch_option_agree_no -> {
                        // 拒绝接单
                        startActivity(
                            Intent(activity, RejectionActivity::class.java).putExtra(
                                "id", item?.id
                            )
                        )
                    }
                    R.id.tv_batch_option_agree_yes -> {
                        // 确认抢单
                        if (mTakeOrderDialog?.isShowing == true) {
                            mTakeOrderDialog?.dismiss()
                        }
                        mTakeOrderDialog = TakeOrderDialog(
                            context,
                            takeOrderClick,
                            item!!
                        )
                        mTakeOrderDialog?.show()
                    }
                    R.id.tv_dispatch_start_distance -> {
                        // 导航
                        MapNav.chooseMapDialog(requireContext(), item?.getConsignerAddressStr(), item?.consignerLat ?: 0.0, item?.consignerLng ?: 0.0)
                    }
                    R.id.tv_dispatch_end_distance -> {
                        // 导航
                        MapNav.chooseMapDialog(requireContext(), item?.getConsigneeAddressStr(), item?.consigneeLat ?: 0.0, item?.consigneeLng ?: 0.0)
                    }
                }
            }
        }
    }

    private var mTakeOrderDialog : TakeOrderDialog?= null

    private val takeOrderClick: TakeOrderDialog.DialogPushConfirmClick = object : TakeOrderDialog.DialogPushConfirmClick {
        override fun sure(id: kotlin.String) {
            mTakeOrderDialog?.dismiss()
            agreeAndAccept(id)
        }

        override fun refuse(id: kotlin.String) {
            mTakeOrderDialog?.dismiss()
            startActivity(Intent(context, RejectionActivity::class.java).putExtra("id", id))
        }
    }

    fun agreeAndAccept(id : String) {
        mPresenter?.takeOrder(id, {
            showToast("抢单成功!")
            mLoadMoreDelegate?.refresh()
            EventBus.getDefault().post(PublicBean(12))
        }, {
        });
    }

    override fun createPresenter(): IOrderViePresenter {
        return OrderVieListPreImpl(this);
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(page, mAdapter.data, Constant.Home_Model_Event);
    }

    override fun onItemOptionSuccess() {
        mLoadMoreDelegate?.refresh()
        EventBus.getDefault().post(PublicBean(12))
    }

    override fun onLoadMoreSuccess(list: List<DispatchOrderItemBean>?, hasMore: Boolean) {
        if (mLoadMoreDelegate?.getPage()?.isRefreshing() == true) {
            mAdapter.replaceData(list ?: arrayListOf())
        } else {
            mAdapter.addData(list ?: arrayListOf())
        }
        mLoadMoreDelegate?.refresh();
        mLoadMoreDelegate?.loadFinish(hasMore, !list.isNullOrEmpty())
        //
        EventBus.getDefault().post(DispatchSingleNumberEvent(-1, 0));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onModelEvent(tempEvent: HomeModelEvent?) {
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

    private var newOrderId : String ?= "";

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNewOrder(event: DispatchNewOrderEvent?) {
        if(event?.id?.isNotBlank() == true) {
            newOrderId = event.id;
            (mAdapter as DispatchAdapter).setOrderId(newOrderId!!);
            mLoadMoreDelegate?.refresh()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if((requestCode == CODE_SIGN
                    || requestCode == CODE_DETAIL
                    || requestCode == CODE_PICK_FAIL
                    || requestCode == CODE_SIGN_FAIL
                    || requestCode == CODE_SIGNED) && resultCode == Activity.RESULT_OK) {
            mLoadMoreDelegate?.refresh()
        }
    }

}