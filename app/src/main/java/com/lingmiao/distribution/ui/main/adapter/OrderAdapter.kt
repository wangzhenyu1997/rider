package com.lingmiao.distribution.ui.main.adapter

import android.text.Html
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.LabelsBean
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.util.MathExtend
import com.lingmiao.distribution.util.PublicUtil
import com.james.common.utils.exts.isNotBlank

/**
Create Date : 2020/12/283:18 PM
Auther      : Fox
Desc        :
 **/
class OrderAdapter(val type : Int) : BaseQuickAdapter<DispatchOrderItemBean, BaseViewHolder>(R.layout.main_adapter_order) {

    private var item : DispatchOrderRecordBean ? = null;

    fun setItem(item: DispatchOrderRecordBean) {
        this.item = item;
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: DispatchOrderItemBean?) {
        convertModel(helper, item);
    }

    private fun convertModel(helper: BaseViewHolder, order : DispatchOrderItemBean?) {
        // 订单号
        helper.setText(R.id.tv_dispatch_order_num, String.format("订单号：%s", order?.orderNo));
        helper.setTag(R.id.tv_dispatch_order_num, order?.orderNo);
        // 总价
        helper.setText(R.id.tv_dispatch_price, String.format("¥%s", "" + MathExtend.round(order?.totalCost!!, 2)))
        // 价格明细
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            helper.setText(R.id.tv_dispatch_price_detail, Html.fromHtml(item?.getPriceDetailStr(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            helper.setText(R.id.tv_dispatch_price_detail, Html.fromHtml(item?.getPriceDetailStr()));
        }
        // 状态
        helper.setText(R.id.tv_dispatch_state, order?.getOrderStatusStr());
        // 发货人
        helper.setText(R.id.tv_dispatch_business_name, order?.consignerCustomerName);
        // 发货地址
        helper.setText(R.id.tv_dispatch_start_address, item?.getConsignerAddress());
        // 发货距离
        helper.setText(R.id.tv_dispatch_start_distance, String.format("%s%s", PublicUtil.isNull(order?.originDistance), if(order?.originDistance?.length?:0 > 0) "公里" else ""));
        // 收货地址
        helper.setText(R.id.tv_dispatch_end_address, item?.getConsigneeAddress());
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
        helper.setText(R.id.tv_dispatch_good, item?.goodsInfo);
        // 备注
        helper.setGone(R.id.rl_dispatch_remark_view, order?.customerRemark?.isNotBlank()?:false);
        helper.setText(R.id.tv_dispatch_remark, PublicUtil.isNull(order?.customerRemark))

        helper.addOnClickListener(R.id.iv_dispatch_order_num_copy);

        setStatusImage(helper, order);

        resetButtonOption(helper);

        addButtonClickListener(helper);

        setButtonOption(helper, order);
    }

    fun setStatusImage(helper: BaseViewHolder, order: DispatchOrderItemBean) {
        when(order?.orderStatus) {
            DispatchOrderItemBean.WAIT_ARRIVE_STATION,
            DispatchOrderItemBean.WAIT_SIGN -> {
                helper.setImageResource(R.id.iv_dispatch_start_view, R.mipmap.home_blue_circle)
                helper.setImageResource(R.id.iv_dispatch_end_view, R.mipmap.home_red_circle_no)
            }
            DispatchOrderItemBean.SIGNED -> {
                helper.setImageResource(R.id.iv_dispatch_start_view, R.mipmap.home_blue_circle)
                helper.setImageResource(R.id.iv_dispatch_end_view, R.mipmap.home_red_circle)
            }
            else -> {
                helper.setImageResource(R.id.iv_dispatch_start_view, R.mipmap.home_blue_circle_no)
                helper.setImageResource(R.id.iv_dispatch_end_view, R.mipmap.home_red_circle_no)
            }
        }
    }

    fun resetButtonOption(helper: BaseViewHolder) {
        helper.setGone(R.id.layoutOption, false);
        helper.setGone(R.id.ll_dispatch_arrive_shop, false);
        helper.setGone(R.id.ll_dispatch_wait_pickup, false);
        helper.setGone(R.id.ll_dispatch_arrive_station, false);
        helper.setGone(R.id.ll_dispatch_wait_sign, false);
    }

    fun addButtonClickListener(helper: BaseViewHolder) {
        // 上报异常
        helper.addOnClickListener(R.id.tv_batch_option_arrive_shop_no);
        helper.addOnClickListener(R.id.tv_batch_option_re_arrive_shop_no);
        // 取货失败
        helper.addOnClickListener(R.id.tv_batch_option_re_wait_pickup_no);
        // 上报异常
        helper.addOnClickListener(R.id.tv_batch_option_arrive_station_no);
        helper.addOnClickListener(R.id.tv_batch_option_re_arrive_station_no);
        // 签收失败
        helper.addOnClickListener(R.id.tv_batch_option_re_wait_sign_no);
    }

    fun setButtonOption(helper: BaseViewHolder, order: DispatchOrderItemBean) {
        when(item?.dispatchStatus) {
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
                    helper.setGone(R.id.layoutOption, true);
                    helper.setGone(R.id.ll_dispatch_arrive_shop, true)
                }
            }
            // 待取货
            DispatchOrderRecordBean.WAIT_PICKUP -> {
                if(DispatchConstants.isTakingTab(type!!)) {
                    helper.setGone(R.id.layoutOption, true);
                    helper.setGone(R.id.ll_dispatch_wait_pickup, true)
                }
            }
            // 待送达
            DispatchOrderRecordBean.WAIT_ARRIVE_STATION -> {
                if(DispatchConstants.isDeliveringTab(type!!)) {
                    if(DispatchOrderItemBean.isWaitSign(order?.orderStatus)) {
                        helper.setGone(R.id.layoutOption, true);
                        helper.setGone(R.id.ll_dispatch_wait_sign, true)
                    } else {
                        helper.setGone(R.id.layoutOption, true);
                        helper.setGone(R.id.ll_dispatch_arrive_station, true)
                    }
                }
            }
            // 待签收
            DispatchOrderRecordBean.WAIT_SIGN -> {
                if(DispatchConstants.isDeliveringTab(type!!)) {
                    helper.setGone(R.id.layoutOption, true);
                    helper.setGone(R.id.ll_dispatch_wait_sign, true)
                }
            }
            DispatchOrderRecordBean.SIGNED -> {

            }
            DispatchOrderRecordBean.SIGN_FAIL,
            DispatchOrderRecordBean.PICKUP_FAIL -> {

            }
        }
    }

}