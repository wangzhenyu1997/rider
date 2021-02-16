package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.adapter.HelpCenterAdapter;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicListBean;
import com.lingmiao.distribution.bean.BasicListParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityHelpCenterBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帮助中心
 * niuxinyu
 */
public class HelpCenterActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityHelpCenterBinding viewBinding;
    private HelpCenterAdapter mAdapter;
    private List<BasicListParam> mListData = new ArrayList<>();//列表数据
    private String tel;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityHelpCenterBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();
        getData();
        getTel();
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
        }, true, "帮助中心", 0, "");
        viewBinding.mPhone.setOnClickListener(this);
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
        mMap.put("type", "2");
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

    /**
     * 获取服务热线
     */
    private void getTel() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("type", "6");
        OkHttpUtils.postAync(Constant.AppQueryContentList, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().size() != 0) {
                    String telStr = response.getData().get(0).getContent();
                    tel = telStr.split("：")[1];
                    viewBinding.ahpTel.setText(telStr);
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_phone:      //拨打电话
                if(tel == null){
                    ToastUtil.showToast(context,"暂无服务热线！");
                    return;
                }
                PublicUtil.callPhone(tel, this);
                break;
        }

    }
}