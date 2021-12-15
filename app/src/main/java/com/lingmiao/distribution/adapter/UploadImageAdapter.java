package com.lingmiao.distribution.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.bean.Filepath;
import com.lingmiao.distribution.util.GlideUtil;

import java.util.ArrayList;


public class UploadImageAdapter extends BaseAdapter implements OnClickListener {
    private Context mContext;
    private ArrayList<Filepath> mData;
    private SelectPicCallBack callback;
    private int maxLength;

    /**
     * @param context
     * @param mData
     * @param maxLens 限制最多上传数量
     */
    public UploadImageAdapter(Context context, ArrayList<Filepath> mData, int maxLens, SelectPicCallBack callback) {
        super();
        this.mContext = context;
        this.mData = mData;
        this.maxLength = maxLens;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        if (mData.size() == maxLength) {
            return mData.size();
        }
        return (mData.size() + 1);
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image_upload, parent, false);
            holder = new ViewHolder(convertView);
            holder.mImg.setOnClickListener(this);
            holder.mImgDelete.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.mImg.setClickable(false);
        holder.mImg.setTag(R.id.tag_first, position);
        holder.mImgDelete.setTag(position);
        if (position == mData.size()) {
            Glide.with(mContext).load(R.mipmap.up_image_default)
                    .into(holder.mImg);
            holder.mImg.setClickable(true);
            if (position == maxLength) {
                holder.mImg.setVisibility(View.GONE);
            }
            holder.mImgDelete.setVisibility(View.GONE);
        } else {
            holder.mImgDelete.setVisibility(View.VISIBLE);
            GlideUtil.load(mContext, mData.get(position).getFilePath(), holder.mImg, GlideUtil.getOption());
        }
        return convertView;
    }

    public class ViewHolder {
        private ImageView mImg;
        private ImageView mImgDelete;

        public ViewHolder(View itemView) {
            mImg = itemView.findViewById(R.id.ii_img);                // 图片
            mImgDelete = itemView.findViewById(R.id.ii_delete);       // 删除图片
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ii_img:
                int index = (Integer) v.getTag(R.id.tag_first);
                callback.addImg(index);
                break;
            case R.id.ii_delete:
                int position = (Integer) v.getTag();
                callback.deleteImg(position);
                break;
            default:
                break;
        }
    }

    public interface SelectPicCallBack {
        void addImg(int position);

        void deleteImg(int position);
    }
}
