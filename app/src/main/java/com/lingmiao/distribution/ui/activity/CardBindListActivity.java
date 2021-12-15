package com.lingmiao.distribution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.github.nrecyclerview.interfaces.LoadingListener;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.adapter.recycleviewAdapter.CommonAdapter;
import com.lingmiao.distribution.adapter.recycleviewAdapter.ViewHolder;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.CardListBean;
import com.lingmiao.distribution.bean.CardListParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityCardBindListBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 绑定银行卡列表
 * niuxinyu
 */
public class CardBindListActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityCardBindListBinding viewBinding;
    private CommonAdapter<CardListParam> mAdapter;
    private ArrayList<CardListParam> mListData = new ArrayList<>();//列表数据

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityCardBindListBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();
        initData();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewBinding.topView.setData(new LayoutTopView.TopCallback() {
            @Override
            public void onTopBack() {
                super.onTopBack();
                finish();
            }
        }, true, "银行卡", 0, "");

        viewBinding.mSubmit.setOnClickListener(this);
        //列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.mRecyclerview.setLayoutManager(linearLayoutManager);
        viewBinding.mRecyclerview.setLoadingMoreEnabled(false);
        viewBinding.mRecyclerview.setLoadingListener(new LoadingListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                mListData.clear();
                mAdapter.notifyDataSetChanged();
                getData();
            }
        });
        mAdapter = new CommonAdapter<CardListParam>(this, R.layout.item_bank_card, mListData) {
            @Override
            protected void convert(ViewHolder holder, final CardListParam data, int position) {
                holder.setText(R.id.ibc_bank_code, "账号：" + data.getCardNo());//银行卡号
                holder.setText(R.id.ibc_bank_account, "户名：" + data.getOpenAccountName());//开户名
                holder.setText(R.id.ibc_bank_name, data.getBankName());//银行名称
                if (data.getBankCardType() == 0) {
                    holder.setText(R.id.ibc_account_type, "个人账户");
                } else {
                    holder.setText(R.id.ibc_account_type, "企业账户");
                }
                //点击item
                holder.setOnClickListener(R.id.ibc_un_bind, view -> {
                    startActivityForResult(new Intent(context, CardUnBindActivity.class).putExtra("cardId", data.getId()), 200);
                });
            }
        };
        viewBinding.mRecyclerview.setAdapter(mAdapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mListData.clear();
        mAdapter.notifyDataSetChanged();
        getData();
    }


    /**
     * 获取列表数据
     */
    private void getData() {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppQueryBankCardList, new Gson().toJson(mMap), new HttpCallback<CardListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(CardListBean response) {
                super.onSuccess(response);
                viewBinding.mRecyclerview.refreshComplete();
                viewBinding.mRecyclerview.loadMoreComplete();
                if (response.getCode().equals(Constant.SUCCESS)) {
                    mListData.addAll(response.getData());
                    mAdapter.notifyDataSetChanged();
                    if(mListData.size()==0){
                        viewBinding.mSubmit.setVisibility(View.VISIBLE);
                    }else{
                        viewBinding.mSubmit.setVisibility(View.GONE);
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_submit:             //添加新卡
                startActivityForResult(new Intent(context, PayTypeBankActivity.class), 200);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            initData();
        }

    }
}