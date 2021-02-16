package com.lingmiao.distribution.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lingmiao.distribution.bean.HomeParam;
import com.lingmiao.distribution.bean.HomeTwoParam;
import com.lingmiao.distribution.databinding.ItemHomeOrderBatchBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Create Date : 2020/12/203:26 PM
 * Auther      : Fox
 * Desc        :
 **/
public class HomeBatchOrderAdapter extends RecyclerView.Adapter<HomeBatchOrderAdapter.ViewHolder> {

    private List<HomeParam> mData;

    private OnListener mListener;

    public HomeBatchOrderAdapter(List<HomeParam> data, OnListener listener) {
        mData = data == null ? new ArrayList<>() : data;
        this.mListener = listener;
    }

    public List<HomeParam> getData() {
        return mData;
    }

    public void setData(List<HomeParam> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeOrderBatchBinding binding = ItemHomeOrderBatchBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding);
    }

    private HomeParam item;
    private HomeTwoParam order;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item = mData.get(position);
        if(item == null || item.getFirstOrder() == null) {
            return;
        }
        order = item.getFirstOrder();
        holder.mViewBinding.tvBatchStoreName.setText(order.consignerCustomerName);
        holder.mViewBinding.tvBatchOrderCount.setText(String.format("共%s单", item.getOrderCount()));
        holder.mViewBinding.tvBatchUserName.setText(order.consignerName);
        holder.mViewBinding.tvBatchUserPhone.setText(order.consignerPhone);
        holder.mViewBinding.tvBatchUserAddress.setText(order.getTempAddress());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemHomeOrderBatchBinding mViewBinding;

        public ViewHolder(ItemHomeOrderBatchBinding mViewBinding) {
            super(mViewBinding.getRoot());
            this.mViewBinding = mViewBinding;
            this.mViewBinding.llBatchContainer.setOnClickListener(v -> {
                if(mListener != null) {
                    mListener.itemClick(getAdapterPosition() - 1);
                }
            });
            this.mViewBinding.tvBatchUserPhone.setOnClickListener(v -> {
                if(mListener != null) {
                    mListener.clickPhone(getData().get(getAdapterPosition() - 1).getFirstOrderConsignerPhone());
                }
            });
            this.mViewBinding.tvBatchOptionYes.setOnClickListener(v -> {
                if(mListener != null) {
                    mListener.takePositive(getAdapterPosition() - 1);
                }
            });
            this.mViewBinding.tvBatchOptionNo.setOnClickListener(v -> {
                if(mListener != null) {
                    mListener.takeNegative(getAdapterPosition() - 1);
                }
            });
        }
    }

    public interface OnListener {

        void itemClick(int postition);

        void takePositive(int position);

        void takeNegative(int position);

        void clickPhone(String phone);
    }
}
