package com.lingmiao.distribution.ui.main.adapter

import android.text.Html
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.utils.exts.isNotBlank
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.LabelsBean
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.util.MathExtend
import com.lingmiao.distribution.util.PublicUtil
import java.text.SimpleDateFormat
import java.util.*


/**
Create Date : 2020/12/283:18 PM
Auther      : Fox
Desc        :
 **/
class DispatchAdapter(val type: Int) :
    BaseMultiItemQuickAdapter<DispatchOrderRecordBean, BaseViewHolder>(null) {

    private var order: DispatchOrderItemBean? = null

    init {
        addItemType(0, R.layout.main_adapter_dispatch_four)
        addItemType(1, R.layout.main_adapter_dispatch_two)
    }

    private var newOrderId: String? = ""

    fun setOrderId(id: String) {
        newOrderId = id;
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: DispatchOrderRecordBean?) {
        item?.apply {
            if (0 == item?.itemType) {
                convertFourModel(helper, item)
            } else if (1 == item?.itemType) {
                convertTwoModel(helper, item)
            }
        }
    }

    /**
     * 四轮
     */
    private fun convertFourModel(helper: BaseViewHolder, item: DispatchOrderRecordBean?) {
        order = item?.getFirstOrder()

        // 新订单显示标识
        helper.setGone(
            R.id.newOrderIv,
            type == DispatchConstants.DISPATCH_STATUS_AGREEING && newOrderId == item?.id
        )

        helper.setText(R.id.tv_batch_no, String.format("调度单号：%s", item?.dispatchNo))

        helper.setText(
            R.id.tv_batch_order_count, String.format(
                "共%s单",
                item?.getOrderCount()
            )
        )
        if (DispatchOrderRecordBean.isDeliveryStatus(item?.dispatchStatus ?: 0)) {
            if (order?.isSelfTake == 1) {
                // 自提点
                helper.setText(R.id.tv_batch_user_name, order?.siteManName)
                helper.setText(R.id.tv_batch_user_phone, order?.consigneeCustomerMobile)
            } else {
                helper.setText(R.id.tv_batch_user_name, order?.consigneeName)
                helper.setText(R.id.tv_batch_user_phone, order?.consigneePhone)
            }
            helper.setText(R.id.tv_batch_user_address, order?.getConsigneeAddressStr())
            helper.setText(R.id.tv_batch_store_name, order?.consigneeCustomerName)
        } else {
            helper.setText(R.id.tv_batch_store_name, order?.consignerCustomerName)
            helper.setText(R.id.tv_batch_user_name, order?.consignerName)
            helper.setText(R.id.tv_batch_user_phone, order?.consignerPhone)
            helper.setText(R.id.tv_batch_user_address, order?.getConsignerAddressStr())
        }

        helper.addOnClickListener(R.id.iv_dispatch_order_num_copy)
        helper.addOnClickListener(R.id.tv_batch_user_phone)
        resetButtonOption(helper)


        when (item?.dispatchStatus) {
            DispatchOrderRecordBean.CANCEL,
            DispatchOrderRecordBean.CREATE,
            DispatchOrderRecordBean.REFUSE -> {

            }
            DispatchOrderRecordBean.WAIT_ARRANGED -> {
                if (DispatchConstants.isAgreeingTab(type)) {
                    helper.setGone(R.id.ll_dispatch_wait_agree, true)
                }
            }
            DispatchOrderRecordBean.WAIT_ARRIVE_SHOP -> {
                if (DispatchConstants.isTakingTab(type)) {
                    helper.setGone(R.id.ll_dispatch_arrive_shop, true)
                }
            }
            DispatchOrderRecordBean.WAIT_PICKUP -> {
                if (DispatchConstants.isTakingTab(type)) {
                    helper.setGone(R.id.ll_dispatch_wait_pickup, true)
                }
            }
            DispatchOrderRecordBean.WAIT_ARRIVE_STATION -> {
                if (DispatchConstants.isDeliveringTab(type)) {
                    if (DispatchOrderItemBean.isWaitSign(item?.getFirstOrder()?.orderStatus)) {
                        helper.setGone(R.id.ll_dispatch_wait_sign, true)
                    } else {
                        helper.setGone(R.id.ll_dispatch_arrive_station, true)
                    }
                }
            }
            DispatchOrderRecordBean.WAIT_SIGN -> {
                if (DispatchConstants.isDeliveringTab(type)) {
                    helper.setGone(R.id.ll_dispatch_wait_sign, true)
                }
            }
            DispatchOrderRecordBean.SIGNED -> {
                helper.setGone(R.id.ll_dispatch_signed, true)
            }
            DispatchOrderRecordBean.SIGN_FAIL,
            DispatchOrderRecordBean.PICKUP_FAIL -> {

            }
        }
    }

    /**
     * 二轮
     */
    private fun convertTwoModel(helper: BaseViewHolder, item: DispatchOrderRecordBean?) {
        order = item?.getFirstOrder()
        // 订单号
        helper.setText(R.id.tv_dispatch_order_num, String.format("订单号：%s", order?.orderNo))
        helper.setTag(R.id.tv_dispatch_order_num, order?.orderNo)
        // 总价
        helper.setText(
            R.id.tv_dispatch_price,
            String.format("¥%s", "" + MathExtend.round(item?.totalCost!!, 2))
        )
        // 价格明细
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            helper.setText(
                R.id.tv_dispatch_price_detail,
                Html.fromHtml(item?.getPriceDetailStr(), Html.FROM_HTML_MODE_LEGACY)
            )
        } else {
            helper.setText(R.id.tv_dispatch_price_detail, Html.fromHtml(item?.getPriceDetailStr()))
        }
        // 状态
        helper.setText(R.id.tv_dispatch_state, item?.getDispatchStatusStr())
        // 发货人
        helper.setText(R.id.tv_dispatch_business_name, order?.consignerCustomerName)
        // 发货地址
        helper.setText(R.id.tv_dispatch_start_address, item?.getConsignerAddress())
        // 发货距离
        helper.setText(
            R.id.tv_dispatch_start_distance,
            String.format(
                "%s%s",
                order?.originDistance,
                if (order?.originDistance?.length ?: 0 > 0) "公里" else ""
            )
        )
        // 收货地址
        helper.setText(R.id.tv_dispatch_end_address, item?.getConsigneeAddress());
        // 收货距离
        helper.setText(
            R.id.tv_dispatch_end_distance,
            String.format(
                "%s%s",
                order?.targetDistance,
                if (order?.targetDistance?.length ?: 0 > 0) "公里" else ""
            )
        );
        // 标签
        helper.getView<LinearLayout>(R.id.ll_dispatch_tag)?.removeAllViews();
        order?.apply {
            val mValue: List<LabelsBean> = getLabelList(labels!!);
            for (param in mValue) {
                helper.getView<LinearLayout>(R.id.ll_dispatch_tag)
                    .addView(getTagView(mContext, param));
            }
        }
        // 外部订单号
        helper.setText(R.id.tv_dispatch_dt_out_order_id, order?.getOutOrderNo());
        helper.setGone(R.id.tv_dispatch_dt_out_order_id, order?.outOrderNoIsEmpty() == false);
        //接单时间
        helper.setText(R.id.tv_dispatch_dt_time, item.getAcceptTimeStr())
        // 实效
        helper.setGone(R.id.tv_dispatch_time, order?.timeRequire != null)
        val time = try {
            val temp =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.acceptTime + " 00:00:00").time

            SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(temp + order?.timeRequire?.toLong()!! * 60 * 1000))
        } catch (e: Exception) {
            ""
        }
        helper.setText(R.id.tv_dispatch_time, "$time 送达")
        // 商品信息
        helper.setText(R.id.tv_dispatch_good, item?.goodsInfo)
        // 备注
        helper.setGone(R.id.rl_dispatch_remark_view, order?.customerRemark?.isNotBlank() ?: false)
        helper.setText(R.id.tv_dispatch_remark, PublicUtil.isNull(order?.customerRemark))

        helper.addOnClickListener(R.id.iv_dispatch_order_num_copy)
        resetButtonOption(helper);
        helper.addOnClickListener(R.id.tv_dispatch_start_distance)
        helper.addOnClickListener(R.id.tv_dispatch_end_distance)

        when (item?.dispatchStatus) {
            DispatchOrderRecordBean.CANCEL,
            DispatchOrderRecordBean.CREATE,
            DispatchOrderRecordBean.REFUSE -> {

            }
            DispatchOrderRecordBean.WAIT_ARRANGED -> {
                helper.setGone(R.id.ll_dispatch_wait_agree, true)
                //待取货
                helper.setImageResource(R.id.iv_dispatch_start_view, R.mipmap.home_blue_circle_no)
                helper.setImageResource(R.id.iv_dispatch_end_view, R.mipmap.home_red_circle_no)
            }
            DispatchOrderRecordBean.WAIT_ARRIVE_SHOP -> {
                helper.setGone(R.id.ll_dispatch_arrive_shop, true)
                helper.setImageResource(R.id.iv_dispatch_start_view, R.mipmap.home_blue_circle_no)
                helper.setImageResource(R.id.iv_dispatch_end_view, R.mipmap.home_red_circle_no)
            }
            DispatchOrderRecordBean.WAIT_PICKUP -> {
                helper.setGone(R.id.ll_dispatch_wait_pickup, true)
                helper.setImageResource(R.id.iv_dispatch_start_view, R.mipmap.home_blue_circle_no)
                helper.setImageResource(R.id.iv_dispatch_end_view, R.mipmap.home_red_circle_no)
            }
            DispatchOrderRecordBean.WAIT_ARRIVE_STATION -> {
                helper.setGone(R.id.ll_dispatch_arrive_station, true)
                helper.setImageResource(R.id.iv_dispatch_start_view, R.mipmap.home_blue_circle)
                helper.setImageResource(R.id.iv_dispatch_end_view, R.mipmap.home_red_circle_no)
            }
            DispatchOrderRecordBean.WAIT_SIGN -> {
                helper.setGone(R.id.ll_dispatch_wait_sign, true);
                helper.setImageResource(R.id.iv_dispatch_start_view, R.mipmap.home_blue_circle);
                helper.setImageResource(R.id.iv_dispatch_end_view, R.mipmap.home_red_circle_no);
            }
            DispatchOrderRecordBean.SIGNED -> {
                helper.setGone(R.id.ll_dispatch_signed, true);
                helper.setImageResource(R.id.iv_dispatch_start_view, R.mipmap.home_blue_circle)
                helper.setImageResource(R.id.iv_dispatch_end_view, R.mipmap.home_red_circle)
            }
            DispatchOrderRecordBean.SIGN_FAIL -> {

            }
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