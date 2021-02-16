package com.lingmiao.distribution.ui.activity;


import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;

import com.google.gson.Gson;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicListBean;
import com.lingmiao.distribution.bean.BasicListParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityAgreementBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.HashMap;
import java.util.Map;

/**
 * AgreementActivity
 *
 * @author yandaocheng <br/>
 * 服务条款
 * 2020-07-11
 * 修改者，修改日期，修改内容
 */
public class AgreementActivity extends ActivitySupport {

    private ActivityAgreementBinding viewBinding;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityAgreementBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
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
                finish();
            }
        }, true, "骑手服务条款", 0, null);
    }

    /**
     * 获取数据
     */
    private void getData() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("type", "4");
        OkHttpUtils.postAync(Constant.AppQueryContentList, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if(response.getData()!=null&&response.getData().size()!=0){
                        BasicListParam contentOnj=response.getData().get(0);
                        viewBinding.aaContext.setText(contentOnj.getContent());
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

}
