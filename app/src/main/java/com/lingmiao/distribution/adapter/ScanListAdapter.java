package com.lingmiao.distribution.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.ClipboardManager;
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
import com.lingmiao.distribution.app.MyApplication;
import com.lingmiao.distribution.bean.HomeTwoParam;
import com.lingmiao.distribution.bean.LabelsBean;
import com.lingmiao.distribution.databinding.ItemScanOrderBinding;
import com.lingmiao.distribution.dialog.HomeConfirmDialog;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;

import java.util.List;

/**
 * @author fox
 */
public class ScanListAdapter extends RecyclerView.Adapter<ScanListAdapter.ViewHolder> {

    private List<HomeTwoParam> mData;

    private Activity mActivity;

    private OnScanItemListener mScanItemListener;

    private HomeTwoParam item;

    public ScanListAdapter(List<HomeTwoParam> dataList, Activity activity, OnScanItemListener onScanItemListener) {
        this.mData = dataList;
        this.mActivity = activity;
        this.mScanItemListener = onScanItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemScanOrderBinding view = ItemScanOrderBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        item = mData.get(position);
        holder.viewBinding.tvOrderPosition.setText(String.format("#%s", position + 1));
        if(item != null && mData.size() != 0) {
            //订单号
            holder.viewBinding.mOrderNum.setText(item.getOutOrderNo());
            holder.viewBinding.mOrderNum.setTag(R.id.tag_first, item.orderNo);
            //状态
            holder.viewBinding.mState.setText(item.getOrderStatusName());
            //实效
            holder.viewBinding.mTime.setText(item.showTimeRequire);
            holder.viewBinding.mTime.setVisibility(item.showTimeRequire == null || item.showTimeRequire.length() == 0 || item.showTimeRequire.equals("null") ? View.GONE : View.VISIBLE);
            //商家名称
            holder.viewBinding.mBusinessName.setText(PublicUtil.isNull(item.consignerCustomerName));
            //商家地址
            holder.viewBinding.mStartAddress.setText(PublicUtil.isNull(item.getConsignorFullAddress()));
            //商家距离
            holder.viewBinding.mStartDistance.setText(item.getOriginDistanceName());
            //送货位置
            holder.viewBinding.mEndAddress.setText(PublicUtil.isNull(item.getConsigneeFullAddress()));
            //送货距离
            holder.viewBinding.mEndDistance.setText(item.getTargetDistanceName());

            //tap标签（待补充）
            holder.viewBinding.mTagLi.removeAllViews();
            if (item.labels != null) {
                addLabelView(holder.viewBinding.mTagLi, item.labels);
            }
            //配送时间
            holder.viewBinding.mDtTime.setText((PublicUtil.isNull(item.planDeliveryTime).equals("") ? "立即配送" : PublicUtil.isNull(item.planDeliveryTime) + " 配送"));
            //商品信息
            holder.viewBinding.mGood.setText(item.goodsName);
            //用户备注
            holder.viewBinding.mRemark.setText(PublicUtil.isNull(item.customerRemark));
            holder.viewBinding.mRemarkView.setVisibility(item.customerRemark == null || item.customerRemark.length() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    private void addLabelView(LinearLayout group, String label) {
        List<LabelsBean> mValue = new Gson().fromJson(label, new TypeToken<List<LabelsBean>>() {
        }.getType());
        for (LabelsBean param : mValue) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(mActivity).inflate(R.layout.tag_view, null);
            ((TextView) view.findViewById(R.id.m_tag)).setText(param.getLabel());
            ((TextView) view.findViewById(R.id.m_tag)).setTextColor(Color.parseColor(param.getColor()));
            view.setBackgroundDrawable(getDrawable(param.getColor()));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.findViewById(R.id.m_tag).getLayoutParams());
            params.setMargins(20, 0, 0, 0);
            view.setLayoutParams(params);
            group.addView(view);
        }
    }

    private GradientDrawable getDrawable(String color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#ffffff"));
        gd.setCornerRadius(5);
        gd.setStroke(2, Color.parseColor(color));
        return gd;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemScanOrderBinding viewBinding;

        ViewHolder(ItemScanOrderBinding itemBinding) {
            super(itemBinding.getRoot());
            viewBinding = itemBinding;
            viewBinding.mItemView.setOnClickListener(this);
            viewBinding.mOrderNum.setOnClickListener(this);
            viewBinding.ibDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.m_order_num:  //复制订单号
                    String mOrderNum = (String) v.getTag(R.id.tag_first);
                    ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(mOrderNum);
                    ToastUtil.showToast(mActivity, "订单号复制成功！");
                    break;
                case R.id.ib_delete:
                    HomeConfirmDialog dialog = new HomeConfirmDialog(MyApplication.DHActivityManager.getManager().getTopActivity(), value -> {
                        if (value) {
                            if (mScanItemListener != null) {
                                mScanItemListener.onDelete(getAdapterPosition() - 1);
                            }
                        }
                    }, "删除提示", "请确认是否删除？", "取消", "确认删除");
                    dialog.setOnDismissListener(dialog1 -> {
                        viewBinding.swipeContent.close(true);
//                        ((SwipeRevealLayout)holder.getView(R.id.swipe_content)).close(true);
                    });
                    dialog.show();
                    break;
            }
        }
    }


    public interface OnScanItemListener {
        /**
         * 删除
         * @param position
         */
        void onDelete(int position);
    }
}