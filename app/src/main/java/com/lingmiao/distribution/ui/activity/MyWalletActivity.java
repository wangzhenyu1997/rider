package com.lingmiao.distribution.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.bean.WalletBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityMyWalletBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.MathExtend;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的钱包
 * niuxinyu
 */
public class MyWalletActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityMyWalletBinding viewBinding;
    private int cardNum = 0;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityMyWalletBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        EventBus.getDefault().register(this);
        initView();
        getData();
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
        }, true, "我的钱包", 0, "");
        viewBinding.amwLayoutBalance.setOnClickListener(this);
        viewBinding.amwLayoutDeposit.setOnClickListener(this);
        viewBinding.amwLayoutIntegral.setOnClickListener(this);
        viewBinding.amwLayoutAlipay.setOnClickListener(this);
        viewBinding.amwLayoutBank.setOnClickListener(this);
    }

    /**
     * 收到刷新消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(PublicBean event) {
        if (event.getCode() == 15) {
            getData();
        }
    }

    /**
     * 获取页面数据
     */
    private void getData() {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppWalletIndex, new Gson().toJson(mMap), new HttpCallback<WalletBean>(context, getProgressDialog()) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(WalletBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    viewBinding.amwBalance.setText(MathExtend.round(response.getData().getBalanceAccount().getBalanceAmount(), 2) + "元");//余额
                    viewBinding.amwDeposit.setText(MathExtend.round(response.getData().getDepositAccount().getBalanceAmount(), 2) + "元");//押金
                    viewBinding.amwIntegral.setText(response.getData().getIntegralAccount().getBalanceAmount() + "分");//鱼蛋分
                    cardNum = response.getData().getBankCardNum();
                    if (response.getData().getBankCardNum() != 0) {
                        viewBinding.amwBindBank.setText(cardNum + "张");
                    } else {
                        viewBinding.amwBindBank.setText("点击设置");
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.amw_layout_balance:
                startActivity(new Intent(context, MyBalanceActivity.class));
                break;
            case R.id.amw_layout_deposit:
                startActivity(new Intent(context, MyDepositActivity.class));
                break;
            case R.id.amw_layout_integral:
                startActivity(new Intent(context, MyIntegralActivity.class));
                break;
            case R.id.amw_layout_alipay:
                startActivity(new Intent(context, PayTypeAliActivity.class));
                break;
            case R.id.amw_layout_bank:
//                if (cardNum!=0){//已绑定
                startActivity(new Intent(context, CardBindListActivity.class));
//                }else{//未绑定
//                    startActivity(new Intent(context, PayTypeBankActivity.class));
//                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}