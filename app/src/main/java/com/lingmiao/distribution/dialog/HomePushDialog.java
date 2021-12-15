package com.lingmiao.distribution.dialog;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.MyApplication;
import com.lingmiao.distribution.bean.HomeDetailBean;
import com.lingmiao.distribution.bean.LabelsBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.DialogHomePushBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.MathExtend;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * HomeConfirmDialog
 *
 * @author yandaocheng <br/>
 * 弹窗温馨提示
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class HomePushDialog extends Dialog {

    private DialogPushConfirmClick mLisetner;

    private DialogHomePushBinding viewBinding;
    private String mId = "";
    private String mContent;
    private String mCancle;
    private String mSure;
    private String mOrderNum;

    public HomePushDialog(Context context, DialogPushConfirmClick lisetner, String id) {
        super(context);
        this.mId = id;
        this.mLisetner = lisetner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        viewBinding = DialogHomePushBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        initView();
        initData();
    }

    private void initView() {
        viewBinding.mSureOrder.setOnClickListener(v -> {
            mLisetner.sure(mId);
//            dismiss();
        });
        viewBinding.mRefuseOrder.setOnClickListener(v -> {
            mLisetner.refuse(mId);
//            dismiss();
        });
        viewBinding.closeDialog.setOnClickListener(v -> dismiss());
        viewBinding.mOrderNum.setOnClickListener(v -> {
            if (mOrderNum == null) {
                return;
            }
            ClipboardManager cm = (ClipboardManager) MyApplication.DHActivityManager.getManager().getTopActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(mOrderNum);
            ToastUtil.showToast(MyApplication.DHActivityManager.getManager().getTopActivity(), "订单号复制成功！");
        });
    }

    private void initData() {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("id", mId);
        OkHttpUtils.postAync(Constant.AppDispatchDetail, new Gson().toJson(mMap), new HttpCallback<HomeDetailBean>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(HomeDetailBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null) {
                    //总配送费
                    viewBinding.mPrice.setText("¥" + MathExtend.round(response.getData().deliveryCost, 2));
                    String mPriceDetail = "";
                    if (!MathExtend.round(response.getData().tipCost, 2).equals("0.00") && !MathExtend.round(response.getData().tipCost, 2).equals("--")) {
                        mPriceDetail += "(包含小费<font color='#C73F4D'>¥" + MathExtend.round(response.getData().tipCost, 2) + "</font>";
                    }
                    if (!MathExtend.round(response.getData().rewardCost, 2).equals("0.00") && !MathExtend.round(response.getData().rewardCost, 2).equals("--")) {
                        mPriceDetail += "，奖励" + "<font color='#C73F4D'>¥" + MathExtend.round(response.getData().rewardCost, 2) + "</font>)";
                    }
                    viewBinding.mPriceDetail.setText(Html.fromHtml(mPriceDetail));//费用明细
//                    viewBinding.mPriceDetail.setText(Html.fromHtml("(包含小费<font color='#C73F4D'>¥" + MathExtend.round(response.getData().tipCost, 2) +
//                            "</font>，奖励" + "<font color='#C73F4D'>¥" + MathExtend.round(response.getData().rewardCost, 2) + "</font>)"));//费用明细
                    if (response.getData().orderList != null && response.getData().orderList.size() != 0) {
                        viewBinding.mOrderNum.setText("订单号：" + response.getData().orderList.get(0).orderNo);        //订单号
                        mOrderNum = response.getData().orderList.get(0).orderNo;
                        viewBinding.mState.setText(getDispatchName(response.getData().dispatchStatus));    //状态
                        viewBinding.mTime.setText(PublicUtil.isNull(response.getData().orderList.get(0).showTimeRequire));//时效
                        viewBinding.llTime.setVisibility(response.getData().orderList.get(0).showTimeRequire == null || response.getData().orderList.get(0).showTimeRequire.length() == 0 || response.getData().orderList.get(0).showTimeRequire.equals("null") ? View.GONE : View.VISIBLE);
                        viewBinding.mBusinessName.setText(PublicUtil.isNull(response.getData().orderList.get(0).consignerCustomerName));//商家名称
                        String mConsignerDetailAddress = PublicUtil.isNull(response.getData().orderList.get(0).consignerProvince) + PublicUtil.isNull(response.getData().orderList.get(0).consignerCity) +
                                PublicUtil.isNull(response.getData().orderList.get(0).consignerDistrict) + PublicUtil.isNull(response.getData().orderList.get(0).consignerStreet) +
                                PublicUtil.isNull(response.getData().orderList.get(0).consignerAddress);
                        viewBinding.mStartAddress.setText(PublicUtil.isNull(mConsignerDetailAddress));     //商家地址
                        if (response.getData().orderList.get(0).originDistance != null && !response.getData().orderList.get(0).originDistance.equals("")) {
                            viewBinding.mStartDistance.setText(PublicUtil.isNull(response.getData().orderList.get(0).originDistance) + "公里");       //商家距离
                        } else {
                            viewBinding.mStartDistance.setText("");       //商家距离
                        }
                        String mConsigneeDetailAddress = PublicUtil.isNull(response.getData().orderList.get(0).consigneeProvince) + PublicUtil.isNull(response.getData().orderList.get(0).consigneeCity) +
                                PublicUtil.isNull(response.getData().orderList.get(0).consigneeDistrict) + PublicUtil.isNull(response.getData().orderList.get(0).consigneeStreet) +
                                PublicUtil.isNull(response.getData().orderList.get(0).consigneeAddress);
                        viewBinding.mEndAddress.setText(PublicUtil.isNull(mConsigneeDetailAddress));       //收货人地址
                        if (response.getData().orderList.get(0).targetDistance != null && !response.getData().orderList.get(0).targetDistance.equals("")) {
                            viewBinding.mEndDistance.setText(PublicUtil.isNull(response.getData().orderList.get(0).targetDistance) + "公里");        //收货人距离
                        } else {
                            viewBinding.mEndDistance.setText("");        //收货人距离
                        }
                        //tap标签
                        viewBinding.mTagLi.removeAllViews();
                        if (response.getData().orderList.get(0).labels != null) {
                            List<LabelsBean> mValue = new Gson().fromJson(response.getData().orderList.get(0).labels, new TypeToken<List<LabelsBean>>() {
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
                        viewBinding.mDtTime.setText((PublicUtil.isNull(response.getData().orderList.get(0).planDeliveryTime).equals("") ? "立即配送" : PublicUtil.isNull(response.getData().orderList.get(0).planDeliveryTime) + " 配送")); //配送时间
                        viewBinding.mGood.setText(response.getData().goodsInfo);                                                        //商品信息
                        if (response.getData().orderList.get(0).customerRemark != null && !response.getData().orderList.get(0).customerRemark.equals("")) {
                            viewBinding.mRemarkView.setVisibility(View.VISIBLE);
                            viewBinding.mRemark.setText(PublicUtil.isNull(response.getData().orderList.get(0).customerRemark));             //备注
                        }
                    }
                } else {
                    ToastUtil.showToast(getContext(), response.getMessage());
                }
            }
        });
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
