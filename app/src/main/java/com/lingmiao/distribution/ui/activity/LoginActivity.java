package com.lingmiao.distribution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.fisheagle.mkt.base.UserManager;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.bean.PersonalBean;
import com.lingmiao.distribution.bean.PersonalDataParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityLoginBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.ui.main.MainActivity;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.MD5Util;
import com.lingmiao.distribution.util.PreferUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * LoginActivity
 *
 * @author yandaocheng <br/>
 * 登录
 * 2020-07-09
 * 修改者，修改日期，修改内容
 */

public class LoginActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityLoginBinding viewBinding;
    private boolean pwdIsVisible = false; //默认密码隐藏

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();
        setCache();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewBinding.topView.setData(new LayoutTopView.TopCallback() {
        }, false, "", 0, "");
        viewBinding.lLoginBtn.setOnClickListener(this);
        viewBinding.lRegister.setOnClickListener(this);
        viewBinding.lForgetPass.setOnClickListener(this);
        viewBinding.lAgreement.setOnClickListener(this);
        viewBinding.mShowPass.setOnClickListener(this);
    }

    /**
     * 设置缓存
     */
    private void setCache() {
        String mUsrName = UserManager.Companion.getUserName();//PreferUtil.getPrefString(context, Constant.IS_USERNAME, "");
        viewBinding.lUserName.setText(mUsrName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.l_login_btn:          //登录
                submit(viewBinding.lUserName.getText().toString(), viewBinding.lUserPass.getText().toString());
                break;
            case R.id.l_forget_pass:        //忘记密码
                startActivity(new Intent(context, ModifyPasswordActivity.class).putExtra("type", 1));
                break;
            case R.id.l_register:           //注册
                startActivity(new Intent(context, RegisterActivity.class));
                break;
            case R.id.l_agreement:          //协议
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

    /**
     * 登录
     */
    private void submit(String userName, String password) {
        if (!checkInput(userName, password)) {
            return;
        }
        Map<String, String> mMap = new HashMap<>();
        mMap.put("account", userName);
        mMap.put("password", MD5Util.getMD5Str(password));
        OkHttpUtils.postAync(Constant.AppLogin, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    UserManager.Companion.setUserNameAndPwd(userName, MD5Util.getMD5Str(password));

                    Constant.TOKEN = response.getData().getToken();

                    Constant.loginState = true;
                    //获取骑手资料  判断跳转方向
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

                    UserManager.Companion.setPushID(personData.getId());

                    Constant.WORKSTATES = personData.getWorkStatus();

                    UserManager.Companion.setUserInfo(personData);

                    setJPushTag(personData.getId());

                    //auditStatus 0未提交    1已提交    2审核通过   3审核不通过
                    if (personData.getAuditStatus() == 2) {//审核通过
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else if (personData.getAuditStatus() == 0) {//未提交
                        Intent intent = new Intent(context, IdentityNoActivity.class);
                        context.startActivity(intent);
                    } else if (personData.getAuditStatus() == 1) {//已提交
                        Intent intent = new Intent(context, IdentityExamineActivity.class);
                        context.startActivity(intent);
                    } else if (personData.getAuditStatus() == 3) {//审核不通过
                        Intent intent = new Intent(context, IdentityExNoActivity.class).putExtra("reason",personData.getRefuseReason());
                        context.startActivity(intent);
                    }
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

    /**
     * 检验输入框内容
     */
    private boolean checkInput(String userName, String password) {
        if (!InputUtil.isEmptyNew(this, userName, "请输入账号/手机号！")) {
            return false;
        }
        if (!InputUtil.isPhoneNew(this, userName, "手机号格式有误！")) {
            return false;
        }
        if (!InputUtil.isEmptyNew(this, password, "请输入登录密码！")) {
            return false;
        }
        if(password.length() < 6){
            ToastUtil.showToastNew(context,"密码长度最低6位！");
            return false;
        }
        if (!viewBinding.lChooseCheckbox.isChecked()) {
            ToastUtil.showToastNew(context, "请阅读并同意服务条款！");
            return false;
        }
        return true;
    }

    //双击退出应用
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showToastNew(this, "再按一次返回桌面");
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

