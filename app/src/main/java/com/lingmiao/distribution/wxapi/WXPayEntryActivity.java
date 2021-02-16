package com.lingmiao.distribution.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lingmiao.distribution.R;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.util.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mIWXAPI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);

        mIWXAPI = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        mIWXAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mIWXAPI.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case Constant.WEIXIN_PAY_RESULT_SUCCESS:
                    //广播关掉订单相关页面
                    ToastUtil.showToast(this, "支付成功!");
                    EventBus.getDefault().post(new PublicBean(15));
                    EventBus.getDefault().post(new PublicBean(16));
                    WXPayEntryActivity.this.finish();
                    break;

                case Constant.WEIXIN_PAY_RESULT_ERROR:
                    ToastUtil.showToast(this, "微信支付异常!");
                    WXPayEntryActivity.this.finish();
                    break;

                case Constant.WEIXIN_PAY_RESULT_CANCEL:
                    ToastUtil.showToast(this, "用户取消微信支付!");
                    WXPayEntryActivity.this.finish();
                    break;
            }
        } else {
            ToastUtil.showToast(this, "微信支付异常!");
            WXPayEntryActivity.this.finish();
        }
    }
}