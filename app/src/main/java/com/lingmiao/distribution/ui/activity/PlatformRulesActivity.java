package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.google.gson.Gson;
import com.lingmiao.distribution.adapter.HelpCenterAdapter;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicListBean;
import com.lingmiao.distribution.bean.BasicListParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityPlatformRulesBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台规则
 * niuxinyu
 */
public class PlatformRulesActivity extends ActivitySupport {

    private ActivityPlatformRulesBinding viewBinding;
    private HelpCenterAdapter mAdapter;
    private List<BasicListParam> mListData = new ArrayList<>();//列表数据

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityPlatformRulesBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();
        getData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewBinding.topView.setData(new LayoutTopView.TopCallback() {
            @Override
            public void onTopBack() {
                finish();
            }
        }, true, "平台规则", 0, "");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.mRecyclerview.setLayoutManager(linearLayoutManager);
        viewBinding.mRecyclerview.setNestedScrollingEnabled(false);
        mAdapter = new HelpCenterAdapter(mListData, this);
        viewBinding.mRecyclerview.setAdapter(mAdapter);
    }

    /**
     * 获取列表数据
     */
    private void getData() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("type", "3");
        OkHttpUtils.postAync(Constant.AppQueryContentList, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    mListData.addAll(response.getData());
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }
}