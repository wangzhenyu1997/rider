package com.lingmiao.distribution.ui.main.pop;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.MyApplication;
import com.lingmiao.distribution.bean.LabelsBean;
import com.lingmiao.distribution.databinding.DialogHomePushBinding;
import com.lingmiao.distribution.databinding.DialogTakeOrderBinding;
import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean;
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean;
import com.lingmiao.distribution.util.MathExtend;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;

import java.util.List;
import java.util.Objects;

/**
 * HomeConfirmDialog
 *
 * @author yandaocheng <br/>
 * 弹窗温馨提示
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class TakeOrderDialog extends Dialog {

    private DialogPushConfirmClick mLisetner;

    private DialogTakeOrderBinding viewBinding;
    private String mId = "";
    private DispatchOrderItemBean mData;
    private String mContent;
    private String mCancle;
    private String mSure;
    private String mOrderNum;

    public TakeOrderDialog(Context context, DialogPushConfirmClick lisetner, DispatchOrderItemBean data) {
        super(context);
        this.mData = data;
        this.mId = data.getId();
        this.mLisetner = lisetner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        viewBinding = DialogTakeOrderBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        initView();
        refreshView();
    }

    private void initView() {
        viewBinding.mSureOrder.setOnClickListener(v -> {
            mLisetner.sure(mId);
//            dismiss();
        });
        viewBinding.mRefuseOrder.setOnClickListener(v -> {
//            mLisetner.refuse(mId);
            dismiss();
        });
        viewBinding.closeDialog.setOnClickListener(v -> dismiss());
        viewBinding.mOrderNum.setOnClickListener(v -> {
            if (mOrderNum == null) {
                return;
            }

            ClipboardManager cm = (ClipboardManager) ActivityUtils.getTopActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(mOrderNum);
            ToastUtil.showToast(ActivityUtils.getTopActivity(), "订单号复制成功！");
        });
    }

    private void refreshView() {
        if (mData == null) {
            return;
        }
        //总配送费
        viewBinding.mPrice.setText("¥" + MathExtend.round(mData.getTotalCost(), 2));
//        String mPriceDetail = "";
//        mPriceDetail += "(包含小费<font color='#C73F4D'>¥" + MathExtend.round(mData.getTipCost(), 2) + "</font>";
//        mPriceDetail += "，奖励" + "<font color='#C73F4D'>¥" + MathExtend.round(mData.getRewardCost(), 2) + "</font>)";
//        //费用明细
//        viewBinding.mPriceDetail.setText(Html.fromHtml(mPriceDetail));

        DispatchOrderItemBean firstOrder = mData;

        //订单号
        mOrderNum = firstOrder.getOrderNo();
        viewBinding.mOrderNum.setText("订单号：" + firstOrder.getOrderNo());
        //状态
        viewBinding.mState.setText(mData.getOrderStatusStr());
        //时效
        viewBinding.mTime.setText(PublicUtil.isNull(firstOrder.getShowTimeRequire()));
        viewBinding.llTime.setVisibility(firstOrder.getShowTimeRequire() == null || firstOrder.getShowTimeRequire().length() == 0 || firstOrder.getShowTimeRequire().equals("null") ? View.GONE : View.VISIBLE);
        //商家名称
        viewBinding.mBusinessName.setText(PublicUtil.isNull(firstOrder.getConsignerCustomerName()));
        //商家地址
        viewBinding.mStartAddress.setText(PublicUtil.isNull(firstOrder.getConsignerAddressStr()));
        //商家距离
        if (firstOrder.getOriginDistance() != null && !firstOrder.getOriginDistance().equals("")) {
            viewBinding.mStartDistance.setText(PublicUtil.isNull(firstOrder.getOriginDistance()) + "公里");
        } else {
            viewBinding.mStartDistance.setText("");
        }
        //收货人地址
        viewBinding.mEndAddress.setText(PublicUtil.isNull(firstOrder.getConsigneeAddressStr()));
        //收货人距离
        if (firstOrder.getTargetDistance() != null && !firstOrder.getTargetDistance().equals("")) {
            viewBinding.mEndDistance.setText(PublicUtil.isNull(firstOrder.getTargetDistance()) + "公里");
        } else {
            viewBinding.mEndDistance.setText("");
        }
        //tap标签
        viewBinding.mTagLi.removeAllViews();
        if (firstOrder.getLabels() != null) {
            List<LabelsBean> mValue = new Gson().fromJson(firstOrder.getLabels(), new TypeToken<List<LabelsBean>>() {
            }.getType());
            for (LabelsBean param : mValue) {
                @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.tag_view, null);
                ((TextView) view.findViewById(R.id.m_tag)).setText(param.getLabel());
                ((TextView) view.findViewById(R.id.m_tag)).setTextColor(Color.parseColor(param.getColor()));
                GradientDrawable gd = new GradientDrawable();
                gd.setColor(Color.parseColor("#ffffff"));
                gd.setCornerRadius(5);
                gd.setStroke(2, Color.parseColor(param.getColor()));
                view.setBackgroundDrawable(gd);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.findViewById(R.id.m_tag).getLayoutParams());
                params.setMargins(20, 0, 0, 0);
                view.setLayoutParams(params);
                viewBinding.mTagLi.addView(view);
            }
        }
        //配送时间
        viewBinding.mDtTime.setText((PublicUtil.isNull(firstOrder.getPlanDeliveryTime()).equals("") ? "立即配送" : PublicUtil.isNull(firstOrder.getPlanDeliveryTime()) + " 配送"));
        //商品信息
        viewBinding.mGood.setText(mData.getGoodsName());
        //备注
        if (firstOrder.getCustomerRemark() != null && !firstOrder.getCustomerRemark().equals("")) {
            viewBinding.mRemarkView.setVisibility(View.VISIBLE);
            viewBinding.mRemark.setText(PublicUtil.isNull(firstOrder.getCustomerRemark()));
        }
    }

    /**
     * 获取账单状态
     */
    private String getDispatchName(int dispatchStatus) {
        String mName = "";
        switch (dispatchStatus) {
            case -1:
                mName = "已取消";
                break;
            case 0:
                mName = "创建调度单";
                break;
            case 1:
                mName = "骑手拒绝";
                break;
            case 2:
                mName = "待骑手确认";
                break;
            case 3:
                mName = "待到店";
                break;
            case 4:
                mName = "待取货";
                break;
            case 5:
                mName = "待送达";
                break;
            case 6:
                mName = "待签收";
                break;
            case 7:
                mName = "已签收";
                break;
            case 8:
                mName = "签收失败";
                break;
            case 9:
                mName = "取货失败";
                break;
        }
        return mName;
    }

    public interface DialogPushConfirmClick {
        void sure(String id);

        void refuse(String id);
    }
}
