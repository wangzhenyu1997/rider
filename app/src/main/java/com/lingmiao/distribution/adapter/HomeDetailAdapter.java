package com.lingmiao.distribution.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lingmiao.distribution.bean.HomeGjParam;
import com.lingmiao.distribution.databinding.HomeDetailItemBinding;
import com.lingmiao.distribution.util.PublicUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * HomeDetailAdapter
 *
 * @author yandaocheng <br/>
 * 配送详细页面adapter
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class HomeDetailAdapter extends RecyclerView.Adapter<HomeDetailAdapter.ViewHolder> {

    private List<HomeGjParam> mData;
    private Activity mActivity;

    public HomeDetailAdapter(List<HomeGjParam> dataList, Activity activity) {
        this.mData = dataList;
        this.mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeDetailItemBinding view = HomeDetailItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.viewBinding.mTopLine.setVisibility(View.GONE);
        holder.viewBinding.mBottomLine.setVisibility(View.GONE);
        holder.viewBinding.mImageLine.setVisibility(View.GONE);
        if (mData.size() != 0) {
            if (position == 0) {
                holder.viewBinding.mBottomLine.setVisibility(View.VISIBLE);
                holder.viewBinding.mImageLine.setVisibility(View.VISIBLE);
            } else if (position == mData.size() - 1) {
                holder.viewBinding.mTopLine.setVisibility(View.VISIBLE);
            } else {
                holder.viewBinding.mTopLine.setVisibility(View.VISIBLE);
                holder.viewBinding.mBottomLine.setVisibility(View.VISIBLE);
                holder.viewBinding.mImageLine.setVisibility(View.VISIBLE);
            }
        }
        holder.viewBinding.mTime.setText(PublicUtil.isNull(mData.get(position).createTime));
        holder.viewBinding.mContent.setText(PublicUtil.isNull(mData.get(position).content));
        List<String> mImageData = new ArrayList<>();
        if (mData.get(position).urls != null) {
            Collections.addAll(mImageData, mData.get(position).urls.split(","));
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(mActivity);
            layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.viewBinding.mImageRecyclerview.setLayoutManager(layoutManager2);
            holder.mImageAdapter.setDataList(mImageData, position);
            holder.viewBinding.mImageRecyclerview.setAdapter(holder.mImageAdapter);
            holder.mImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        HomeDetailItemBinding viewBinding;

        HomeImageAdapter mImageAdapter;

        ViewHolder(HomeDetailItemBinding itemBinding) {
            super(itemBinding.getRoot());
            viewBinding = itemBinding;
            mImageAdapter = new HomeImageAdapter(mActivity);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.m_sure_order:     //确认接单
//                    mCallBack.sureOrder();
//                    break;

            }
        }
    }

}