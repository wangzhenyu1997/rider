package com.lingmiao.distribution.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.LabelsBean
import com.lingmiao.distribution.dialog.HomeConfirmDialog
import com.lingmiao.distribution.ui.activity.ComplaintListActivity
import com.lingmiao.distribution.ui.activity.ReportExceptionActivity
import com.lingmiao.distribution.ui.activity.ReportExceptionListActivity
import com.lingmiao.distribution.ui.main.adapter.TraceAdapter
import com.lingmiao.distribution.ui.main.adapter.getLabelList
import com.lingmiao.distribution.ui.main.adapter.getTagView
import com.lingmiao.distribution.ui.main.bean.*
import com.lingmiao.distribution.ui.main.presenter.IOrderDetailPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.OrderDetailPreImpl
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import kotlinx.android.synthetic.main.main_activity_order_detail.*
import kotlinx.android.synthetic.main.main_view_dispatch_option_detail.*
import kotlinx.android.synthetic.main.main_view_order_base_info.*
import kotlinx.android.synthetic.main.main_view_order_complaint.*
import kotlinx.android.synthetic.main.main_view_order_delivery_info.*
import kotlinx.android.synthetic.main.main_view_order_exception.*
import kotlinx.android.synthetic.main.main_view_order_goods_info.*
import kotlinx.android.synthetic.main.main_view_order_time.*

/**
Create Date : 2021/1/611:50 AM
Auther      : Fox
Desc        :
 **/
class OrderDetailActivity : BaseActivity<IOrderDetailPresenter>(), IOrderDetailPresenter.View {

    private var id : String? = "";

    private var type : Int? = -1;

    private var mItem : OrderDetail?= null;

    private var mDispatch : DispatchOrderRecordBean ? = null

    private var mOrder : DispatchOrderItemBean?= null

    private var mTraceAdapter : TraceAdapter? = null;

