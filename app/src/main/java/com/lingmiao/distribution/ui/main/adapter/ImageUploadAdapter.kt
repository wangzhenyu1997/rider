package com.lingmiao.distribution.ui.main.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.ui.common.bean.UploadDataBean

/**
Create Date : 2021/1/58:58 PM
Auther      : Fox
Desc        :
 **/
class ImageUploadAdapter : BaseQuickAdapter<UploadDataBean, BaseViewHolder>(R.layout.main_adapter_image_upload) {

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: UploadDataBean?) {
        if(item?.path?.isNotBlank() == true) {
            helper.setGone(R.id.deleteIv, true);
            Glide.with(mContext).load(item?.path).into(helper.getView<ImageView>(R.id.imageIv))
            //.error(R.mipmap.up_image_default)
        } else {
            helper.setImageResource(R.id.imageIv, R.mipmap.up_image_default);
            helper.setGone(R.id.deleteIv, false);
        }
        helper.addOnClickListener(R.id.imageIv);
        helper.addOnClickListener(R.id.deleteIv);
    }

}