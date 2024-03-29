package com.lingmiao.distribution.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.Utils;
import com.lingmiao.distribution.R;


/**
 * 订单tab view
 */
public class ITabWithNumberView extends RelativeLayout {
    private TextView tvTabTitle;
    private TextView tvTabNumber;
    private View viTabLine;

    public ITabWithNumberView(Context context) {
        this(context, null);
    }

    public ITabWithNumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ITabWithNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ITabWithNumberView, defStyleAttr, 0);

        if (attributes != null) {

            boolean isSelected = attributes.getBoolean(R.styleable.ITabWithNumberView_tvSelected, false);
            String title = attributes.getString(R.styleable.ITabWithNumberView_tvTitle);
            attributes.recycle();

            View view = View.inflate(context, R.layout.view_tab_with_number, this);
            tvTabTitle = view.findViewById(R.id.tvTabTitle);
            tvTabNumber = view.findViewById(R.id.tvTabHint);
            viTabLine = view.findViewById(R.id.viTabLine);
            tvTabTitle.setText(title);
            tvTabNumber.setVisibility(View.GONE);
            viTabLine.setVisibility(isSelected ? View.VISIBLE : View.GONE);
            setTextColor(tvTabTitle, isSelected);
            setTextColor(tvTabNumber, isSelected);
        }
    }

    public void setTabNumber(int number) {
        tvTabNumber.setVisibility(View.VISIBLE);
//        tvTabNumber.setVisibility(number > 0 ? View.VISIBLE : View.GONE);
        String numberString = number > 999 ? "999+" : (number + "");
        tvTabNumber.setText(String.format("(%s)", numberString));
    }

    public void setTabSelected(Boolean isSelected) {
        viTabLine.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        setTextColor(tvTabTitle, isSelected);
        setTextColor(tvTabNumber, isSelected);
    }

    private void setTextColor(TextView textView, boolean isSelected) {
        textView.setTextColor(ContextCompat.getColor(Utils.getApp(),isSelected ? R.color.colorTabOn : R.color.colorTabUn));
    }

}

