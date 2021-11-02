package com.lingmiao.distribution.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;

import com.lingmiao.distribution.base.UserManager;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.AuthLogin;
import com.lingmiao.distribution.app.MyApplication;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.bean.UpdateBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.dialog.UpdateConfirmDialog;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.service.UpdateService;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * WelcomeActivity
 *
 * @author yandaocheng <br/>
 * 欢迎页
 * 2018-04-23
 * 修改者，修改日期，修改内容
 */
public class WelcomeActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.APP_STATUS = MyApplication.APP_STATUS_NORMAL; // App正常的启动，设置App的启动状态为正常启动
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        initView();
    }

    private void initView() {
        XXPermissions.with(this)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION, Permission.WRITE_EXTERNAL_STORAGE, Permission.RECORD_AUDIO, Permission.CAMERA) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
//                            WindowManager wm = (WindowManager) getContext().getSystemService(context.WINDOW_SERVICE);
//                            Constant.displayWidth = wm.getDefaultDisplay().getWidth();
//                            Constant.displayHeight = SystemAppUtils.getDpi(context);
//                            checkVersion();
                            toNextPage();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtil.showToast(context, "被永久拒绝授权，请手动授予权限！");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(context);
                        } else {
                            ToastUtil.showToast(context, "获取权限失败！");
                        }
                    }
                });
    }


    /**
     * 检测app版本信息
     */
    private void checkVersion() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("appType", "1");
        mMap.put("currentVersion", PublicUtil.getAppVersionName(context));
        OkHttpUtils.postAync(Constant.AppUpdateOnLine, new Gson().toJson(mMap), new HttpCallback<UpdateBean>() {
            @Override
            public void onSuccess(UpdateBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if (response.getData().isCastUpdate()) {  //是否强制升级
                        //检测到新版本
                        UpdateConfirmDialog dialog = new UpdateConfirmDialog(context, value -> {
                            if (value) {
                                Constant.downLoadUrl = response.getData().getDownloadUrl();
                                ToastUtil.showToast(context, "开始下载,请打开通知栏查看进度！");
                                if (PublicUtil.isServiceExisted(Objects.requireNonNull(context), "com.lingmiao.distribution.service.UpdateService")) {
                                    stopService();
                                    startService();
                                } else {
                                    startService();
                                }
                            }
                        }, "版本更新", "检测到新版本，是否立即更新？", "暂不更新", "立即更新");
                        dialog.show();
                    } else {
                        toNextPage();
                    }
                } else {
                    toNextPage();
                }
            }

            @Override
            public void onFailure(int code, String message) {
                toNextPage();
            }
        });
    }

    private void toNextPage() {
        if (UserManager.Companion.isStoredUser()) {
            AuthLogin.autoLogin(WelcomeActivity.this, UserManager.Companion.getUserName(), UserManager.Companion.getUserPwd(), true, new HttpCallback<PublicBean>() {
            });
        } else {
            initData();
        }
    }

    public void startService() {
        // TODO Auto-generated method stub
        Intent server = new Intent(this, UpdateService.class);
        startService(server);
    }

    public void stopService() {
        // TODO Auto-generated method stub
        Intent server = new Intent(this, UpdateService.class);
        stopService(server);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10086) {
            PublicUtil.installProcess(Constant.apkDownLoadPath + "yyps.apk");
        }
    }

    private void initData() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, com.lingmiao.distribution.ui.login.LoginActivity.class));
            finish();
        }, Constant.TIME_DELAY_WELCOME);
    }
}
