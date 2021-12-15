package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.github.nrecyclerview.interfaces.LoadingListener;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.adapter.recycleviewAdapter.CommonAdapter;
import com.lingmiao.distribution.adapter.recycleviewAdapter.ViewHolder;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.ComplaintBean;
import com.lingmiao.distribution.bean.ComplaintParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityMessageListBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * MessageListActivity
 *
 * @author yandaocheng <br/>
 * 消息列表
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class MessageListActivity extends ActivitySupport {


    private CommonAdapter<ComplaintParam> mAdapter;
    private List<ComplaintParam> mData = new ArrayList<>();
    private ActivityMessageListBinding viewBinding;

    private int mPage = 1;

    @Override
    protected void setCreateView(Bundle savedInstanceState) {
        viewBinding = ActivityMessageListBinding.inflate(LayoutInflater.from(this));
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
                finish();
            }
        }, true, "消息中心", 0, null);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.mRecyclerView.setLayoutManager(layoutManager);
        viewBinding.mRecyclerView.setLoadingListener(new LoadingListener() {
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
        mAdapter = new CommonAdapter<ComplaintParam>(context, R.layout.activity_message_list_item, mData) {
            @Override
            protected void convert(ViewHolder holder, final ComplaintParam s, int position) {
                holder.setText(R.id.m_tittle, s.title);
                holder.setText(R.id.m_content, s.getContent());
                holder.setText(R.id.m_time, s.getCreateTime());
            }
        };
        viewBinding.mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        mData.clear();
        mAdapter.notifyDataSetChanged();
        mPage = 1;
        viewBinding.mRecyclerView.setNoMore(false);
        getData();
    }

    private void getData() {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("pageNum", String.valueOf(mPage));
        mMap.put("pageSize", "10");
//        Map<String, Object> mMapState = new HashMap<>();
//        mMap.put("body", mMapState);
        OkHttpUtils.postAync(Constant.AppMsgList, new Gson().toJson(mMap), new HttpCallback<ComplaintBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(ComplaintBean response) {
                super.onSuccess(response);
                viewBinding.mRecyclerView.refreshComplete();//刷新成功
                viewBinding.mRecyclerView.loadMoreComplete();//加载成功
                if (response.getData() != null && response.getData().getTotalCount() == 0) {
                    viewBinding.mRecyclerView.setFootViewText("", "暂无数据");
                }else{
                    viewBinding.mRecyclerView.setFootViewText("", "没有更多了");
                }
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().getRecords() != null && response.getData().getRecords().size() != 0) {
                    mData.addAll(response.getData().getRecords());
                    if (mData.size() == response.getData().getTotalCount()) {
                        viewBinding.mRecyclerView.setNoMore(true);//没有更多了
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    viewBinding.mRecyclerView.setNoMore(true);//没有更多了
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                viewBinding.mRecyclerView.refreshComplete();//刷新成功
                viewBinding.mRecyclerView.loadMoreComplete();//加载成功
            }
        });
    }
}
