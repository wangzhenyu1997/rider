package com.lingmiao.distribution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.nrecyclerview.interfaces.LoadingListener;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.adapter.ScanListAdapter;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.app.MyApplication;
import com.lingmiao.distribution.bean.HomeTwoParam;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.bean.RespResult;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityScanOrderBinding;
import com.lingmiao.distribution.dialog.HomeConfirmDialog;
import com.lingmiao.distribution.event.ScanList;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ScanOrderActivity extends ActivitySupport {

    private ActivityScanOrderBinding mViewBinding;

    private ScanListAdapter mAdapter;

    private List<HomeTwoParam> mListData;

    private HashSet<String> mOrderIdsList;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        mViewBinding = ActivityScanOrderBinding.inflate(LayoutInflater.from(this));
        setContentView(mViewBinding.getRoot());

        EventBus.getDefault().register(this);

        initData();

        initTitle();

        initRecycleView();

        setListener();

        updateRecycleView();

        setOrderCount();

    }

    private void initData() {
        mListData = new ArrayList<>();

        mOrderIdsList = new HashSet<>();
    }

    private void initTitle() {
        mViewBinding.topView.setData(new LayoutTopView.TopCallback() {
            @Override
            public void onTopBack() {
                if(mListData == null || mListData.size() == 0) {
                    super.onTopBack();
                    finish();
                } else {
                    HomeConfirmDialog dialog = new HomeConfirmDialog(MyApplication.DHActivityManager.getManager().getTopActivity(), value -> {
                        if (value) {
                            super.onTopBack();
                            finish();
                        }
                    }, "返回提示", "返回首页已扫描订单将会被清空\n确认要返回吗？", "取消", "确定");
                    dialog.show();
                }
            }

            @Override
            public void onRightImage() {
                Intent intent = new Intent(ScanOrderActivity.this, ScanActivity.class);
                intent.putExtra("orderNosString", getOrderNoString());
                startActivity(intent);
            }

            @Override
            public void onRightTx() {
                Intent intent = new Intent(ScanOrderActivity.this, ScanActivity.class);
                intent.putExtra("orderNosString", getOrderNoString());
                startActivity(intent);
            }
        }, true, "扫码取货", R.mipmap.scan_code, "");
    }

    protected String getOrderNoString() {
        StringBuffer buffer = new StringBuffer();
        for(String item : mOrderIdsList) {
            buffer.append(item).append(",");
        }
        return buffer.toString();
    }

    private void initRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mViewBinding.mRecyclerView.setLayoutManager(linearLayoutManager);
        mViewBinding.mRecyclerView.setLoadingListener(new LoadingListener() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefresh() {
                super.onRefresh();
                updateRecycleView();
            }
        });

        mAdapter = new ScanListAdapter(mListData, this, position -> {
            if(mListData == null || position > mListData.size() || mListData.get(position) == null) {
                return;
            }
            mOrderIdsList.remove(mListData.get(position).upsBillNo);
            mListData.remove(position);
            mAdapter.notifyDataSetChanged();
            setOrderCount();
        });

        mViewBinding.mRecyclerView.setAdapter(mAdapter);

        mViewBinding.mRecyclerView.refresh();

    }

    private void setListener() {
        mViewBinding.tvSaveOrder.setOnClickListener(v ->{
            HomeConfirmDialog dialog = new HomeConfirmDialog(MyApplication.DHActivityManager.getManager().getTopActivity(), value -> {
                if (value) {
                    accept();
                }
            }, "接单提示", "该操作将直接派单给您\n确认要保存吗？", "取消", "确认接单");
            dialog.show();
        });
        mViewBinding.tvTakeSuccess.setOnClickListener(v -> {
            HomeConfirmDialog dialog = new HomeConfirmDialog(MyApplication.DHActivityManager.getManager().getTopActivity(), value -> {
                if (value) {
                    pickup();
                }
            }, "取货提示", "该操作将直接派单给您并取货成功\n确认要取货吗？", "取消", "确认取货");
            dialog.show();
        });
        mViewBinding.tvClearOrder.setOnClickListener(v -> {
            HomeConfirmDialog dialog = new HomeConfirmDialog(MyApplication.DHActivityManager.getManager().getTopActivity(), value -> {
                if (value) {
                    clearList();
                }
            }, "清空提示", "清空后已扫描的订单将全部删除\n确认要清空吗？", "取消", "确认");
            dialog.show();
        });

    }

    private void clearList() {
        mListData.clear();
        mOrderIdsList.clear();
        mAdapter.notifyDataSetChanged();
        setOrderCount();
    }

    private void setOrderCount() {
        int scanCount = mListData.size();
        int orderCount = mListData.size();
        mViewBinding.tvScanOrderCount.setText(String.format("已扫描：%s单", scanCount));
        mViewBinding.tvUnTakeOrderCount.setText(String.format("待派单：%s单", orderCount));

        mViewBinding.tvSaveOrder.setEnabled(scanCount > 0);
        mViewBinding.tvTakeSuccess.setEnabled(scanCount > 0);
    }

    private void updateRecycleView() {
        mViewBinding.mRecyclerView.refreshComplete();
        mViewBinding.mRecyclerView.loadMoreComplete();
        mViewBinding.mRecyclerView.setNoMore(true);
        mViewBinding.mRecyclerView.setFootViewText("", "没有更多了");
    }

    private void pickup() {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("ids", getIds());
        OkHttpUtils.postAync(Constant.AssignAndPickup, new Gson().toJson(mMap), new HttpCallback<RespResult>(ScanOrderActivity.this, getProgressDialog()) {
            @Override
            public void onSuccess(RespResult response) {
                super.onSuccess(response);

                if(response != null && response.isSuccess()) {
                    ToastUtil.showToast(ScanOrderActivity.this, response == null || response.getMessage() == null ? "取货成功" : response.getMessage());
                    EventBus.getDefault().post(new PublicBean(21));
                    finish();
                } else {
                    ToastUtil.showToast(ScanOrderActivity.this, response == null || response.getMessage() == null ? "操作失败" : response.getMessage());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showToast(ScanOrderActivity.this, "操作失败");
            }
        });
    }

    private void accept() {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("ids", getIds());
        OkHttpUtils.postAync(Constant.AssignAndAcecptOrder, new Gson().toJson(mMap), new HttpCallback<RespResult>(ScanOrderActivity.this, getProgressDialog()) {
            @Override
            public void onSuccess(RespResult response) {
                super.onSuccess(response);
                if(response != null && response.isSuccess()) {
                    ToastUtil.showToast(ScanOrderActivity.this, response == null || response.getMessage() == null ? "接单成功" : response.getMessage());
                    EventBus.getDefault().post(new PublicBean(20));
                    finish();
                } else {
                    ToastUtil.showToast(ScanOrderActivity.this, response == null || response.getMessage() == null ? "操作失败" : response.getMessage());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showToast(ScanOrderActivity.this, message == null || message.length() == 0 ? "操作失败" : message);
            }
        });
    }

    private List<String> getIds() {
        List<String> list = new ArrayList<>();
        for(HomeTwoParam item : mListData) {
            list.add(item.getId());
        }
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scanFinish(ScanList event) {
        if(event == null || event.getList() == null) {
            return;
        }
        for(HomeTwoParam item : event.getList()) {
            if(item == null || item.upsBillNo == null || item.upsBillNo.length() == 0) {
                continue;
            }
            if(!mOrderIdsList.contains(item.upsBillNo)) {
                mListData.add(item);
                mOrderIdsList.add(item.upsBillNo);
            }
        }
        mAdapter.notifyDataSetChanged();
        setOrderCount();
    }

}
