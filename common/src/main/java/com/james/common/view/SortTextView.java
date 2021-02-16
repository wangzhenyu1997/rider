package com.james.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.james.common.R;

/**
 * Create Date : 2020/12/303:52 PM
 * Auther      : Fox
 * Desc        :
 **/
@SuppressLint("AppCompatCustomView")
public class SortTextView extends AppCompatTextView {

    private Context mContext;

    private Drawable mAscRightDrawable;
    private Drawable mDescRightDrawable;
    private int sortValue = 0;

    public static final int SORT_ASC = 1;
    public static final int SORT_DESC = 0;

    public SortTextView(Context context) {
        this(context, null);
    }

    public SortTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SortTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SortView, defStyleAttr, 0);
        if(attributes != null) {
            mAscRightDrawable = attributes.getDrawable(R.styleable.SortView_sortAscDrawableRight);
            mDescRightDrawable = attributes.getDrawable(R.styleable.SortView_sortDescDrawableRight);
            attributes.recycle();

            this.resetCompoundDrawables();
        }
    }

    public void setAscRightDrawable(@DrawableRes int drawable) {
        mAscRightDrawable = this.mContext.getDrawable(drawable);
    }

    public void setDescRightDrawable(@DrawableRes int drawable) {
        mDescRightDrawable = this.mContext.getDrawable(drawable);
    }

    public void resetCompoundDrawables() {
        if(isSelected()) {
            super.setCompoundDrawables(null, null, isSortAsc() ? mAscRightDrawable : mDescRightDrawable, null);
        } else {
            super.setCompoundDrawables(null, null, mDescRightDrawable, null);
        }
    }

    public boolean isSortAsc() {
        return sortValue == SORT_ASC;
    }

    public boolean isSortDesc() {
        return sortValue == SORT_DESC;
    }

    public int getSortValue() {
        return sortValue;
    }

    public void setSortValue(int value) {
        this.sortValue = value;
    }

    public void shiftSortStatus() {
        sortValue = isSortAsc() ? SORT_DESC : SORT_ASC;
    }

    @Override
    public void setSelected(boolean selected) {
        if(isSelected() == selected) {
            shiftSortStatus();
        } else {
            sortValue = SORT_DESC;
        }
        super.setSelected(selected);
        resetCompoundDrawables();
    }
}
