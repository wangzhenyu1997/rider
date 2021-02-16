package com.lingmiao.distribution.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.lingmiao.distribution.R;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.dialog.LoadingDialog;
import com.lingmiao.distribution.service.UpdateService;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.StatusBarUtil;

/**
 * ActivitySupport
 *
 * @author yandaocheng <br/>
 * 帮助支持类
 * 2017-12-18
 * 修改者，修改日期，修改内容
 */
public abstract class ActivitySupport extends AppCompatActivity implements com.lingmiao.distribution.app.ISupport {
    protected Context context = null;
    private Dialog dialog;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
//        MyApplication.addActivity(this);
//        if (MyApplication.APP_STATUS != MyApplication.APP_STATUS_NORMAL) {
//            // 非正常启动流程，直接重新初始化应用界面
//            MyApplication.reInitApp();
//            return;
//        } else {
//
//        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
        setCreateView(savedInstanceState);
    }

    /**
     * 提供给子Activity设置界面的接口，不要在onCreate中初始化界面
     *
     * @param savedInstanceState
     */
    protected abstract void setCreateView(@Nullable Bundle savedInstanceState);


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        MyApplication.finishActivity(this);
    }

    public void showProgressDialog() {
        if (dialog == null) {
            dialog = LoadingDialog.createLoadingDialog(this, "载入中...");
            dialog.show();
        } else {
            dialog.show();
        }
    }

    public void showProgressDialog(String msg) {
        if (dialog == null) {
            dialog = LoadingDialog.createLoadingDialog(this, msg);
            dialog.show();
        }
    }

    public void closeProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 获取dialog对象
     *
     * @return
     */
    public Dialog getProgressDialog() {
        dialog = LoadingDialog.createLoadingDialog(this, "载入中...");
        return dialog;
    }

    /**
     * 关闭键盘事件
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public Application getMyApplication() {
//        return MyApplication.getInstance();
        return MyApp.getInstance();
    }

    @Override
    public Context getContext() {
        return context;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10086) {
			PublicUtil.installProcess(Constant.apkDownLoadPath + "yyps.apk");
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void startService() {
        Intent server = new Intent(this, UpdateService.class);
        startService(server);
    }

    public void stopService() {
        Intent server = new Intent(this, UpdateService.class);
        stopService(server);
    }
}
