package com.lingmiao.distribution.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lingmiao.distribution.databinding.HomeImageItemBinding;
import com.lingmiao.distribution.dialog.DialogShowImageFragment;
import com.lingmiao.distribution.ui.activity.HomeDetailActivity;
import com.lingmiao.distribution.util.GlideUtil;

import java.util.List;

/**
 * ImageAdapter
 *
 * @author yandaocheng <br/>
 * 配送详细图片页面adapter
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class HomeImageAdapter extends RecyclerView.Adapter<HomeImageAdapter.ViewHolder> {

    private List<String> mData;
    private Activity mActivity;
    private int parentPosition;

    public HomeImageAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void setDataList(List<String> dataList, int position) {
        this.mData = dataList;
        this.parentPosition = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeImageItemBinding view = HomeImageItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        GlideUtil.load(mActivity, mData.get(position), holder.viewBinding.mImage, GlideUtil.getOption());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        HomeImageItemBinding viewBinding;
        ViewHolder(HomeImageItemBinding itemBinding) {
            super(itemBinding.getRoot());
            viewBinding = itemBinding;
            viewBinding.mImage.setOnClickListener(v -> {
                DialogShowImageFragment imageFragment = new DialogShowImageFragment();
                imageFragment.setData(mData, getAdapterPosition());
                imageFragment.show(((HomeDetailActivity) mActivity).getSupportFragmentManager(), null);
            });
        }

    }

}