    companion object {
        fun open(context: Context, id : String, type : Int, code : Int) {
            if(context is Activity) {
                val intent = Intent(context, OrderDetailActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("type", type)
                context.startActivityForResult(intent, code);
            }
        }
    }

    override fun initBundles() {
        id = intent?.getStringExtra("id");
        type = intent?.getIntExtra("type", -1);
    }

    override fun createPresenter(): IOrderDetailPresenter {
        return OrderDetailPreImpl(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_order_detail;
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle("订单详情");

        initButtonListener();

        getDetail();
    }

    fun getDetail() {
        id?.apply {
            mPresenter?.getDetail(id!!);
        }
    }

    override fun getDetailSuccess(item: OrderDetail?) {
        mItem = item;
        mDispatch = mItem?.dispatch;
        mOrder = mItem?.order;
        refreshUi();
    }

    override fun getDetailFailed() {
        finish();
    }

    fun refreshUi() {
        setBaseInfo();
        setGoodsInfo();
        setDeliveryInfo();
        setTimeInfo();
        setExceptionInfo();
        setTraceInfo();
        resetStatusButton();
    }

    fun setBaseInfo() {
        // 预约tag
        m_order_tag.visibility = if(mOrder?.isAppointment == 1) View.VISIBLE else View.GONE
        // 订单号
         m_number.setText(mOrder?.orderNo)
        // 订单状态
          m_status.setText(mOrder?.getOrderStatusStr())
        // 外部订单号
        if(mOrder?.upsBillNo?.isNotBlank() == true) {
            m_out_no.setText(mOrder?.upsBillNo)
            ll_m_out_no.visiable()
            l_m_out_no.visiable()
        } else {
            ll_m_out_no.gone()
            l_m_out_no.gone()
        }
        // 配送时间
        m_time.setText(mOrder?.planDeliveryTime)
        // 时效要求
        m_time_require.setText(mOrder?.showTimeRequire)
        // 配送费
        m_cost_view.gone()
        line_cost_view.gone()
        m_money.setText(String.format("%s", mOrder?.totalCost))
        // 下单用户
        m_person.setText(mOrder?.consigneeName)
    }

    fun confirmCall(phone : String?) {
        val dialog = HomeConfirmDialog(
            context,
            HomeConfirmDialog.DialogHomeConfirmClick { value: Boolean ->
                if (value) {
                    PublicUtil.callPhone(phone, context)
                }
            }, "确定提示", "确认需要拨打？", "取消", "确认拨打"
        )
        dialog.show()
    }

    fun setGoodsInfo() {
        // 发货人
        tv_dispatch_start_name.setText(PublicUtil.isNull(mOrder?.consignerCustomerName));
        tv_dispatch_start_phone.setText(PublicUtil.isNull(mOrder?.consignerPhone))
        tv_dispatch_start_phone.setOnClickListener {
            confirmCall(mOrder?.consignerPhone);
        }
        when(mOrder?.orderStatus) {
            DispatchOrderItemBean.WAIT_ARRIVE_STATION,
            DispatchOrderItemBean.WAIT_SIGN -> {
                iv_dispatch_start_view.setImageResource(R.mipmap.home_blue_circle)
                iv_dispatch_end_view.setImageResource(R.mipmap.home_red_circle_no)
            }
            DispatchOrderItemBean.SIGNED -> {
                iv_dispatch_start_view.setImageResource(R.mipmap.home_blue_circle)
                iv_dispatch_end_view.setImageResource(R.mipmap.home_red_circle)
            }
            else -> {
                iv_dispatch_start_view.setImageResource(R.mipmap.home_blue_circle_no)
                iv_dispatch_end_view.setImageResource(R.mipmap.home_red_circle_no)
            }
        }
        // 发货地址
        tv_dispatch_start_address.setText(mOrder?.getConsignerAddressStr());
        // 发货距离
        tv_dispatch_start_distance.setText(String.format("%s%s", PublicUtil.isNull(mOrder?.originDistance), if(mOrder?.originDistance?.length?:0 > 0) "公里" else ""));
        // 收货人
        tv_dispatch_end_name.setText(PublicUtil.isNull(mOrder?.consigneeName))
        tv_dispatch_end_phone.setText(PublicUtil.isNull(mOrder?.consigneePhone))
        tv_dispatch_end_phone.setOnClickListener {
            confirmCall(mOrder?.consigneePhone)
        }
        // 收货地址
        tv_dispatch_end_address.setText(mOrder?.getConsigneeAddressStr());
        // 收货距离
        tv_dispatch_end_distance.setText(String.format("%s%s", PublicUtil.isNull(mOrder?.targetDistance), if(mOrder?.targetDistance?.length?:0 > 0) "公里" else ""));
        // 标签
        ll_dispatch_tag.removeAllViews();
        mOrder?.apply {
            val mValue: List<LabelsBean> = getLabelList(labels!!);
            for (param in mValue) {
                ll_dispatch_tag.addView(getTagView(context, param));
            }
        }
        // 外部订单
        tv_dispatch_dt_out_order_id.gone();
        // 配送信息不显示
        layout_time.gone()
        // 配送时间
        tv_dispatch_dt_time.setText(mOrder?.getPlanDeliveryTimeStr())
        // 商品信息
        tv_dispatch_good.setText(mOrder?.goodsName);
        // 备注
        if(mOrder?.customerRemark?.isNotBlank() == true) {
            tv_dispatch_remark.setText(mOrder?.customerRemark)
            rl_dispatch_remark_view.visiable()
        } else {
            rl_dispatch_remark_view.gone();
        }
    }

    fun setDeliveryInfo() {
        // 配送员
        m_dispatcher.setText(mDispatch?.riderName)
        // 手机号
        m_order_phone.setText(mDispatch?.riderMobile)
        // 车牌号
        m_plate_num.setText(mDispatch?.getVehiclePlateStr())
        // 车型
        m_model.setText(mDispatch?.getVehicleTypeStr())
        // 调度单号
        m_dispatch_no.setText(mDispatch?.dispatchNo)
    }

    fun setTimeInfo() {
        // 下单时间
        m_down_time.setText(String.format("下单时间：%s", PublicUtil.isNull(mDispatch?.createTime)));
        // 接单时间：
        m_receiving_time.setText(String.format("接单时间：%s", PublicUtil.isNull(mDispatch?.acceptTime)))
        // 完成时间：
        m_complete_time.setText(String.format("完成时间：%s", PublicUtil.isNull(mDispatch?.signedTime)))
    }

    fun setExceptionInfo() {
        // 异常信息
        m_ab_num.setText(String.format("%s", mOrder?.dispatchExceptionNum));
        m_abnormal_message.setOnClickListener {
            startActivity(
                Intent(context, ReportExceptionListActivity::class.java).putExtra("id", mDispatch?.id)
            )
        }
        // 投诉信息
        m_cp_num.setText(String.format("%s", mOrder?.complaintNum))
        m_complaint_message.setOnClickListener {
            startActivity(
                Intent(context, ComplaintListActivity::class.java).putExtra("id", mDispatch?.id)
            )
        }
    }

    fun setTraceInfo() {
        mTraceAdapter = TraceAdapter(this).apply {

        }
        rvTrace?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mTraceAdapter;
        }
        mTraceAdapter?.replaceData(mItem?.orderTrackList?: arrayListOf<OrderTrack>());
    }

    fun exceptionUpload() {
        if(mOrder?.id?.isNotBlank() == true) {
            startActivity(
                Intent(context, ReportExceptionActivity::class.java)
                    .putExtra("id", mDispatch?.id!!)
                    .putExtra("type", DispatchConstants.TYPE_ORDER)
            )
        }
    }

    fun initButtonListener() {
        // 上报异常
        tv_batch_option_arrive_shop_no.setOnClickListener {
            exceptionUpload();
        }
        tv_batch_option_re_arrive_shop_no.setOnClickListener {
            exceptionUpload();
        }

        // 取货失败
        tv_batch_option_re_wait_pickup_no.setOnClickListener {
            if(mOrder?.id?.isNotBlank() == true) {
                OperatePickFailActivity.order(context, mOrder?.id!!, DispatchDetailActivity.CODE_PICK_FAIL)
            }
        }
        // 上报异常
        tv_batch_option_arrive_station_no.setOnClickListener {
            exceptionUpload();
        }
        tv_batch_option_re_arrive_station_no.setOnClickListener {
            exceptionUpload();
        }
        // 签收失败
        tv_batch_option_re_wait_sign_no.setOnClickListener {
            if(mOrder?.id?.isNotBlank() == true) {
                OperateSignFailActivity.order(context, mOrder?.id!!, DispatchDetailActivity.CODE_SIGN_FAIL)
            }
        }
    }

    fun resetStatusButton() {
        ll_dispatch_arrive_shop.gone();
        ll_dispatch_wait_pickup.gone();
        ll_dispatch_arrive_station.gone();
        ll_dispatch_wait_sign.gone();
        layoutOption.gone();
        when(mDispatch?.dispatchStatus) {
            DispatchOrderRecordBean.CANCEL,
            DispatchOrderRecordBean.CREATE,
            DispatchOrderRecordBean.REFUSE -> {
            }
            // 确认
            DispatchOrderRecordBean.WAIT_ARRANGED -> {
                if(DispatchConstants.isAgreeingTab(type!!)) {

                }
            }
            // 待到店
            DispatchOrderRecordBean.WAIT_ARRIVE_SHOP -> {
                if(DispatchConstants.isTakingTab(type!!)) {
                    layoutOption.visiable();
                    ll_dispatch_arrive_shop.visiable();
                }
            }
            // 待取货
            DispatchOrderRecordBean.WAIT_PICKUP -> {
                if(DispatchConstants.isTakingTab(type!!)) {
                    layoutOption.visiable();
                    ll_dispatch_wait_pickup.visiable();
                }
            }
            // 待送达
            DispatchOrderRecordBean.WAIT_ARRIVE_STATION -> {
                if(DispatchConstants.isDeliveringTab(type!!)) {
                    if(DispatchOrderItemBean.isWaitSign(mOrder?.orderStatus)) {
                        layoutOption.visiable();
                        ll_dispatch_wait_sign.visiable();
                    } else {
                        layoutOption.visiable();
                        ll_dispatch_arrive_station.visiable();
                    }
                }
            }
            // 待签收
            DispatchOrderRecordBean.WAIT_SIGN -> {
                if(DispatchConstants.isDeliveringTab(type!!)) {
                    layoutOption.visiable();
                    ll_dispatch_wait_sign.visiable();
                }
            }
            DispatchOrderRecordBean.SIGNED -> {

            }
            DispatchOrderRecordBean.SIGN_FAIL,
            DispatchOrderRecordBean.PICKUP_FAIL -> {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if((DispatchDetailActivity.CODE_SIGN == requestCode
                    || DispatchDetailActivity.CODE_SIGN_FAIL == requestCode
                    || DispatchDetailActivity.CODE_PICK_FAIL == requestCode
           )&& resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            getDetail()
        }
    }

}