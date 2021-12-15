package com.lingmiao.distribution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.github.nrecyclerview.interfaces.LoadingListener;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.adapter.recycleviewAdapter.CommonAdapter;
import com.lingmiao.distribution.adapter.recycleviewAdapter.ViewHolder;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BillBean;
import com.lingmiao.distribution.bean.BillDataParam;
import com.lingmiao.distribution.bean.RequestJson;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityBillListBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.ArrayList;

/**
 * 账单列表
 * niuxinyu
 */
public class BillListActivity extends ActivitySupport {

    private ActivityBillListBinding viewBinding;
    private CommonAdapter<BillDataParam> mAdapter;
    private ArrayList<BillDataParam> mListData = new ArrayList<>();//列表数据

    private int iStart = 1;                            //列表起始
    private int pageSize = 10;                        //列表条数

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityBillListBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();
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
        }, true, "账单列表", 0, "");
        //列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.mRecyclerView.setLayoutManager(linearLayoutManager);
        viewBinding.mRecyclerView.setLoadingListener(new LoadingListener() {
            @Override
            public void onLoadMore() {
                getDataList();
            }

            @Override
            public void onRefresh() {
                super.onRefresh();
                iStart = 1;
                mListData.clear();
                mAdapter.notifyDataSetChanged();
                getDataList();
            }
        });
        mAdapter = new CommonAdapter<BillDataParam>(this, R.layout.item_bill, mListData) {
            @Override
            protected void convert(ViewHolder holder, final BillDataParam data, int position) {
                holder.setText(R.id.ib_bill_code, data.getSettleBillNo());//帐单编号
                holder.setText(R.id.ib_bill_num, "X"+data.getBillNum()+"单");//单数
                holder.setText(R.id.ib_bill_time,  data.getCreateTime());//账单时间
                String statusStr="";// 1待确认 2待结算 3已结算
                if(data.getStatus()==1){
                    statusStr="待确认";
                }else if(data.getStatus()==2){
                    statusStr="待结算";
                }else{
                    statusStr="已结算";
                }
                holder.setText(R.id.ib_bill_status, statusStr);//账单状态
                holder.setText(R.id.ib_bill_money, data.getAmount());//账单金额
                holder.setText(R.id.ib_bill_name, ""+data.getSettleBillName());//账单时间
                //点击item
                holder.setOnClickListener(R.id.ib_item, view -> {
                    startActivity(new Intent(context, BillDetailActivity.class).putExtra("id",data.getId()));
                });
            }
        };
        viewBinding.mRecyclerView.setAdapter(mAdapter);
        viewBinding.mRecyclerView.refresh();
    }


    /**
     * 获取列表数据
     */
    private void getDataList() {
        RequestJson jsonData = new RequestJson();
        jsonData.setPageNum(iStart);
        jsonData.setPageSize(pageSize);
        OkHttpUtils.postAync(Constant.AppQuerySettleBillList, new Gson().toJson(jsonData), new HttpCallback<BillBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BillBean response) {
                super.onSuccess(response);
                viewBinding.mRecyclerView.refreshComplete();
                viewBinding.mRecyclerView.loadMoreComplete();
                if (response.getData() != null && response.getData().getTotalCount() == 0) {
                    viewBinding.mRecyclerView.setFootViewText("", "暂无数据");
                }else{
                    viewBinding.mRecyclerView.setFootViewText("", "没有更多了");
                }
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if (response.getData().getRecords() != null) {
                        mListData.addAll(response.getData().getRecords());
                        mAdapter.notifyDataSetChanged();
                    }
                    if (response.getData().getTotalCount() <= mListData.size()) {
                        viewBinding.mRecyclerView.setNoMore(true);
                    } else {
                        ++iStart;
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

}