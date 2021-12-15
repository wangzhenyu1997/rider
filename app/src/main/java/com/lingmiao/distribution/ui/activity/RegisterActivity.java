package com.lingmiao.distribution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;

import com.lingmiao.distribution.base.UserManager;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicBean;
import com.lingmiao.distribution.bean.CheckPhoneBean;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.bean.PersonalBean;
import com.lingmiao.distribution.bean.PersonalDataParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityRegisterBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.MD5Util;
import com.lingmiao.distribution.util.TimeCountUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * RegisterActivity
 *
 * @author yandaocheng <br/>
 * 注册
 * 2020-07-11
 * 修改者，修改日期，修改内容
 */

public class RegisterActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityRegisterBinding viewBinding;
    private boolean pwdIsVisible = false; //默认密码隐藏

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityRegisterBinding.inflate(LayoutInflater.from(this));
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
        }, true, "", 0, "");
        viewBinding.mSubmit.setOnClickListener(this);
        viewBinding.mGetCode.setOnClickListener(this);
        viewBinding.mAgreement.setOnClickListener(this);
        viewBinding.mShowPass.setOnClickListener(this);
        viewBinding.mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 11) { //校验号码是否存在
                    if (!InputUtil.isPhone(context, s.toString(), "手机号格式有误！")) {
//                        viewBinding.mPhone.setText("");
                    } else {
                        checkRegisterPhone(s.toString());
                    }
                }
            }
        });
    }

    /**
     * 校验手机号码是否存在
     */
    private void checkRegisterPhone(String phone) {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("mobile", phone);
        OkHttpUtils.postAync(Constant.AppCheckRegister, new Gson().toJson(mMap), new HttpCallback<CheckPhoneBean>(context) {
            @Override
            public void onSuccess(CheckPhoneBean response) {
                super.onSuccess(response);
                if (response.isData()) {
                    ToastUtil.showToastNew(context, "手机号已存在！");
//                    viewBinding.mPhone.setText("");
                }
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getPhone() {
        if (!InputUtil.isEmptyNew(context, viewBinding.mPhone.getText().toString(), "请输入手机号！")) return;
        if (!InputUtil.isPhoneNew(context, viewBinding.mPhone.getText().toString(), "手机号格式有误！"))
            return;
        Map<String, String> mMap = new HashMap<>();
        mMap.put("mobile", viewBinding.mPhone.getText().toString());
        OkHttpUtils.postAync(Constant.AppRegisterGetPhone, new Gson().toJson(mMap), new HttpCallback<BasicBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    TimeCountUtil timeCountUtil = new TimeCountUtil(60000, 1000, viewBinding.mGetCode);
                    timeCountUtil.start();
                } else {
                    ToastUtil.showToastNew(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 注册提交
     */
    private void submit(String phone, String code, String pass) {
        if (!InputUtil.isEmptyNew(context, phone, "请输入手机号！")) return;
        if (!InputUtil.isPhoneNew(context, phone, "手机号格式有误！"))
            return;
        if (!InputUtil.isEmptyNew(context, code, "请输入验证码！")) return;
        if (!InputUtil.isEmptyNew(context, pass, "请输入密码！")) return;
        if (pass.length() < 6) {
            ToastUtil.showToastNew(context, "密码长度最低6位！");
            return;
        }
        if (!viewBinding.lChooseCheckbox.isChecked()) {
            ToastUtil.showToastNew(context, "请阅读并同意服务条款！");
            return;
        }
        Map<String, String> mMap = new HashMap<>();
        mMap.put("mobile", phone);
        mMap.put("captcha", code);
        mMap.put("password", MD5Util.getMD5Str(pass));
        OkHttpUtils.postAync(Constant.AppRegister, new Gson().toJson(mMap), new HttpCallback<BasicBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicBean response) {
                super.onSuccess(response);
                ToastUtil.showToastNew(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    loginAcount(phone, pass);
                }
            }
        });
    }

    private void loginAcount(String userName, String password) {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("account", userName);
        mMap.put("password", MD5Util.getMD5Str(password));
        OkHttpUtils.postAync(Constant.AppLogin, new Gson().toJson(mMap), new HttpCallback<LoginBean>() {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    UserManager.Companion.setUserNameAndPwd(userName, MD5Util.getMD5Str(password));
//                    PreferUtil.setPrefString(context, Constant.IS_USERNAME, userName); //登录成功、把userName存到本地
//                    PreferUtil.setPrefString(context, Constant.IS_USERPASS, password); //登录成功、把password存到本地
                    Constant.TOKEN = response.getData().getToken();
                    Constant.loginState = true;
//                    PreferUtil.setPrefString(context, Constant.IS_TOCKEN, response.getData().getToken()); //登录成功、把TK存到本地
                    getUserInfo();

                } else {
                    ToastUtil.showToastNew(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 获取个人资料数据
     */
    private void getUserInfo() {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppUserInfoDetail, new Gson().toJson(mMap), new HttpCallback<PersonalBean>() {
            @Override
            public void onSuccess(PersonalBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().getRider() != null) {
                    PersonalDataParam personData = response.getData().getRider();

//                    PreferUtil.setPrefString(context, Constant.IS_ID, personData.getId()); //登录成功
                    UserManager.Companion.setPushID(personData.getId());

                    UserManager.Companion.setUserInfo(personData);

                    setJPushTag(personData.getId());

                    Constant.WORKSTATES = personData.getWorkStatus();
                    
                    startActivity(new Intent(context, IdentityNoActivity.class));
                    finish();
                } else {
                    ToastUtil.showToastNew(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 设置推送TAG
     */
    private void setJPushTag(String user_id) {
        Set<String> tagSet = new LinkedHashSet<>();
        tagSet.add("test_" + user_id);//其他环境用test_   生产环境pro_
        JPushInterface.setTags(context, 1, tagSet);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_submit:             //提交
                submit(viewBinding.mPhone.getText().toString(), viewBinding.mPhoneCode.getText().toString(), viewBinding.lUserPass.getText().toString());
                break;
            case R.id.m_get_code:           //获取验证码
                getPhone();
                break;
            case R.id.m_agreement:          //注册协议
                startActivity(new Intent(context, AgreementActivity.class));
                break;
            case R.id.m_show_pass:          //展示密码
                pwdIsVisible = !pwdIsVisible;
                if (pwdIsVisible) {
                    viewBinding.mShowPass.setImageResource(R.mipmap.eye_choose_on);
                    viewBinding.lUserPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    viewBinding.mShowPass.setImageResource(R.mipmap.eye_choose_un);
                    viewBinding.lUserPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            default:
                break;
        }
    }
}

