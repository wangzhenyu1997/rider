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
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.bean.RequestBody;
import com.lingmiao.distribution.bean.RequestJson;
import com.lingmiao.distribution.bean.TradeRecordBean;
import com.lingmiao.distribution.bean.TradeRecordDataParam;
import com.lingmiao.distribution.bean.WalletBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityMyDepositBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 押金
 * niuxinyu
 */
public class MyDepositActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityMyDepositBinding viewBinding;
    private CommonAdapter<TradeRecordDataParam> mAdapter;
    private ArrayList<TradeRecordDataParam> mListData = new ArrayList<>();//列表数据
    private String dataId = "";
    private int iStart = 1;                            //列表起始
    private int pageSize = 10;                        //列表条数

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityMyDepositBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        EventBus.getDefault().register(this);
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
                super.onTopBack();
                finish();
            }
        }, true, "押金", 0, "");
        //列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.mRecyclerview.setLayoutManager(linearLayoutManager);
        viewBinding.mRecyclerview.setPullRefreshEnabled(false);
        viewBinding.mRecyclerview.setLoadingListener(new LoadingListener() {
            @Override
            public void onLoadMore() {
                getDataList();
            }
        });
        mAdapter = new CommonAdapter<TradeRecordDataParam>(this, R.layout.item_balance, mListData) {
            @Override
            protected void convert(ViewHolder holder, final TradeRecordDataParam data, int position) {
                holder.setText(R.id.ib_type, data.getTradeContent());//交易类型
                holder.setText(R.id.ib_time, data.getTradeTime());//交易时间
                if (data.getFundFlowType() == 10) {//fundFlowType;//资金流向 10收 20支
                    holder.setTextColor(R.id.ib_money, getResources().getColor(R.color.colorPrimary));
                    holder.setText(R.id.ib_money, "+" + data.getAmount());//交易金额
                } else {
                    holder.setTextColor(R.id.ib_money, getResources().getColor(R.color.colorPrice));
                    holder.setText(R.id.ib_money, "-" + data.getAmount());//交易金额
                }
            }
        };
        viewBinding.mRecyclerview.setAdapter(mAdapter);
        viewBinding.amdRecharge.setOnClickListener(this);
    }

    /**
     * 收到刷新消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(PublicBean event) {
        if (event.getCode() == 15) {
            iStart = 1;
            mListData.clear();
            mAdapter.notifyDataSetChanged();
            getData();
        }
    }

    /**
     * 获取列表数据
     */
    private void getData() {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppDepositIndex, new Gson().toJson(mMap), new HttpCallback<WalletBean>() {
            @Override
            public void onSuccess(WalletBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    viewBinding.amdDeposit.setText(response.getData().getDepositAccount().getBalanceAmount());
                    dataId = response.getData().getDepositAccount().getId();
                    getDataList();
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }


    /**
     * 获取列表数据
     */
    private void getDataList() {
        RequestJson jsonData = new RequestJson();
        jsonData.setPageNum(iStart);
        jsonData.setPageSize(pageSize);
        RequestBody body = new RequestBody();
        body.setAccountId(dataId);
        jsonData.setBody(body);
        OkHttpUtils.postAync(Constant.AppQueryTradeRecordList, new Gson().toJson(jsonData), new HttpCallback<TradeRecordBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(TradeRecordBean response) {
                super.onSuccess(response);
                viewBinding.mRecyclerview.loadMoreComplete();
                if (response.getData() != null && response.getData().getTotalCount() == 0) {
                    viewBinding.mRecyclerview.setFootViewText("", "暂无数据");
                }else{
                    viewBinding.mRecyclerview.setFootViewText("", "没有更多了");
                }
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if (response.getData().getRecords() != null) {
                        mListData.addAll(response.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
                    }
                    if (response.getData().getTotalCount() <= mListData.size()) {
                        viewBinding.mRecyclerview.setNoMore(true);
                    } else {
                        ++iStart;
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
            case R.id.amd_recharge:
                startActivity(new Intent(context, RechargeActivity.class).putExtra("dataId", dataId));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}