package com.lingmiao.distribution.ui.main.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.BasicListParam

/**
Create Date : 2021/1/711:19 PM
Auther      : Fox
Desc        :
 **/
class ReasonAdapter : BaseQuickAdapter<BasicListParam, BaseViewHolder>(R.layout.main_adapter_reason) {

    private var selectedPosition : Int ? = -1;

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: BasicListParam?) {
        helper.setGone(R.id.bottom_view, helper?.adapterPosition < itemCount - 1)
        helper.setText(R.id.m_content, item?.label)
        helper.setChecked(R.id.m_content, item?.isSelectState?:false)
        helper.addOnClickListener(R.id.m_content)
    }
}