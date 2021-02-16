package com.lingmiao.distribution.ui.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.app.MyApp
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean

/**
Create Date : 2021/1/45:16 PM
Auther      : Fox
Desc        :
 **/
class HistoryDispatchAdapter : BaseQuickAdapter<DispatchOrderRecordBean, BaseViewHolder>(R.layout.main_adapter_history_dispatch) {

    private var order: DispatchOrderItemBean? = null;

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: DispatchOrderRecordBean?) {
        order = item?.getFirstOrder();

        helper.setText(R.id.tv_batch_no, String.format("调度单号：%s", item?.dispatchNo));
        helper.setText(R.id.tv_batch_status, item?.getDispatchStatusStr());

        helper.setText(R.id.tv_batch_store_name, order?.consignerCustomerName);
        helper.setText(R.id.tv_batch_order_count, String.format(
            "共%s单",
            item?.getOrderCount()
        ));
        helper.setText(R.id.tv_batch_user_name, order?.consignerName);
        helper.setText(R.id.tv_batch_user_phone, order?.consignerPhone);
        helper.setText(R.id.tv_batch_user_address, order?.getConsignerAddressStr());

        helper.addOnClickListener(R.id.iv_batch_no_copy);
        helper.setGone(R.id.ll_dispatch_signed, false);
        helper.setGone(R.id.layoutOption, false);
        when(item?.dispatchStatus) {
            DispatchOrderRecordBean.CANCEL -> {
                helper.setTextColor(R.id.tv_batch_status, MyApp.getInstance().resources.getColor(R.color.color999));
            }
            DispatchOrderRecordBean.SIGNED -> {
                helper.setGone(R.id.ll_dispatch_signed, true);
                helper.setGone(R.id.layoutOption, true);
                helper.setTextColor(R.id.tv_batch_status, MyApp.getInstance().resources.getColor(R.color.green));
            }
            DispatchOrderRecordBean.PICKUP_FAIL,
            DispatchOrderRecordBean.SIGN_FAIL -> {
                helper.setTextColor(R.id.tv_batch_status, MyApp.getInstance().resources.getColor(R.color.red));
            }
            else -> {
                helper.setTextColor(R.id.tv_batch_status, MyApp.getInstance().resources.getColor(R.color.color666));
            }
        }
    }
}