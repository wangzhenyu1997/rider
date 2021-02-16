package com.lingmiao.distribution.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lingmiao.distribution.R;
import com.lingmiao.distribution.bean.BasicListParam;
import com.lingmiao.distribution.databinding.HelpCenterItemBinding;

import java.util.List;

/**
 * HelpCenterAdapter
 *
 * @author yandaocheng <br/>
 * 帮助中心adapter
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class HelpCenterAdapter extends RecyclerView.Adapter<HelpCenterAdapter.ViewHolder> {

    private List<BasicListParam> mData;
    private Activity mActivity;

    public HelpCenterAdapter(List<BasicListParam> dataList, Activity activity) {
        this.mData = dataList;
        this.mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HelpCenterItemBinding view = HelpCenterItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.viewBinding.mContentView.setVisibility(View.GONE);
        if (mData.get(position).isSelectState()) {
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.arrow_up_help);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.viewBinding.mTittle.setCompoundDrawables(null, null, drawable, null);
            holder.viewBinding.mContentView.setVisibility(View.VISIBLE);
        } else {
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.arrow_down_help);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.viewBinding.mTittle.setCompoundDrawables(null, null, drawable, null);
        }
        holder.viewBinding.mTittle.setText(mData.get(position).getTitle());
        holder.viewBinding.mContent.setText(mData.get(position).getContent());
        holder.viewBinding.mTittle.setTag(R.id.tag_first, mData.get(position).isSelectState());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        HelpCenterItemBinding viewBinding;

        ViewHolder(HelpCenterItemBinding itemBinding) {
            super(itemBinding.getRoot());
            viewBinding = itemBinding;
            viewBinding.mTittle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.m_tittle) {  //点击展开闭合
                boolean mState = (boolean) v.getTag(R.id.tag_first);
                mData.get(getAdapterPosition()).setSelectState(!mState);
                notifyDataSetChanged();
            }
        }
    }

}