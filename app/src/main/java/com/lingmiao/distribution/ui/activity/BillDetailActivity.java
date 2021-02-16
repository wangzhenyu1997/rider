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
import com.lingmiao.distribution.bean.BillDetailDataBean;
import com.lingmiao.distribution.bean.BillItemDataParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityBillDetailBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 账单详情
 * niuxinyu
 */
public class BillDetailActivity extends ActivitySupport {

    private ActivityBillDetailBinding viewBinding;
    private CommonAdapter<BillItemDataParam> mAdapter;
    private ArrayList<BillItemDataParam> mListData = new ArrayList<>();//列表数据

    private String billId="";

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityBillDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        billId=getIntent().getStringExtra("id");
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
//            @Override
//            public void onRightTx() {//确认账单
//                startActivity(new Intent(context, BillConfirmActivity.class));
//            }


        }, true, "账单详情", 0, "");
        viewBinding.mScrollView.setPullRefreshEnabled(false);//关闭下拉刷新
        viewBinding.mScrollView.setLoadingMoreEnabled(true);
        viewBinding.mScrollView.setLoadingListener(new LoadingListener() {
            @Override
            public void onLoadMore() {
                getData();
                viewBinding.mScrollView.loadMoreComplete();
            }
        });

        //列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.mRecyclerView.setLayoutManager(linearLayoutManager);
        viewBinding.mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new CommonAdapter<BillItemDataParam>(this, R.layout.item_order_fee, mListData) {
            @Override
            protected void convert(ViewHolder holder, final BillItemDataParam data, int position) {
                holder.setText(R.id.iof_order_id, data.getItemBillNo());//帐单编号
                holder.setText(R.id.iof_order_fee,  data.getAmount());//账单金额
                holder.setText(R.id.iof_order_type, data.getItemTypeName());//账单状态
//                holder.setText(R.id.iof_order_status, data.get);//账单状态
                holder.setText(R.id.iof_order_time, ""+data.getOccurTime());//账单时间
                //点击item
                holder.setOnClickListener(R.id.iof_item, view -> {
                });
            }
        };
        viewBinding.mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 获取列表数据
     */
    private void getData() {
        Map<String,String> mMap=new HashMap<>();
        mMap.put("id",billId);
        OkHttpUtils.postAync(Constant.AppQuerySettleBillById, new Gson().toJson(mMap), new HttpCallback<BillDetailDataBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BillDetailDataBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    viewBinding.abdBillNumber.setText(response.getData().getSettleBillNo());
                    viewBinding.abdBillMonth.setText(response.getData().getCreateTime());
                    viewBinding.abdBillMoney.setText(response.getData().getAmount());
//                    viewBinding.abdBillRemark.setText();
//                    viewBinding.abdBillReMoney.setText("");
                    String statusStr="";// 1待确认 2待结算 3已结算
                    if(response.getData().getStatus()==1){
                        statusStr="待确认";
                    }else if(response.getData().getStatus()==2){
                        statusStr="待结算";
                    }else{
                        statusStr="已结算";
                    }
                    viewBinding.abdBillStatus.setText(statusStr);
                    viewBinding.adbTotal.setText("（"+response.getData().getSettleBillItemList()+"单，共计"+response.getData().getAmount()+"元）");
                    if (response.getData().getSettleBillItemList() != null) {
                        mListData.addAll(response.getData().getSettleBillItemList());
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }
}