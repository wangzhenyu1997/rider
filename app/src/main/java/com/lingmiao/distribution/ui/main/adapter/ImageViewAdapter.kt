package com.lingmiao.distribution.ui.main.adapter

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.dialog.DialogShowImageFragment
import com.lingmiao.distribution.ui.photo.GlideUtils

/**
Create Date : 2021/1/58:58 PM
Auther      : Fox
Desc        :
 **/
class ImageViewAdapter(val activivty: FragmentActivity, list: List<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.main_adapter_image, list) {

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: String?) {
        helper.setGone(R.id.deleteIv, false);
        if(item?.isNotBlank() == true) {
            Glide.with(mContext).load(item).into(helper.getView<ImageView>(R.id.imageIv))
        } else {
            Glide.with(mContext).load(R.mipmap.default_image).into(helper.getView<ImageView>(R.id.imageIv))
        }
        helper.getView<ImageView>(R.id.imageIv).setOnClickListener {
            var imageFragment = DialogShowImageFragment();
            imageFragment.setData(mData, helper.adapterPosition);
            imageFragment.show(activivty?.getSupportFragmentManager(), null);
        }
    }

}