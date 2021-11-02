package com.lingmiao.distribution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;

import com.lingmiao.distribution.base.UserManager;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicBean;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityModifyPasswordBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.MD5Util;
import com.lingmiao.distribution.util.TimeCountUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码
 * niuxinyu
 */
public class ModifyPasswordActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityModifyPasswordBinding viewBinding;
    private int mType;  // 1.忘记密码   2.修改密码
    private boolean pwdIsVisible = false; //默认密码隐藏

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityModifyPasswordBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        mType = getIntent().getIntExtra("type", 1);
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
        }, true, mType == 1 ? "忘记密码" : "修改密码", 0, "");
        viewBinding.mShowPass.setOnClickListener(this);
        viewBinding.mSubmit.setOnClickListener(this);
        viewBinding.ampGetCode.setOnClickListener(this);
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
        OkHttpUtils.postAync(Constant.AppGetResetPasswordCaptcha, new Gson().toJson(mMap), new HttpCallback<BasicBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicBean response) {
                super.onSuccess(response);
                if (response.getCode().equals("0")) {
                    TimeCountUtil timeCountUtil = new TimeCountUtil(60000, 1000, viewBinding.ampGetCode);
                    timeCountUtil.start();
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 提交修改信息
     */
    private void submit(String phone, String password, String code) {
        if (!checkInput(phone, password)) return;
        Map<String, String> mMap = new HashMap<>();
        mMap.put("mobile", phone);
        mMap.put("password", MD5Util.getMD5Str(password));
        mMap.put("captcha", code);
        OkHttpUtils.postAync((mType == 1 ? Constant.AppResetPassword : Constant.AppUpdatePassword), new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals("0")) {//成功后  登陆状态失效
//                    PreferUtil.setPrefString(context, Constant.IS_USERPASS, "");

                    UserManager.Companion.loginOut();

                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    /**
     * 检验输入框内容
     */
    private boolean checkInput(String userName, String password) {
        if (!InputUtil.isEmpty(this, userName, "请输入账号/手机号！")) {
            return false;
        }
        if (!InputUtil.isPhone(this, userName, "手机号格式有误！")) {
            return false;
        }
        if (!InputUtil.isEmpty(this, password, "请输入登录密码！")) {
            return false;
        }
        if (password.length() < 6) {
            ToastUtil.showToast(context, "密码长度最低6位！");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_show_pass:          //展示密码
                pwdIsVisible = !pwdIsVisible;
                if (pwdIsVisible) {
                    viewBinding.mShowPass.setImageResource(R.mipmap.eye_choose_on);
                    viewBinding.ampNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    viewBinding.mShowPass.setImageResource(R.mipmap.eye_choose_un);
                    viewBinding.ampNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.amp_get_code://获取验证码

                getVerifyCode(viewBinding.ampPhone.getText().toString());
                break;
            case R.id.m_submit:             //提交
                submit(viewBinding.ampPhone.getText().toString(),
                        viewBinding.ampNewPass.getText().toString(),
                        viewBinding.ampVerifyCode.getText().toString());
                break;
            default:
                break;
        }
    }


}