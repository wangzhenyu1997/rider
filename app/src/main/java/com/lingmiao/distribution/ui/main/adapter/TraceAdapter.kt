package com.lingmiao.distribution.ui.main.adapter

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.ui.main.bean.OrderTrack

/**
Create Date : 2021/1/62:06 PM
Auther      : Fox
Desc        :
 **/
class TraceAdapter(val activivty: FragmentActivity) : BaseQuickAdapter<OrderTrack, BaseViewHolder>(R.layout.main_adapter_trace) {

    private var urls : List<String>? = null;
    private var mImageAdapter : ImageViewAdapter ?= null;
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: OrderTrack?) {
        helper.setGone(R.id.tvTopLine, false);
        helper.setGone(R.id.vBottomLine, false);
        helper.setGone(R.id.vImageLine, false);

        helper.setText(R.id.tvTraceTime, item?.createTime);
        helper.setText(R.id.tvTraceContent, item?.content);

        if(helper.adapterPosition == 0) {
            helper.setGone(R.id.vBottomLine, true);
        } else if(helper.adapterPosition == itemCount - 1) {
            helper.setGone(R.id.tvTopLine, true);
        } else {
            helper.setGone(R.id.tvTopLine, true);
            helper.setGone(R.id.vBottomLine, true);
        }

        urls = if(item?.urls?.isNullOrEmpty() == false) item?.urls?.split(",") else arrayListOf();
        helper.setGone(R.id.vImageLine, if(urls?.size?:0 > 0 ) true else false);
        helper.getView<RecyclerView>(R.id.rvTraceImage).apply {
            layoutManager = GridLayoutManager(context, 3);
            adapter = ImageViewAdapter(activivty, urls).apply{
            }
        };

    }

}