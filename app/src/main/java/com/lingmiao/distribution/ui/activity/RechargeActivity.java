package com.lingmiao.distribution.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.AuthResult;
import com.lingmiao.distribution.bean.PayResult;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.bean.RechargeBean;
import com.lingmiao.distribution.bean.WeixinBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityRechargeBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 充值
 * niuxinyu
 */
public class RechargeActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityRechargeBinding viewBinding;
    private int type = 3;//选择类型  2支付宝 3微信

    private String mDataId;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    //微信
    private IWXAPI mIWXAPI = WXAPIFactory.createWXAPI(this, Constant.APP_ID);

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityRechargeBinding.inflate(LayoutInflater.from(this));
        mDataId = getIntent().getStringExtra("dataId");
        EventBus.getDefault().register(this);
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
                super.onTopBack();
                finish();
            }
        }, true, "充值", 0, "");
        viewBinding.awRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.aw_weixin) {
                type = 3;
            } else {
                type = 2;
            }
        });
        viewBinding.awSubmit.setOnClickListener(this);
    }

    /**
     * 提交
     */
    private void mSubmit() {
        if (!checkInput(viewBinding.awMoney.getText().toString())) return;
        Map<String, String> mMap = new HashMap<>();
        mMap.put("accountId", mDataId);
        mMap.put("tradeChannel", type + "");
        mMap.put("amount", viewBinding.awMoney.getText().toString());
        OkHttpUtils.postAync(Constant.AppApplyRecharge, new Gson().toJson(mMap), new HttpCallback<RechargeBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(RechargeBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {//成功后  返回刷新
                    if (type == 3) {  //微信
                        WeixinBean param = new Gson().fromJson(response.getData(), WeixinBean.class);
                        PayReq req = new PayReq();
                        req.appId = param.getAppid(); // 应用
                        req.partnerId = param.getPartnerid(); // 商户号
                        req.prepayId = param.getPrepayid(); // 预支付会话ID
                        req.packageValue = "Sign=WXPay"; // 扩展字段该参数目前为固定值
                        req.nonceStr = param.getNoncestr(); // 随机字符串
                        req.timeStamp = param.getTimestamp(); // 时间戳
                        req.sign = param.getSign(); // 签名
                        getWeiXinPayInfo(req);
                    } else {
                        payOnline(response.getData());
                    }
                } else ToastUtil.showToast(context, response.getMessage());
            }
        });
    }

    /**
     * 微信支付
     */
    private void getWeiXinPayInfo(PayReq req) {
        mIWXAPI.registerApp(Constant.APP_ID);
        boolean isPaySupported = mIWXAPI.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            ToastUtil.showToast(this, "当前环境不支持微信支付!");
            return;
        }
        mIWXAPI.sendReq(req);
    }

    /**
     * 微信回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(PublicBean event) {
        if (event.getCode() == 16) {
            finish();
        }
    }

    /**
     * 支付宝
     */
    private void payOnline(final String info) {
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(RechargeActivity.this);
            Map<String, String> result = alipay.payV2(info, true);
            Log.i("msp", result.toString());

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.showToast(getApplicationContext(), "支付成功");
//                        startActivity(new Intent(getApplicationContext(), OrderSuccessActivity.class).putExtra("orderId",
//                                getIntent().getExtras().getString("orderId")).putExtra("money", mOrderMoneyValue).putExtra("orderTime", mOrderTime));
                        EventBus.getDefault().post(new PublicBean(15));
//                        setResult(200);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.showToast(getApplicationContext(), "支付失败");
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        ToastUtil.showToast(getApplicationContext(), "授权成功" + String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
                        ToastUtil.showToast(getApplicationContext(), "授权失败" + String.format("authCode:%s", authResult.getAuthCode()));
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 检验输入框内容
     */
    private boolean checkInput(String money) {
        if (!InputUtil.isEmpty(this, money, "请输入充值金额！")) {
            return false;
        }
        return InputUtil.isMoney(this, money, "无效金额，整数8位，小数2位，示例：247.23！");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.aw_submit) {
            mSubmit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}