package com.lingmiao.distribution.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lingmiao.distribution.ui.fragment.HomeListFragment;


public class VPHomeAdapter extends FragmentPagerAdapter {

    private int mStartPosition;

    public VPHomeAdapter(FragmentManager fm, int index) {
        super(fm);
        this.mStartPosition = index;
    }

    @Override
    public Fragment getItem(int position) {
        return HomeListFragment.getInstance(position, mStartPosition);
    }

    @Override
    public int getCount() {
        return 3;
    }

}
