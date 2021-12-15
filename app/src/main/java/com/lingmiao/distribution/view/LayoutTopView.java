package com.lingmiao.distribution.view;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingmiao.distribution.R;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.SystemAppUtils;

/**
 * Activity
 *
 * @author yandaocheng <br/>
 * 标题栏封装
 * 2019-06-03
 * 修改者，修改日期，修改内容
 */
public class LayoutTopView extends FrameLayout {

    private LayoutInflater mInflater;
    private Context mContext;

    private ImageView mBack;
    private TextView mTittle;
    private ImageView mRightImage;
    private TextView mRightTx;

    public LayoutTopView(@NonNull Context context) {
        this(context, null);
    }

    public LayoutTopView(@NonNull Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LayoutTopView(@NonNull Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = getContext();
        mInflater = LayoutInflater.from(getContext());
        setTopView();
    }

    /**
     * @param callback   点击事件回调
     * @param tittle     标题
     * @param rightImage 右侧图片（默认是隐藏的）
     * @param rightTx    右侧文字（默认是隐藏的）
     */
    public void setData(final TopCallback callback, Boolean backShow, String tittle, int rightImage, String rightTx) {
        if (backShow != null && backShow) mBack.setVisibility(View.VISIBLE);
        if (tittle != null) mTittle.setText(tittle);
        if (rightImage != 0) {
            mRightImage.setImageResource(rightImage);
            mRightImage.setVisibility(View.VISIBLE);
        }
        if (rightTx != null) {
            mRightTx.setText(rightTx);
            mRightTx.setVisibility(View.VISIBLE);
        }
        if (callback != null) {
            mBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onTopBack();
                }
            });
            mRightImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onRightImage();
                }
            });
            mRightTx.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onRightTx();
                }
            });
        }
    }

    public void setRightImage(int rightImage) {
        if (rightImage != 0) {
            mRightImage.setImageResource(rightImage);
            mRightImage.setVisibility(View.VISIBLE);
        } else {
            mRightImage.setVisibility(View.GONE);
        }
    }

    public void setTitle(String tittle) {
        if (tittle != null) mTittle.setText(tittle);
    }

    public void setTitleOnClickListener(final TopCallback callback) {
        if (callback != null) {
            mTittle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onTitle();
                }
            });
        }
    }


    /**
     * 初始化view
     */
    private void setTopView() {
        View view = mInflater.inflate(R.layout.layout_top_title, this, false);
        mBack = view.findViewById(R.id.top_back);
        mTittle = view.findViewById(R.id.top_title);
        mRightImage = view.findViewById(R.id.top_right_image);
        mRightTx = view.findViewById(R.id.top_right_tv);
        RelativeLayout mLayout = view.findViewById(R.id.t_top_view);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemAppUtils.getStatusHeight(mContext) + PublicUtil.dip2px(mContext, 44));
        mLayout.setLayoutParams(param);
        addView(view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mHeadH = SystemAppUtils.getStatusHeight(mContext) + PublicUtil.dip2px(mContext, 44);
        setWillNotDraw(false);
        setMeasuredDimension(widthMeasureSpec, mHeadH);
    }

    public abstract static class TopCallback {
        public void onTopBack() {
        }

        public void onRightImage() {
        }

        public void onRightTx() {
        }

        public void onTitle() {
        }
    }

}
