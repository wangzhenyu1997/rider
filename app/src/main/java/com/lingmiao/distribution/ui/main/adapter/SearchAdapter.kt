package com.lingmiao.distribution.ui.main.adapter

import android.text.Html
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.LabelsBean
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.fragment.DispatchTabFragment
import com.lingmiao.distribution.util.MathExtend
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.utils.exts.isNotBlank
/**
Create Date : 2020/12/283:18 PM
Auther      : Fox
Desc        :
 **/
class SearchAdapter : BaseQuickAdapter<DispatchOrderRecordBean, BaseViewHolder>(R.layout.main_adapter_dispatch_four) {

    private var order: DispatchOrderItemBean? = null;

    private var type : Int = DispatchConstants.DISPATCH_STATUS_AGREEING;

    fun setStatus(type : Int) {
        this.type = type;
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: DispatchOrderRecordBean?) {
        item?.apply {
            convertFourModel(helper, item);
        }
    }

    /**
     * 四轮
     */
    private fun convertFourModel(helper: BaseViewHolder, item: DispatchOrderRecordBean?) {
        order = item?.getFirstOrder();

        helper.setText(R.id.tv_batch_no, String.format("调度单号：%s", item?.dispatchNo));

        helper.setText(R.id.tv_batch_order_count, String.format(
            "共%s单",
            item?.getOrderCount()
        ));
        if(DispatchOrderRecordBean.isDeliveryStatus(item?.dispatchStatus?:0)) {
            if(order?.isSelfTake == 1) {
                // 自提点
                helper.setText(R.id.tv_batch_user_name, order?.siteManName);
                helper.setText(R.id.tv_batch_user_phone, order?.consigneeCustomerMobile);
            } else {
                helper.setText(R.id.tv_batch_user_name, order?.consigneeName);
                helper.setText(R.id.tv_batch_user_phone, order?.consigneePhone);
            }
            helper.setText(R.id.tv_batch_user_address, order?.getConsigneeAddressStr());
            helper.setText(R.id.tv_batch_store_name, order?.consigneeCustomerName);
        } else {
            helper.setText(R.id.tv_batch_store_name, order?.consignerCustomerName);
            helper.setText(R.id.tv_batch_user_name, order?.consignerName);
            helper.setText(R.id.tv_batch_user_phone, order?.consignerPhone);
            helper.setText(R.id.tv_batch_user_address, order?.getConsignerAddressStr());
        }

        helper.addOnClickListener(R.id.iv_dispatch_order_num_copy);
        helper.addOnClickListener(R.id.tv_batch_user_phone);
        resetButtonOption(helper);


        when(item?.dispatchStatus) {
            DispatchOrderRecordBean.CANCEL,
            DispatchOrderRecordBean.CREATE,
            DispatchOrderRecordBean.REFUSE -> {

            }
            DispatchOrderRecordBean.WAIT_ARRANGED -> {
                if(DispatchConstants.isAgreeingTab(type)) {
                    helper.setGone(R.id.ll_dispatch_wait_agree, true);
                }
            }
            DispatchOrderRecordBean.WAIT_ARRIVE_SHOP -> {
                if(DispatchConstants.isTakingTab(type)) {
                    helper.setGone(R.id.ll_dispatch_arrive_shop, true);
                }
            }
            DispatchOrderRecordBean.WAIT_PICKUP -> {
                if(DispatchConstants.isTakingTab(type)) {
                    helper.setGone(R.id.ll_dispatch_wait_pickup, true);
                }
            }
            DispatchOrderRecordBean.WAIT_ARRIVE_STATION -> {
                if(DispatchConstants.isDeliveringTab(type)) {
                    if(DispatchOrderItemBean.isWaitSign(item?.getFirstOrder()?.orderStatus)) {
                        helper.setGone(R.id.ll_dispatch_wait_sign, true);
                    } else {
                        helper.setGone(R.id.ll_dispatch_arrive_station, true);
                    }
                }
            }
            DispatchOrderRecordBean.WAIT_SIGN -> {
                if(DispatchConstants.isDeliveringTab(type)) {
                    helper.setGone(R.id.ll_dispatch_wait_sign, true);
                }
            }
            DispatchOrderRecordBean.SIGNED -> {
                helper.setGone(R.id.ll_dispatch_signed, true);
            }
            DispatchOrderRecordBean.SIGN_FAIL,
            DispatchOrderRecordBean.PICKUP_FAIL -> {

            }
        }
    }

    fun resetButtonOption(helper: BaseViewHolder) {
        // 拒绝
        helper.addOnClickListener(R.id.tv_batch_option_agree_no);
        // 接单
        helper.addOnClickListener(R.id.tv_batch_option_agree_yes);
        // 上报异常
        helper.addOnClickListener(R.id.tv_batch_option_arrive_shop_no);
        // 到达取货点
        helper.addOnClickListener(R.id.tv_batch_option_arrive_shop_yes);
        // 取货失败
        helper.addOnClickListener(R.id.tv_batch_option_wait_pickup_no);
        // 取货成功
        helper.addOnClickListener(R.id.tv_batch_option_wait_pickup_yes);
        // 上报异常
        helper.addOnClickListener(R.id.tv_batch_option_arrive_station_no);
        // 送达收货点
        helper.addOnClickListener(R.id.tv_batch_option_arrive_station_yes);
        // 签收失败
        helper.addOnClickListener(R.id.tv_batch_option_wait_sign_no);
        // 签收成功
        helper.addOnClickListener(R.id.tv_batch_option_wait_sign_yes);
        // 投诉客户
        helper.addOnClickListener(R.id.tv_batch_option_signed_no)
        // 评价客户
        helper.addOnClickListener(R.id.tv_batch_option_signed_yes)

        helper.setGone(R.id.ll_dispatch_wait_agree, false);
        helper.setGone(R.id.ll_dispatch_arrive_shop, false);
        helper.setGone(R.id.ll_dispatch_wait_pickup, false);
        helper.setGone(R.id.ll_dispatch_arrive_station, false);
        helper.setGone(R.id.ll_dispatch_wait_sign, false);
        helper.setGone(R.id.ll_dispatch_signed, false);
    }

}