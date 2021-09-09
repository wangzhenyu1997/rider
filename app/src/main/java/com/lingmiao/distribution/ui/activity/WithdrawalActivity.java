package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.CardListBean;
import com.lingmiao.distribution.bean.CardListParam;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.bean.PayTypeListBean;
import com.lingmiao.distribution.bean.PayTypeListDataParam;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityWithdrawalBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 提现
 * niuxinyu
 */
public class WithdrawalActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityWithdrawalBinding viewBinding;
    private String accountId = "";
    private int type = 2;//选择类型 1银行卡 2支付宝 3微信
    private CardListParam cardInfo = null;
    private PayTypeListDataParam aliInfo = null;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityWithdrawalBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        accountId = getIntent().getStringExtra("accountId");
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
                super.onTopBack();
                finish();
            }
        }, true, "提现", 0, "");
        viewBinding.awRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.aw_ali) {
                type = 2;
                if (aliInfo == null) {
                    getAliInfo();
                }
                viewBinding.awPayAliLayout.setVisibility(View.VISIBLE);
                viewBinding.awPayBankLayout.setVisibility(View.GONE);
            } else {
                type = 1;
                viewBinding.awPayAliLayout.setVisibility(View.GONE);
                viewBinding.awPayBankLayout.setVisibility(View.VISIBLE);
                if (cardInfo == null) {
                    getBankCardInfo();
                }
            }
        });
        viewBinding.awSubmit.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getAliInfo();
//        getChargeRate();
    }

    /**
     * 获取银行卡信息
     */
    private void getBankCardInfo() {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppQueryBankCardList, new Gson().toJson(mMap), new HttpCallback<CardListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(CardListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().size() != 0) {
                    cardInfo = response.getData().get(0);
                    viewBinding.awBankAccount.setText(response.getData().get(0).getOpenAccountName());
                    viewBinding.awBankName.setText(response.getData().get(0).getBankName());
                    viewBinding.awCardCode.setText(response.getData().get(0).getCardNo());
                }
            }
        });
    }

    private void getChargeRate() {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppQueryChargeRate, new Gson().toJson(mMap), new HttpCallback<String>(context, getProgressDialog()) {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
//                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().size() != 0) {
//                    cardInfo = response.getData().get(0);
//                    viewBinding.awBankAccount.setText(response.getData().get(0).getOpenAccountName());
//                    viewBinding.awBankName.setText(response.getData().get(0).getBankName());
//                    viewBinding.awCardCode.setText(response.getData().get(0).getCardNo());
//                }
            }
        });
        //
    }
    /**
     * 获取支付宝账户信息
     */
    private void getAliInfo() {
        Map<String, Object> mMap = new HashMap<>();
        Map<String, Object> mMapBody = new HashMap<>();
        mMapBody.put("type", 1);
        mMap.put("body", mMapBody);//账号类型, 1支付宝 2微信
        OkHttpUtils.postAync(Constant.AppQueryWithdrawAccountList, new Gson().toJson(mMap), new HttpCallback<PayTypeListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(PayTypeListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().getRecords() != null && response.getData().getRecords().size() != 0) {
                    aliInfo = response.getData().getRecords().get(0);
                    viewBinding.awPayAccount.setText(response.getData().getRecords().get(0).getAccountNo());
                    viewBinding.awPayName.setText(response.getData().getRecords().get(0).getAccountName());
                }
            }
        });
    }

    /**
     * 提交
     */
    private void mSubmit() {
        if (!checkInput(viewBinding.awMoney.getText().toString()))
            return;
        if (type == 2 && aliInfo == null) {
            ToastUtil.showToast(context, "请先绑定相关支付宝账号！");
            return;
        } else if (type == 1 && cardInfo == null) {
            ToastUtil.showToast(context, "请先绑定相关银行卡账号！");
            return;
        }
        Map<String, String> mMap = new HashMap<>();
        mMap.put("accountId", accountId);//账户id ,
        mMap.put("withdrawChannel", type + "");
        mMap.put("amount", viewBinding.awMoney.getText().toString());//申请提现金额 ,
        if (type == 1) {//1银行卡 2支付宝
            mMap.put("bankName", cardInfo.getBankName());//银行名称
            mMap.put("cardNo", cardInfo.getCardNo());//银行卡号
            mMap.put("openAccountName", cardInfo.getOpenAccountName());//银行卡号
        } else {
            mMap.put("otherAccountTypeName", "支付宝");//其他账户类型名称
            mMap.put("otherAccountNo", aliInfo.getAccountNo());//其他账号
            mMap.put("otherAccountName", aliInfo.getAccountName());//其他账户名
        }
        OkHttpUtils.postAync(Constant.AppApplyWithdraw, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {//成功后  返回刷新
                    EventBus.getDefault().post(new PublicBean(15));
                    finish();
                }
            }
        });

    }

    /**
     * 检验输入框内容
     */
    private boolean checkInput(String money) {
        if (!InputUtil.isEmpty(this, money, "请输入提现金额！")) {
            return false;
        }
        return InputUtil.isMoney(this, money, "无效金额，整数8位，小数2位，示例：247.23！");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aw_submit:
                mSubmit();
                break;
        }
    }
}