package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicListBean;
import com.lingmiao.distribution.bean.PublicListBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityPayTypeAliBinding;
import com.lingmiao.distribution.dialog.ListDialog;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝
 * niuxinyu
 */
public class PayTypeAliActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityPayTypeAliBinding viewBinding;
    private ArrayList<String> typeList = new ArrayList<>();
    private String selectType;

    private String mId;                 //已存在支付宝账号时的主键id

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityPayTypeAliBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();
        initData();
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
        }, true, "支付宝", 0, "");

        viewBinding.ptaAccountType.setOnClickListener(this);
        viewBinding.ptaSubmit.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        typeList.add("0@个人账户");
        typeList.add("1@企业账户");
        Map<String, Object> mMap = new HashMap<>();
        Map<String, Object> mMapBody = new HashMap<>();
        mMapBody.put("type", 1);
        mMap.put("body", mMapBody);//账号类型, 1支付宝 2微信
        OkHttpUtils.postAync(Constant.AppQueryWithdrawAccountList, new Gson().toJson(mMap), new HttpCallback<PublicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(PublicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().getRecords() != null && response.getData().getRecords().size() != 0) {
                    mId = response.getData().getRecords().get(0).id;
                    if (response.getData().getRecords().get(0).ofPublic == 0) {
                        viewBinding.ptaAccountType.setText("个人账户");
                    } else {
                        viewBinding.ptaAccountType.setText("企业账户");
                    }
                    selectType = response.getData().getRecords().get(0).ofPublic + "";
                    viewBinding.ptaAccountName.setText(response.getData().getRecords().get(0).accountName);
                    viewBinding.ptaAccountPhone.setText(response.getData().getRecords().get(0).accountNo);
                }
            }
        });
    }

    /**
     * 选择账户类型方法
     */
    private void chooseType() {
        ListDialog dialog = new ListDialog(context, (position, id, text, chooseIndex) -> {
            viewBinding.ptaAccountType.setText(text);
            selectType = id;
        }, typeList, 0, true);
        dialog.show();
    }

    /**
     * 提交信息
     */
    private void mSubmit() {
        if (!checkInput(viewBinding.ptaAccountName.getText().toString(), viewBinding.ptaAccountPhone.getText().toString()))
            return;
        Map<String, String> mMap = new HashMap<>();
        if (mId != null) {
            mMap.put("id", mId);
        }
        mMap.put("type", "1");//账号类型, 1支付宝 2微信
        mMap.put("ofPublic", selectType);//0对私  1对公
        mMap.put("accountNo", viewBinding.ptaAccountPhone.getText().toString());
        mMap.put("accountName", viewBinding.ptaAccountName.getText().toString());
        OkHttpUtils.postAync(mId != null ? Constant.AppUpdateWithdrawAccount : Constant.AppSubmitWithdrawAccount, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    finish();
                }
            }
        });
    }

    /**
     * 检验输入框内容
     */
    private boolean checkInput(String name, String code) {
        if (!InputUtil.isEmpty(this, selectType, "请选择账户类型！")) {
            return false;
        }
        if (!InputUtil.isEmpty(this, name, "请输入账户名称！")) {
            return false;
        }
        if (!InputUtil.isEmpty(this, code, "请输入支付宝账号！")) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pta_account_type:
                chooseType();
                break;
            case R.id.pta_submit:
                mSubmit();
                break;
        }
    }
}