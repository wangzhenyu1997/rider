package com.lingmiao.distribution.ui.main.adapter

import android.text.Html
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.LabelsBean
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean
import com.lingmiao.distribution.util.MathExtend
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.utils.exts.isNotBlank

/**
Create Date : 2020/12/283:18 PM
Auther      : Fox
Desc        :
 **/
class OrderVieAdapter() : BaseQuickAdapter<DispatchOrderItemBean, BaseViewHolder>(R.layout.main_adapter_dispatch_two) {

    private var order: DispatchOrderItemBean? = null;

    private var newOrderId : String? = ""

    fun setOrderId(id : String) {
        newOrderId = id;
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: DispatchOrderItemBean?) {
        item?.apply {
            convertItem(helper, item);
        }
    }

    private fun convertItem(helper: BaseViewHolder, item: DispatchOrderItemBean?) {
        order = item;
        // 订单号
        helper.setText(R.id.tv_dispatch_order_num, String.format("订单号：%s", order?.orderNo));
        helper.setTag(R.id.tv_dispatch_order_num, order?.orderNo);
        // 总价
        helper.setText(R.id.tv_dispatch_price, String.format("¥%s", "" + MathExtend.round(item?.totalCost!!, 2)))
        helper.setGone(R.id.tv_dispatch_price_detail, false);
        // 状态
        helper.setText(R.id.tv_dispatch_state, "待骑手抢单");
        // 发货人
        helper.setText(R.id.tv_dispatch_business_name, order?.consignerCustomerName);
        // 发货地址
        helper.setText(R.id.tv_dispatch_start_address, item?.getConsignerAddressStr());
        // 发货距离
        helper.setText(R.id.tv_dispatch_start_distance, String.format("%s%s", PublicUtil.isNull(order?.originDistance), if(order?.originDistance?.length?:0 > 0) "公里" else ""));
        // 收货地址
        helper.setText(R.id.tv_dispatch_end_address, item?.getConsigneeAddressStr());
        // 收货距离
        helper.setText(R.id.tv_dispatch_end_distance, String.format("%s%s", PublicUtil.isNull(order?.targetDistance), if(order?.targetDistance?.length?:0 > 0) "公里" else ""));
        // 标签
        helper.getView<LinearLayout>(R.id.ll_dispatch_tag)?.removeAllViews();
        order?.apply {
            val mValue: List<LabelsBean> = getLabelList(labels!!);
            for (param in mValue) {
                helper.getView<LinearLayout>(R.id.ll_dispatch_tag).addView(getTagView(mContext, param));
            }
        }
        // 外部订单号
        helper.setText(R.id.tv_dispatch_dt_out_order_id, order?.getOutOrderNo());
        helper.setGone(R.id.tv_dispatch_dt_out_order_id, order?.outOrderNoIsEmpty() == false);
        // 配送时间
        helper.setText(R.id.tv_dispatch_dt_time, order?.getPlanDeliveryTimeStr());
        // 实效
        helper.setText(R.id.tv_dispatch_time, PublicUtil.isNull(order?.showTimeRequire));
        helper.setGone(R.id.tv_dispatch_time, PublicUtil.isNull(order?.showTimeRequire)?.length?:0 > 0)
        // 商品信息
        helper.setText(R.id.tv_dispatch_good, item?.goodsName);
        // 备注
        helper.setGone(R.id.rl_dispatch_remark_view, order?.customerRemark?.isNotBlank()?:false);
        helper.setText(R.id.tv_dispatch_remark, PublicUtil.isNull(order?.customerRemark))
        resetButtonOption(helper);
        helper.addOnClickListener(R.id.iv_dispatch_order_num_copy);
        //待抢货
        helper.setGone(R.id.ll_dispatch_wait_agree, true);
        helper.setImageResource(R.id.iv_dispatch_start_view, R.mipmap.home_blue_circle_no);
        helper.setImageResource(R.id.iv_dispatch_end_view, R.mipmap.home_red_circle_no);
        helper.addOnClickListener(R.id.tv_dispatch_start_distance);
        helper.addOnClickListener(R.id.tv_dispatch_end_distance);
    }

    private fun resetButtonOption(helper: BaseViewHolder) {
        // 拒绝
        helper.addOnClickListener(R.id.tv_batch_option_agree_no);
        // 接单
        helper.addOnClickListener(R.id.tv_batch_option_agree_yes);
        helper.setGone(R.id.ll_dispatch_wait_agree, false);
    }

}