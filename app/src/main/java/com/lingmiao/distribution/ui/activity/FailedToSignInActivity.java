package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.adapter.recycleviewAdapter.CommonAdapter;
import com.lingmiao.distribution.adapter.recycleviewAdapter.ViewHolder;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicBean;
import com.lingmiao.distribution.bean.BasicListBean;
import com.lingmiao.distribution.bean.BasicListParam;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityFailedToSignInBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 取货失败
 * niuxinyu
 */
public class FailedToSignInActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityFailedToSignInBinding viewBinding;
    private CommonAdapter<BasicListParam> mAdapter;
    private ArrayList<BasicListParam> mListData = new ArrayList<>();//列表数据

    private String mId;     //拒绝主键

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityFailedToSignInBinding.inflate(LayoutInflater.from(this));
        mId = getIntent().getStringExtra("id");
        setContentView(viewBinding.getRoot());
        initView();
        getReason();
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
        }, true, "取货失败", 0, "");

        viewBinding.abcSubmit.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.mRefuseRecyclerview.setLayoutManager(linearLayoutManager);
        mAdapter = new CommonAdapter<BasicListParam>(this, R.layout.item_rejection_ac, mListData) {
            @Override
            protected void convert(ViewHolder holder, final BasicListParam s, int position) {
                if (position == mListData.size() - 1) {
                    holder.setVisible(R.id.bottom_view, false);
                } else {
                    holder.setVisible(R.id.bottom_view, true);
                }
                holder.setText(R.id.m_content, s.getLabel());
                holder.setChecked(R.id.m_content, s.isSelectState());
                holder.setOnClickListener(R.id.m_content, view -> {
                    if (s.isSelectState()) {
                        mListData.get(position).setSelectState(false);
                    } else {
                        for (BasicListParam param : mListData) {
                            param.setSelectState(false);
                        }
                        mListData.get(position).setSelectState(true);
                    }
                    mAdapter.notifyDataSetChanged();
                });
            }
        };
        viewBinding.mRefuseRecyclerview.setAdapter(mAdapter);
    }

    /**
     * 查询可选失败原因
     */
    private void getReason() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("type", "pickup_fail_type");
        OkHttpUtils.postAync(Constant.AppGetListByType, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if (response.getData() != null && response.getData().size() != 0) {
                        mListData.addAll(response.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 提交
     */
    private void submit() {
        String mRefuseAcceptType = "";
        String mRefuseAcceptTypeName = "";
        for (BasicListParam param : mListData) {
            if (param.isSelectState()) {
                mRefuseAcceptType = param.getValue();
                mRefuseAcceptTypeName = param.getLabel();
            }
        }
        if (mRefuseAcceptType.isEmpty()) {
            ToastUtil.showToast(context, "请选择失败原因！");
            return;
        }
        if (mRefuseAcceptTypeName != null && mRefuseAcceptTypeName.equals("其他")) {
            if (!InputUtil.isEmpty(context, viewBinding.mContent.getText().toString(), "请输入详细内容！"))
                return;
        }
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("dispatchId", mId);
        mMap.put("pickupFailType", mRefuseAcceptType);
        mMap.put("explainReason", viewBinding.mContent.getText().toString());
        mMap.put("address", PublicUtil.isNull(Constant.LOCATIONADDRESS));
        mMap.put("latitude", Constant.LOCATIONLATITUDE);
        mMap.put("longitude", Constant.LOCATIONLONGITUDE);
        OkHttpUtils.postAync(Constant.AppPickFail, new Gson().toJson(mMap), new HttpCallback<BasicBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    EventBus.getDefault().post(new PublicBean(10));
                    EventBus.getDefault().post(new PublicBean(11));
                    EventBus.getDefault().post(new PublicBean(12));
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.abc_submit) {       //提交接口
            submit();
        }
    }
}