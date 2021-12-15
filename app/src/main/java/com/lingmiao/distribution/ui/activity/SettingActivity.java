package com.lingmiao.distribution.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;

import android.view.LayoutInflater;
import android.view.View;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.blankj.utilcode.util.Utils;
import com.lingmiao.distribution.base.UserManager;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.bean.UpdateBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivitySettingBinding;
import com.lingmiao.distribution.dialog.HomeConfirmDialog;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;
import com.james.common.utils.DialogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置
 * niuxinyu
 */
public class SettingActivity extends ActivitySupport implements View.OnClickListener {

    private ActivitySettingBinding viewBinding;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivitySettingBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();
    }

    /**
     * 初始化控件
     */
    @SuppressLint("SetTextI18n")
    private void initView() {
        viewBinding.topView.setData(new LayoutTopView.TopCallback() {
            @Override
            public void onTopBack() {
                super.onTopBack();
                finish();
            }
        }, true, "设置", 0, "");
        viewBinding.mModifyPass.setOnClickListener(this);
        viewBinding.mAbountUs.setOnClickListener(this);
        viewBinding.mModifyPhone.setOnClickListener(this);
        viewBinding.mUpdate.setOnClickListener(this);
        viewBinding.mOutLogin.setOnClickListener(this);
        viewBinding.mVersonName.setText("V" + PublicUtil.getAppVersionName(context));
    }

    /**
     * 退出登录
     */
    private void loginOut() {
        HomeConfirmDialog dialog = new HomeConfirmDialog(context, value -> {
            if (value) {
                Map<String, String> mMap = new HashMap<>();
                OkHttpUtils.postAync(Constant.AppLoginOut, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
                    @Override
                    public void onSuccess(LoginBean response) {
                        super.onSuccess(response);
                        if (response.getCode().equals(Constant.SUCCESS)) {
//                            PreferUtil.setPrefString(context, Constant.IS_USERPASS, ""); //登录成功、把password存到本地

                            UserManager.Companion.loginOut();

                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            ToastUtil.showToast(context, response.getMessage());
                        }
                    }
                });


            }
        }, "提示", "确认退出当前登录账号？", "取消", "确认退出");
        dialog.show();
    }

    /**
     * 检测app版本信息
     */
    private void checkVersion() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("appType", "1");
        mMap.put("currentVersion", PublicUtil.getAppVersionName(context));
        OkHttpUtils.postAync(Constant.AppUpdateOnLine, new Gson().toJson(mMap), new HttpCallback<UpdateBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(UpdateBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if (response.getData().isNeedUpgrade()) {
                        //检测到新版本
//                        HomeConfirmDialog dialog = new HomeConfirmDialog(context, value -> {
//                            if (value) {
//                                Constant.downLoadUrl = response.getData().getDownloadUrl();
//                                ToastUtil.showToast(context, "开始下载,请打开通知栏查看进度！");
//                                if (PublicUtil.isServiceExisted(Objects.requireNonNull(context), "com.lingmiao.distribution.service.UpdateService")) {
//                                    stopService();
//                                    startService();
//                                } else {
//                                    startService();
//                                }
//                            }
//                        }, "版本更新", "检测到新版本，是否立即更新？", "暂不更新", "立即更新");
//                        dialog.show();
                        upgrade(response.getData());
                    } else {
                        ToastUtil.showToast(context, "当前已是最新版本！");
                    }
                } else {
                    ToastUtil.showToast(context, "当前已是最新版本！");
                }
            }
        });
    }

    private AppCompatDialog versionUpdateDialog;

    private UIData crateUIData(UpdateBean bean) {
        return UIData.create().setTitle("版本更新")
                .setContent(bean.getUpgradeContent())
                .setDownloadUrl(bean.getDownloadUrl());
    }

    private void upgrade(UpdateBean response) {
        if(context == null || response == null
                || response.getDownloadUrl() == null || response.getDownloadUrl().length() == 0
                || !response.isNeedUpgrade()) {
            return;
        }
        versionUpdateDialog = DialogUtils.Companion.showVersionUpdateDialog(
                SettingActivity.this,
                "版本更新",
                response.getUpgradeContent() == null || response.getUpgradeContent().length() == 0 ? "检测到新版本，是否立即更新？" : response.getUpgradeContent(),
        "暂不更新",
                null,
                "立即更新",
                v -> {
                    DownloadBuilder builder = AllenVersionChecker
                    .getInstance()
                    .downloadOnly(crateUIData(response));
                    builder.setDirectDownload(true);
                    builder.setShowNotification(true);
                    builder.setShowDownloadingDialog(true);
                    builder.setShowDownloadFailDialog(false).getRequestVersionBuilder();
                    builder.executeMission(Utils.getApp());
        }, response.isCastUpdate());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_modify_pass:    //修改密码
                startActivity(new Intent(context, ModifyPasswordActivity.class).putExtra("type", 2));
                break;
            case R.id.m_abount_us:      //关于我们
                startActivity(new Intent(context, AboutUsActivity.class));
                break;
            case R.id.m_modify_phone:   //修改手机号
                startActivity(new Intent(context, ModifyPhoneActivity.class));
                break;
            case R.id.m_update:         //在线更新
                checkVersion();
                break;
            case R.id.m_out_login:      //退出登录
                loginOut();
                break;
        }
    }


}