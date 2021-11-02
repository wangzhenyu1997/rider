package com.lingmiao.distribution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.lingmiao.distribution.base.UserManager;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicBean;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityModifyPhoneBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.TimeCountUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改手机号
 * niuxinyu
 */
public class ModifyPhoneActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityModifyPhoneBinding viewBinding;
    private int pageStep = 1;//当前步骤

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityModifyPhoneBinding.inflate(LayoutInflater.from(this));
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
                finish();
            }
        }, true, "更换手机号", 0, "");
        viewBinding.ampGetCode.setOnClickListener(this);
        viewBinding.ampNewGetCode.setOnClickListener(this);
        viewBinding.mSubmit.setOnClickListener(this);
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
                    if (pageStep == 1) {
                        TimeCountUtil timeCountUtil = new TimeCountUtil(60000, 1000, viewBinding.ampGetCode);
                        timeCountUtil.start();
                    } else {
                        TimeCountUtil timeCountUtil = new TimeCountUtil(60000, 1000, viewBinding.ampNewGetCode);
                        timeCountUtil.start();
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }


    /**
     * 检验输入框内容
     */
    private boolean checkInput(String phone, String password) {
        if (!InputUtil.isEmpty(this, phone, "请输入手机号！")) {
            return false;
        }
        if (!InputUtil.isPhone(this, phone, "手机号格式有误！")) {
            return false;
        }
        return InputUtil.isEmpty(this, password, "请输入验证码！");
    }


    /**
     * 校验手机号信息
     */
    private void checkPhone(String phone, String code) {
        if (!checkInput(phone, code)) return;
        Map<String, String> mMap = new HashMap<>();
        mMap.put("mobile", phone);
        mMap.put("captcha", code);
        OkHttpUtils.postAync(Constant.AppValidateModify, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                if (response.getCode().equals("0")) {
                    viewBinding.ampPageFirst.setVisibility(View.GONE);
                    viewBinding.ampPageNext.setVisibility(View.VISIBLE);
                    pageStep = 2;
                    viewBinding.mSubmit.setText("提交");
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }


    /**
     * 提交修改信息
     */
    private void submit(String phone, String code) {
        if (!checkInput(phone, code)) return;
        Map<String, String> mMap = new HashMap<>();
        mMap.put("mobile", phone);
        mMap.put("captcha", code);
        OkHttpUtils.postAync(Constant.AppUpdateMobile, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals("0")) {
//                    PreferUtil.setPrefString(context, Constant.IS_USERNAME, "");

                    UserManager.Companion.loginOut();

                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_submit:     //提交
                if (pageStep == 1) {
                    checkPhone(viewBinding.ampPhone.getText().toString(), viewBinding.ampVerifyCode.getText().toString());
                } else {
                    submit(viewBinding.ampNewPhone.getText().toString(), viewBinding.ampNewVerifyCode.getText().toString());
                }
                break;
            case R.id.amp_get_code://获取验证码
                getVerifyCode(viewBinding.ampPhone.getText().toString());
                break;
            case R.id.amp_new_get_code: //获取新手机号验证码
                getVerifyCode(viewBinding.ampNewPhone.getText().toString());
                break;
        }
    }
}