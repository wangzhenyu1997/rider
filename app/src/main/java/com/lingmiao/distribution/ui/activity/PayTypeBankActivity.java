package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicListBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityPayTypeBankBinding;
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
 * 银行卡
 * niuxinyu
 */
public class PayTypeBankActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityPayTypeBankBinding viewBinding;
    private ArrayList<String> bankTypeList = new ArrayList<>();
    private ArrayList<String> bankList = new ArrayList<>();

    private String selectBankId;
    private String selectType;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityPayTypeBankBinding.inflate(LayoutInflater.from(this));
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
        }, true, "银行卡", 0, "");
        viewBinding.ptaAccountType.setOnClickListener(this);
        viewBinding.ptaBankSelect.setOnClickListener(this);
        viewBinding.ptaSubmit.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        bankTypeList.add("0@个人账户");
        bankTypeList.add("1@企业账号");
        getBankList();
    }


    /**
     * 获取银行列表
     */
    private void getBankList() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("type", "bank_list");
        OkHttpUtils.postAync(Constant.AppGetListByType, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    for (int i = 0; i < response.getData().size(); i++) {
                        bankList.add(response.getData().get(i).getValue() + "@" + response.getData().get(i).getLabel());
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
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
            selectType=id;
        }, bankTypeList, 0, true);
        dialog.show();
    }

    /**
     * 选择银行
     */
    private void chooseBank() {
        ListDialog dialog = new ListDialog(context, (position, id, text, chooseIndex) -> {
            viewBinding.ptaBankSelect.setText(text);
            selectBankId=id;
        }, bankList, 0, true);
        dialog.show();
    }


    /**
     * 提交信息
     */
    private void mSubmit(){
        if (!checkInput(viewBinding.ptaAccountName.getText().toString(),viewBinding.ptaBankCode.getText().toString())) return;
        Map<String, String> mMap = new HashMap<>();
        mMap.put("bankCardType", selectType);
        mMap.put("bankName", viewBinding.ptaBankSelect.getText().toString());
        mMap.put("cardNo", viewBinding.ptaBankCode.getText().toString());
        mMap.put("openAccountName", viewBinding.ptaAccountName.getText().toString());
        mMap.put("isDefault;;", "0");
        OkHttpUtils.postAync(Constant.AppBindBankCard, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    setResult(200);
                    finish();
                }
            }
        });
    }


    /**
     * 检验输入框内容
     */
    private boolean checkInput(String name,String code) {
        if (!InputUtil.isEmpty(this, selectType, "请选择账户类型！")) {
            return false;
        }
        if (!InputUtil.isEmpty(this, selectBankId, "请选择开户银行！")) {
            return false;
        }
        if (!InputUtil.isEmpty(this, name, "请输入账户名称！")) {
            return false;
        }
        if (!InputUtil.isEmpty(this, code, "请输入银行账号！")) {
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
            case R.id.pta_bank_select:
                chooseBank();
                break;
            case R.id.pta_submit:
                mSubmit();
                break;
        }

    }
}