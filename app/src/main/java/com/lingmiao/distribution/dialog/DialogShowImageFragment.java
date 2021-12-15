package com.lingmiao.distribution.dialog;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lingmiao.distribution.R;
import com.lingmiao.distribution.view.HackyViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DialogShowImageFragment
 *
 * @author yandaocheng <br/>
 * 图片放大处理
 * 2017-06-21
 * 修改者，修改日期，修改内容
 */
public class DialogShowImageFragment extends DialogFragment implements BigImageFragment.DialogCallBack {

    private List<String> pathList = new ArrayList<>();
    private int mPosition;
    private LinearLayout mViewPoint;

    /**
     * 用volatile修饰的变量，
     * 线程在每次使用变量的时候，都会读取变量修改后的最的值。
     * volatile很容易被误用，用来进行原子性操作。
     */
//    @SuppressLint("StaticFieldLeak")
//    private static volatile DialogShowImageFragment myAlertDialogFragment = null;
//
//    public static DialogShowImageFragment getInstance() {
//        if (myAlertDialogFragment == null) {
//            synchronized (DialogShowImageFragment.class) {
//                if (myAlertDialogFragment == null) {
//                    myAlertDialogFragment = new DialogShowImageFragment();
//                }
//            }
//        }
//        return myAlertDialogFragment;
//    }
    @Override
    public void onStart() {
        super.onStart();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        Objects.requireNonNull(getDialog().getWindow()).getDecorView().setSystemUiVisibility(uiOptions);
    }


    @Override
    public void imageCallBack() {
        dismiss();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Window window = this.getDialog().getWindow();
        assert window != null;
        window.getDecorView().setPadding(0, 0, 0, 0);//去掉dialog默认的padding
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.windowAnimations = R.style.dialogAnim;//设置dialog的动画
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable());

        @SuppressLint("InflateParams") View inflate = inflater.inflate(R.layout.activity_view_pager, null);
        HackyViewPager mViewPager = inflate.findViewById(R.id.view_pager);
        mViewPoint = inflate.findViewById(R.id.view_point);
        mViewPager.setAdapter(new ImageViewPagerAdapter(getChildFragmentManager(), pathList));
        mViewPoint.removeAllViews();
        for (int i = 0; i < pathList.size(); i++) {
            View point = new View(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (i == mPosition) {
                point.setBackgroundResource(R.drawable.page_indicator_f);
            } else {
                point.setBackgroundResource(R.drawable.page_indicator_white);
            }
            if (i > 0) {
                params.leftMargin = 30;// 设置圆点间隔
            }
            point.setLayoutParams(params);// 设置圆点的大小
            mViewPoint.addView(point);// 将圆点添加给线性布局
        }
        mViewPager.setOffscreenPageLimit(pathList.size());//表示几个页面之间来会切换不会重新加载
        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < pathList.size(); i++) {
                    mViewPoint.getChildAt(i).setBackgroundResource(R.drawable.page_indicator_white);
                    if (i == position) {
                        mViewPoint.getChildAt(i).setBackgroundResource(R.drawable.page_indicator_f);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        return inflate;
    }

    public void setData(List<String> pathlist, int position) {
        this.pathList = pathlist;
        this.mPosition = position;
    }

    private class ImageViewPagerAdapter extends FragmentPagerAdapter {
        List<String> mDatas;

        ImageViewPagerAdapter(FragmentManager fm, List<String> data) {
            super(fm);
            mDatas = data;
        }

        @Override
        public Fragment getItem(int position) {
            String url = mDatas.get(position);
            return BigImageFragment.newInstance(url, DialogShowImageFragment.this);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }
    }
}