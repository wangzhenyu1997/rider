package com.lingmiao.distribution.ui.activity;

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
import com.lingmiao.distribution.bean.BillRecordBean;
import com.lingmiao.distribution.bean.BillRecordDataParam;
import com.lingmiao.distribution.bean.RequestJson;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityOrderFeeListBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.ArrayList;

/**
 * 订单费用列表
 * niuxinyu
 */
public class OrderFeeListActivity extends ActivitySupport {

    private ActivityOrderFeeListBinding viewBinding;
    private CommonAdapter<BillRecordDataParam> mAdapter;
    private ArrayList<BillRecordDataParam> mListData = new ArrayList<>();//列表数据

    private int iStart = 1;                            //列表起始
    private int pageSize = 10;                        //列表条数

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityOrderFeeListBinding.inflate(LayoutInflater.from(this));
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
        }, true, "订单费用", 0, "");
        //列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.mRecyclerview.setLayoutManager(linearLayoutManager);
        viewBinding.mRecyclerview.setLoadingListener(new LoadingListener() {
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
        mAdapter = new CommonAdapter<BillRecordDataParam>(this, R.layout.item_order_fee, mListData) {
            @Override
            protected void convert(ViewHolder holder, final BillRecordDataParam data, int position) {
                holder.setText(R.id.iof_order_id, data.getBillNo());//订单编号
                holder.setText(R.id.iof_order_fee, "¥" + data.getTotalCost());//费用
                holder.setText(R.id.iof_order_type, data.getBillType());//类型
                holder.setText(R.id.iof_order_time, data.getCreateTime());//时间
                holder.setText(R.id.iof_order_status, data.getStatus());//状态
                //点击item
//                holder.setOnClickListener(R.id.iof_item, view -> {
////                    startActivity(new Order);
//                });
            }
        };
        viewBinding.mRecyclerview.setAdapter(mAdapter);
        viewBinding.mRecyclerview.refresh();
    }


    /**
     * 获取列表数据
     */
    private void getDataList() {
        RequestJson jsonData = new RequestJson();
        jsonData.setPageNum(iStart);
        jsonData.setPageSize(pageSize);
        OkHttpUtils.postAync(Constant.AppQueryPayableChargeList, new Gson().toJson(jsonData), new HttpCallback<BillRecordBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BillRecordBean response) {
                super.onSuccess(response);
                viewBinding.mRecyclerview.refreshComplete();
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

}