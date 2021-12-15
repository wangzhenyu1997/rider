package com.lingmiao.distribution.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.lingmiao.distribution.R;
import com.lingmiao.distribution.dialog.LoadingDialog;

/**
 * FragmentSupport
 *
 * @author yandaocheng <br/>
 * 帮助支持类
 * 2017-12-18
 * 修改者，修改日期，修改内容
 */
public class FragmentSupport extends Fragment {
    protected Activity mActivity = null;
    private Dialog dialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void showProgressDialog() {
        if (dialog == null) {
            dialog = LoadingDialog.createLoadingDialog(mActivity, "载入中...");
            dialog.show();
        }
    }

    public void showProgressDialog(String msg) {
        if (dialog == null) {
            dialog = LoadingDialog.createLoadingDialog(mActivity, msg);
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
        dialog = LoadingDialog.createLoadingDialog(mActivity, "载入中...");
        return dialog;
    }

    public void finish() {
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}

