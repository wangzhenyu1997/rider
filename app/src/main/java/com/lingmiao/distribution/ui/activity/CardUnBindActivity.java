package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicBean;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityCardUnbindBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.TimeCountUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.HashMap;
import java.util.Map;

/**
 * 银行卡解绑
 * niuxinyu
 */
public class CardUnBindActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityCardUnbindBinding viewBinding;
    private String cardId;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityCardUnbindBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        cardId = getIntent().getStringExtra("cardId");
        initView();
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
        }, true, "解除绑定", 0, "");
        viewBinding.mSubmit.setOnClickListener(this);
        viewBinding.acuGetCode.setOnClickListener(this);
    }


    /**
     * 获取验证码
     */
    private void getVerifyCode(String phone) {
        if (!InputUtil.isEmpty(this, phone, "请输入手机号码！"))
            return;
        if (!InputUtil.isPhone(context, phone, "手机号格式有误！"))
            return;
        Map<String, String> mMap = new HashMap<>();
        mMap.put("mobile", phone);
        OkHttpUtils.postAync(Constant.AppRegisterGetPhone, new Gson().toJson(mMap), new HttpCallback<BasicBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicBean response) {
                super.onSuccess(response);
                if (response.getCode().equals("0")) {
                    TimeCountUtil timeCountUtil = new TimeCountUtil(60000, 1000, viewBinding.acuGetCode);
                    timeCountUtil.start();
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 提交解绑信息
     */
    private void submit(String code) {
        if (!InputUtil.isEmpty(this, code, "请输入短信验证码！")) return;
        Map<String, String> mMap = new HashMap<>();
        mMap.put("bankCardId", cardId);
        mMap.put("captcha", code);
        OkHttpUtils.postAync(Constant.AppUnBindBankCard, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {//成功后  返回刷新
                    setResult(200);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acu_get_code://获取验证码
                getVerifyCode(viewBinding.acuPhone.getText().toString());
                break;
            case R.id.m_submit:             //提交
                submit(viewBinding.acuVerifyCode.getText().toString());
                break;
            default:
                break;
        }
    }


}