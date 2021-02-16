package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.nrecyclerview.interfaces.LoadingListener;
import com.google.gson.Gson;
import com.lingmiao.distribution.adapter.HomeListAdapter;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.HomeBean;
import com.lingmiao.distribution.bean.HomeParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityBatchDetailBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryOrderActivity extends ActivitySupport {

    private ActivityBatchDetailBinding mViewBinding;

    private HomeListAdapter mAdapter;

    private List<HomeParam> mListData;

    private int mPage = 1;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        mViewBinding = ActivityBatchDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(mViewBinding.getRoot());

        initTitle();

        initRecycleView();
    }

    private void initData() {
        mPage = 1;
        mListData.clear();
        mAdapter.notifyDataSetChanged();
        mViewBinding.mRecyclerView.setNoMore(false);
        getData();
    }

    private void initTitle() {
        mViewBinding.topView.setData(new LayoutTopView.TopCallback() {
            @Override
            public void onTopBack() {
                super.onTopBack();
                finish();
            }
        }, true, "列表详情", 0, "");
    }

    private void initRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mViewBinding.mRecyclerView.setLayoutManager(linearLayoutManager);
        mViewBinding.mRecyclerView.setLoadingListener(new LoadingListener() {
            @Override
            public void onLoadMore() {
                mPage += 1;
                getData();
            }

            @Override
            public void onRefresh() {
                super.onRefresh();
                initData();
            }
        });

        mListData = new ArrayList<>();
        mAdapter = new HomeListAdapter(mListData, 0, new HomeListAdapter.OnHomeListener() {
            @Override
            public void itemClick(int postition) {

            }

            @Override
            public void sureOrder(String id) {

            }

            @Override
            public void refuseOrder(String id) {

            }

            @Override
            public void upAbnormal(String id) {

            }

            @Override
            public void pickGood(String id) {

            }

            @Override
            public void pickFail(String id) {

            }

            @Override
            public void pickSuccess(String id) {

            }

            @Override
            public void upAbnormalTwo(String id) {

            }

            @Override
            public void receivingGoods(String id) {

            }

            @Override
            public void signFail(String id) {

            }

            @Override
            public void signSuccess(String id) {

            }

            @Override
            public void complaintCustomer(String orderId) {

            }

            @Override
            public void evaluateCustomer(String orderId) {

            }
        }, this);

        mViewBinding.mRecyclerView.setAdapter(mAdapter);

        mViewBinding.mRecyclerView.refresh();
    }

    private void getData() {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("pageNum", String.valueOf(mPage));
        mMap.put("pageSize", "10");
        Map<String, Object> mMapState = new HashMap<>();
        List<Integer> mState = new ArrayList<>();

        mState.add(7);
        mState.add(8);
        mState.add(9);
        mMapState.put("dispatchStatusArray", mState);
        mMapState.put("longitude", Constant.LOCATIONLONGITUDE);
        mMapState.put("latitude", Constant.LOCATIONLATITUDE);
        mMap.put("body", mMapState);
        OkHttpUtils.postAync(Constant.AppDispathList, new Gson().toJson(mMap), new HttpCallback<HomeBean>(this, getProgressDialog()) {
            @Override
            public void onSuccess(HomeBean response) {
                super.onSuccess(response);
                mViewBinding.mRecyclerView.refreshComplete();//刷新成功
                mViewBinding.mRecyclerView.loadMoreComplete();//加载成功
                if (response.getData() != null && response.getData().getTotalCount() == 0) {
                    mViewBinding.mRecyclerView.setFootViewText("", "暂无数据");
                }else{
                    mViewBinding.mRecyclerView.setFootViewText("", "没有更多了");
                }
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().getRecords() != null && response.getData().getRecords().size() != 0) {
                    mListData.addAll(response.getData().getRecords());
                    if (mListData.size() == response.getData().getTotalCount()) {
                        //没有更多了
                        mViewBinding.mRecyclerView.setNoMore(true);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    //没有更多了
                    mViewBinding.mRecyclerView.setNoMore(true);
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                mViewBinding.mRecyclerView.refreshComplete();//刷新成功
                mViewBinding.mRecyclerView.loadMoreComplete();//加载成功
            }
        });
    }

}
