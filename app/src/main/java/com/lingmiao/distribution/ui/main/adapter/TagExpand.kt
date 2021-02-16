package com.lingmiao.distribution.ui.main.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.LabelsBean

/**
Create Date : 2021/1/63:10 PM
Auther      : Fox
Desc        :
 **/

fun getTagView(context : Context, param : LabelsBean) : View {
    val view = LayoutInflater.from(context).inflate(R.layout.tag_view, null)
    val tag = view.findViewById<View>(R.id.m_tag) as TextView;
    tag.text = param.label
    tag.setTextColor(Color.parseColor(param.color))

    val params = LinearLayout.LayoutParams(tag.layoutParams)
    params.setMargins(20, 0, 0, 0)
    view.layoutParams = params;
    view.setBackground(getGradientDrawable(param.color))
    return view;
}


fun getGradientDrawable(color : String) : GradientDrawable {
    val gd = GradientDrawable()
    gd.setColor(Color.parseColor("#ffffff"))
    gd.cornerRadius = 5f
    gd.setStroke(2, Color.parseColor(color))
    return gd;
}

fun getLabelList(labels : String) : List<LabelsBean> {
    return GsonUtils.fromJson<List<LabelsBean>>(labels, object : TypeToken<List<LabelsBean?>?>() {}.type);
}


