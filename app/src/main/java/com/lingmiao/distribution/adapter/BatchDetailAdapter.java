package com.lingmiao.distribution.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.ClipboardManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.bean.HomeParam;
import com.lingmiao.distribution.bean.HomeTwoParam;
import com.lingmiao.distribution.bean.LabelsBean;
import com.lingmiao.distribution.databinding.BatchItemBinding;
import com.lingmiao.distribution.util.MathExtend;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;

import java.util.List;

/**
 * HomeListAdapter
 *
 * @author yandaocheng <br/>
 * 配送页面adapter
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class BatchDetailAdapter extends RecyclerView.Adapter<BatchDetailAdapter.ViewHolder> {

    private List<HomeTwoParam> mData;
    private int mIndex;
    private OnHomeListener mCallBack;
    private Activity mActivity;

    public BatchDetailAdapter(List<HomeTwoParam> dataList, int index, OnHomeListener callBack, Activity activity) {
        this.mData = dataList;
        this.mIndex = index;
        this.mActivity = activity;
        this.mCallBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BatchItemBinding view = BatchItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(view);
    }

    private HomeTwoParam mItem;

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        mItem = mData.get(position);

        holder.viewBinding.mPrice.setText("¥" + MathExtend.round(mItem.totalCost, 2)); //花费费用
        String mPriceDetail = "";
//        mPriceDetail += "(包含小费<font color='#C73F4D'>¥" + MathExtend.round(mData.get(position).tipCost, 2) + "</font>";
//        mPriceDetail += "，奖励" + "<font color='#C73F4D'>¥" + MathExtend.round(mData.get(position).rewardCost, 2) + "</font>)";

        //费用明细
        holder.viewBinding.mPriceDetail.setText(Html.fromHtml(mPriceDetail));

        holder.viewBinding.mRemarkView.setVisibility(View.GONE);
        holder.viewBinding.mDtOutOrderId.setVisibility(View.GONE);


        holder.viewBinding.mDtOutOrderId.setText(mItem.getOutOrderNo());
        holder.viewBinding.mDtOutOrderId.setVisibility(mItem.outOrderNoIsEmpty() ? View.GONE : View.VISIBLE);
        //订单号
        holder.viewBinding.mOrderNum.setText("订单号：" + mItem.orderNo);
        holder.viewBinding.mOrderNum.setTag(R.id.tag_first, mItem.orderNo);
        //状态
        holder.viewBinding.mState.setText(mItem.getOrderStatusName());
        //实效
        holder.viewBinding.mTime.setText(PublicUtil.isNull(mItem.showTimeRequire));
        holder.viewBinding.mTime.setVisibility(mItem.showTimeRequire == null || mItem.showTimeRequire.length() == 0 || mItem.showTimeRequire.equals("null") ? View.GONE : View.VISIBLE);
        //商家名称
        holder.viewBinding.mBusinessName.setText(PublicUtil.isNull(mItem.consignerCustomerName));
        String mConsignerDetailAddress = PublicUtil.isNull(mItem.consignerProvince) + PublicUtil.isNull(mItem.consignerCity) +
                PublicUtil.isNull(mItem.consignerDistrict) + PublicUtil.isNull(mItem.consignerStreet) +
                PublicUtil.isNull(mItem.consignerAddress);
        //商家地址
        holder.viewBinding.mStartAddress.setText(PublicUtil.isNull(mConsignerDetailAddress));
        //商家距离
        if (mItem.originDistance != null && !mItem.originDistance.equals("")) {
            holder.viewBinding.mStartDistance.setText(PublicUtil.isNull(mItem.originDistance) + "公里");
        } else {
            holder.viewBinding.mStartDistance.setText("");
        }
        String mConsigneeDetailAddress = PublicUtil.isNull(mItem.consigneeProvince) + PublicUtil.isNull(mItem.consigneeCity) +
                PublicUtil.isNull(mItem.consigneeDistrict) + PublicUtil.isNull(mItem.consigneeStreet) +
                PublicUtil.isNull(mItem.consigneeAddress);
        //送货位置
        holder.viewBinding.mEndAddress.setText(PublicUtil.isNull(mConsigneeDetailAddress));
        if (mItem.targetDistance != null && !mItem.targetDistance.equals("")) {
            //送货距离
            holder.viewBinding.mEndDistance.setText(PublicUtil.isNull(mItem.targetDistance) + "公里");
        } else {
            //送货距离
            holder.viewBinding.mEndDistance.setText("");
        }

        //tap标签（待补充）
        holder.viewBinding.mTagLi.removeAllViews();
        if (mItem.labels != null) {
            List<LabelsBean> mValue = new Gson().fromJson(mItem.labels, new TypeToken<List<LabelsBean>>() {
            }.getType());
            for (LabelsBean param : mValue) {
                @SuppressLint("InflateParams") View view = LayoutInflater.from(mActivity).inflate(R.layout.tag_view, null);
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
                holder.viewBinding.mTagLi.addView(view);
            }
        }
        //配送时间
        holder.viewBinding.mDtTime.setText((PublicUtil.isNull(mItem.planDeliveryTime).equals("") ? "立即配送" : PublicUtil.isNull(mItem.planDeliveryTime) + " 配送"));
        //商品信息
        holder.viewBinding.mGood.setText(mItem.goodsName);

        if (mItem.customerRemark != null && !mItem.customerRemark.equals("")) {
            holder.viewBinding.mRemarkView.setVisibility(View.VISIBLE);
            //用户备注
            holder.viewBinding.mRemark.setText(PublicUtil.isNull(mItem.customerRemark));
        }

        holder.viewBinding.mBtnOne.setVisibility(View.GONE);
        holder.viewBinding.mBtnTwo.setVisibility(View.GONE);
        holder.viewBinding.mBtnThree.setVisibility(View.GONE);
        holder.viewBinding.mBtnFour.setVisibility(View.GONE);
        holder.viewBinding.mBtnFive.setVisibility(View.GONE);
        holder.viewBinding.mBtnSix.setVisibility(View.GONE);
        if (mIndex == 0) {
            //待取货
            holder.viewBinding.mStartView.setImageResource(R.mipmap.home_blue_circle_no);
            holder.viewBinding.mEndView.setImageResource(R.mipmap.home_red_circle_no);
            if (mData.get(position).getOrderStatus() == 2) {
                //拒绝接单/确认接单
                holder.viewBinding.mBtnOne.setVisibility(View.VISIBLE);
            } else if (mData.get(position).getOrderStatus() == 3) {
                //上报异常/到达取货点
                holder.viewBinding.mBtnTwo.setVisibility(View.VISIBLE);
            } else if (mData.get(position).getOrderStatus() == 4) {
                //取货失败/取货成功
                holder.viewBinding.mBtnThree.setVisibility(View.VISIBLE);
            }
        } else if (mIndex == 1) {
            //待送达
            holder.viewBinding.mStartView.setImageResource(R.mipmap.home_blue_circle);
            holder.viewBinding.mEndView.setImageResource(R.mipmap.home_red_circle_no);
            if (mData.get(position).getOrderStatus() == 5) {
                //上报异常/到达收货点
                holder.viewBinding.mBtnFour.setVisibility(View.VISIBLE);
            } else if (mData.get(position).getOrderStatus() == 6) {
                //签收失败/签收成功
                holder.viewBinding.mBtnFive.setVisibility(View.VISIBLE);
            }
        } else {
            //已结束
            holder.viewBinding.mStartView.setImageResource(R.mipmap.home_blue_circle);
            holder.viewBinding.mEndView.setImageResource(R.mipmap.home_red_circle);
            if (mData.get(position).getOrderStatus() == 7) {
                // 投诉客户/评价客户
                holder.viewBinding.mBtnSix.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        BatchItemBinding viewBinding;

        ViewHolder(BatchItemBinding itemBinding) {
            super(itemBinding.getRoot());
            viewBinding = itemBinding;
            viewBinding.mSureOrder.setOnClickListener(this);
            viewBinding.mRefuseOrder.setOnClickListener(this);
            viewBinding.mPickGood.setOnClickListener(this);
            viewBinding.mPickFail.setOnClickListener(this);
            viewBinding.mPickSuccess.setOnClickListener(this);
            viewBinding.mSignSuccess.setOnClickListener(this);
            viewBinding.mUpAbnormal.setOnClickListener(this);
            viewBinding.mUpAbnormalTwo.setOnClickListener(this);
            viewBinding.mReceivingGoods.setOnClickListener(this);
            viewBinding.mSignFail.setOnClickListener(this);
            viewBinding.mComplaintCustomer.setOnClickListener(this);
            viewBinding.mEvaluateCustomer.setOnClickListener(this);
            viewBinding.mItemView.setOnClickListener(this);
            viewBinding.mOrderNum.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.m_sure_order:     //确认接单
                    mCallBack.sureOrder(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_refuse_order:   //拒绝接单
                    mCallBack.refuseOrder(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_up_abnormal:    //上报异常
                    mCallBack.upAbnormal(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_pick_good:      //到达取货点
                    mCallBack.pickGood(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_pick_fail:      //取货失败
                    mCallBack.pickFail(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_pick_success:   //取货成功
                    mCallBack.pickSuccess(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_up_abnormal_two: //上报异常Two
                    mCallBack.upAbnormalTwo(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_receiving_goods: //到达收货点
                    mCallBack.receivingGoods(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_sign_fail:  //签收失败
                    mCallBack.signFail(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_sign_success:   //签收成功
                    mCallBack.signSuccess(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_complaint_customer: //投诉客户
                    mCallBack.complaintCustomer(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_evaluate_customer: //评价客户
                    mCallBack.evaluateCustomer(mData.get(getAdapterPosition() - 1).getId());
                    break;
                case R.id.m_item_view:      //跳转配送详细
                    mCallBack.itemClick(getAdapterPosition() - 1);
                    break;
                case R.id.m_order_num:  //复制订单号
                    String mOrderNum = (String) v.getTag(R.id.tag_first);
                    ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(mOrderNum);
                    ToastUtil.showToast(mActivity, "订单号复制成功！");
                    break;
            }
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

    public interface OnHomeListener {
        void itemClick(int postition);

        void sureOrder(String id);

        void refuseOrder(String id);

        void upAbnormal(String id);

        void pickGood(String id);

        void pickFail(String id);

        void pickSuccess(String id);

        void upAbnormalTwo(String id);

        void receivingGoods(String id);

        void signFail(String id);

        void signSuccess(String id);

        void complaintCustomer(String orderId);

        void evaluateCustomer(String orderId);
    }
}