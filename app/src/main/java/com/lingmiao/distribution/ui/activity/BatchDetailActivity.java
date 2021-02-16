package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.nrecyclerview.interfaces.LoadingListener;
import com.google.gson.Gson;
import com.lingmiao.distribution.adapter.BatchDetailAdapter;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.base.bean.DataVO;
import com.lingmiao.distribution.bean.HomeParam;
import com.lingmiao.distribution.bean.HomeTwoParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityBatchDetailBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchDetailActivity extends ActivitySupport {

    private ActivityBatchDetailBinding mViewBinding;

    private BatchDetailAdapter mAdapter;

    private List<HomeTwoParam> mListData;

    private int mPage = 1;

    private String id;
    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        mViewBinding = ActivityBatchDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(mViewBinding.getRoot());

        id = getIntent().getStringExtra("id");
//        EventBus.getDefault().register(this);

        initData();

        initTitle();

        initRecycleView();
    }

    private void initData() {
        if(mListData == null) {
            mListData = new ArrayList<>();
        } else {
            mListData.clear();
        }
        mPage = 1;
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

        mAdapter = new BatchDetailAdapter(mListData, 0, new BatchDetailAdapter.OnHomeListener() {
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
//        //刷新成功
//        mViewBinding.mRecyclerView.refreshComplete();
//        //加载成功
//        mViewBinding.mRecyclerView.loadMoreComplete();
//        // 没有更多了
//        mViewBinding.mRecyclerView.setNoMore(true);
//        mViewBinding.mRecyclerView.setFootViewText("", "没有更多了");
//        mListData.add(new HomeParam());
//        mAdapter.notifyDataSetChanged();
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("id", id);
        OkHttpUtils.postAync(Constant.AppDispatchDetail, new Gson().toJson(mMap), new HttpCallback<DataVO<HomeParam>>(this, getProgressDialog()) {
            @Override
            public void onSuccess(DataVO<HomeParam> response) {
                super.onSuccess(response);
                mViewBinding.mRecyclerView.refreshComplete();//刷新成功
                mViewBinding.mRecyclerView.loadMoreComplete();//加载成功
                if (response.getData().getOrderList() != null && response.getData().getOrderList().size() == 0) {
                    mViewBinding.mRecyclerView.setFootViewText("", "暂无数据");
                }else{
                    mViewBinding.mRecyclerView.setFootViewText("", "没有更多了");
                }
                if (response.isSuccessAndData() && response.getData().getOrderList() != null && response.getData().getOrderList().size() != 0) {
                    mListData.addAll(response.getData().getOrderList());
                    mAdapter.notifyDataSetChanged();
                }
                mViewBinding.mRecyclerView.setNoMore(true);
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                mViewBinding.mRecyclerView.refreshComplete();//刷新成功
                mViewBinding.mRecyclerView.loadMoreComplete();//加载成功
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

}
