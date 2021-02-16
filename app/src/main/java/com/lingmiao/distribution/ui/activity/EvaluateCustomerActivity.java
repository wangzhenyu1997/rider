package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicBean;
import com.lingmiao.distribution.bean.BasicListBean;
import com.lingmiao.distribution.bean.BasicListParam;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityEvaluateCustomerBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * EvaluateCustomerActivity
 *
 * @author yandaocheng <br/>
 * 评价客户
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class EvaluateCustomerActivity extends ActivitySupport {

    private ActivityEvaluateCustomerBinding viewBinding;
    private String mId;     //主键

    private int customerScore = 0;          //发货人（客户）
    private int consigneeScore = 0;         //收货人

    private List<BasicListParam> mListData = new ArrayList<>(); //发货人标签
    private List<BasicListParam> mListTwoData = new ArrayList<>();

    private List<String> mGetData = new ArrayList<>();      //发货人总标签
    private List<String> mSendData = new ArrayList<>();

    private Set<Integer> mGetSelct = new HashSet<>();   //发货人选择标签
    private Set<Integer> mSendSelct = new HashSet<>();   //收货人选择标签


    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityEvaluateCustomerBinding.inflate(LayoutInflater.from(this));
        mId = getIntent().getStringExtra("id");
        setContentView(viewBinding.getRoot());
        initView();
        getTagOne();
        getTagTwo();
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
        }, true, "评价客户", 0, null);
        viewBinding.mGetStar.setOnRatingChangedListener((current, count) -> customerScore = current);
        viewBinding.mSendStar.setOnRatingChangedListener((current, count) -> consigneeScore = current);

        //发货人
        viewBinding.mGetFlowlayout.setOnSelectListener(selectPosSet -> {
            mGetSelct.clear();
            mGetSelct.addAll(selectPosSet);
        });
        //收货人
        viewBinding.mSendFlowlayout.setOnSelectListener(selectPosSet -> {
            mSendSelct.clear();
            mSendSelct.addAll(selectPosSet);
        });

        viewBinding.mSubmit.setOnClickListener(v -> submit());
    }

    /**
     * 查询标签
     */
    private void getTagOne() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("type", "customer_label_type");
        OkHttpUtils.postAync(Constant.AppGetListByType, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if (response.getData() != null && response.getData().size() != 0) {
                        mListData.addAll(response.getData());
                        for (BasicListParam param : mListData) {
                            mGetData.add(param.getLabel());
                        }
                        viewBinding.mGetFlowlayout.setAdapter(new TagAdapter<String>(mGetData) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tv, viewBinding.mGetFlowlayout, false);
                                tv.setText(s);
                                return tv;
                            }
                        });
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 查询标签
     */
    private void getTagTwo() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("type", "consignee_label_type");
        OkHttpUtils.postAync(Constant.AppGetListByType, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if (response.getData() != null && response.getData().size() != 0) {
                        mListTwoData.addAll(response.getData());
                        for (BasicListParam param : mListTwoData) {
                            mSendData.add(param.getLabel());
                        }
                        viewBinding.mSendFlowlayout.setAdapter(new TagAdapter<String>(mSendData) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tv, viewBinding.mSendFlowlayout, false);
                                tv.setText(s);
                                return tv;
                            }
                        });
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
        if (customerScore == 0) {
            ToastUtil.showToast(context, "请选择发货人星级！");
            return;
        }
//        if (mGetSelct.size() == 0) {
//            ToastUtil.showToast(context, "请选择发货人标签！");
//            return;
//        }
        if (!InputUtil.isEmpty(context, viewBinding.mGetEdit.getText().toString(), "请输入发货人评价！"))
            return;
        if (consigneeScore == 0) {
            ToastUtil.showToast(context, "请选择收货人星级！");
            return;
        }
//        if (mSendSelct.size() == 0) {
//            ToastUtil.showToast(context, "请选择收货人标签！");
//            return;
//        }
        if (!InputUtil.isEmpty(context, viewBinding.mSendEdit.getText().toString(), "请输入收货人评价！"))
            return;
        StringBuilder customerLabels = new StringBuilder();
        for (Integer value : mGetSelct) {
            if (customerLabels.toString().equals("")) {
                customerLabels = new StringBuilder(mListData.get(value).getLabel());
            } else {
                customerLabels.append(",").append(mListData.get(value).getLabel());
            }
        }
        StringBuilder consigneeLabels = new StringBuilder();
        for (Integer value : mSendSelct) {
            if (consigneeLabels.toString().equals("")) {
                consigneeLabels = new StringBuilder(mListTwoData.get(value).getLabel());
            } else {
                consigneeLabels.append(",").append(mListTwoData.get(value).getLabel());
            }
        }
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("orderId", mId);
        mMap.put("customerScore", customerScore);
        mMap.put("customerComment", viewBinding.mGetEdit.getText().toString());
        mMap.put("customerLabels", customerLabels.toString()); //标签
        mMap.put("consigneeScore", consigneeScore);
        mMap.put("consigneeComment", viewBinding.mSendEdit.getText().toString());
        mMap.put("consigneeLabels", consigneeLabels.toString());//标签
        OkHttpUtils.postAync(Constant.AppAddComment, new Gson().toJson(mMap), new HttpCallback<BasicBean>(context, getProgressDialog()) {
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

}