package com.lingmiao.distribution.ui.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R

/**
Create Date : 2021/1/259:14 PM
Auther      : Fox
Desc        :
 **/
class RemarkAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.main_adapter_remark) {

    override fun convert(helper: BaseViewHolder, item: String?) {
        helper.setText(R.id.remarkTv, item);
        helper.addOnClickListener(R.id.remarkAddIv)
    }
}