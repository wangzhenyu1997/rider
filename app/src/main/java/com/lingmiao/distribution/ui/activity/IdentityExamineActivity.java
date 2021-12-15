package com.lingmiao.distribution.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.lingmiao.distribution.base.UserManager;
import com.github.nrecyclerview.interfaces.LoadingListener;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.PersonalBean;
import com.lingmiao.distribution.bean.PersonalDataParam;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.bean.PushBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityIdentityExamineBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.GlideUtil;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopTwoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * IdentityNoActivity
 *
 * @author yandaocheng <br/>
 * 身份认证 -- 审核中
 * 2020-07-11
 * 修改者，修改日期，修改内容
 */

public class IdentityExamineActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityIdentityExamineBinding viewBinding;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityIdentityExamineBinding.inflate(LayoutInflater.from(this));
        EventBus.getDefault().register(this);
        setContentView(viewBinding.getRoot());
        initView();
        getUserInfo();
    }

    /**
     * 初始化控件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        viewBinding.topView.setData(new LayoutTopTwoView.TopCallback() {
            @Override
            public void onTopBack() { //侧滑菜单
                viewBinding.tradeDrawer.openDrawer(Gravity.START);
            }

            @Override
            public void onRightImage() {
                startActivity(new Intent(context, MessageListActivity.class));
            }
        }, true, "身份认证", R.mipmap.home_right_image, null);
        viewBinding.mSubmit.setOnClickListener(v -> PublicUtil.callPhone("021-86739851", IdentityExamineActivity.this));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth() / 6 * 4, LinearLayout.LayoutParams.MATCH_PARENT);
        viewBinding.mAllView.setLayoutParams(param);
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //屏蔽穿透事件
        viewBinding.mAllView.setOnTouchListener((v, event) -> true);
        viewBinding.mOrderFee.setOnClickListener(this);
        viewBinding.mUserRe.setOnClickListener(this);
        viewBinding.mSettlement.setOnClickListener(this);
//        viewBinding.mZizhi.setOnClickListener(this);
        viewBinding.mMoney.setOnClickListener(this);
        viewBinding.mPingtai.setOnClickListener(this);
        viewBinding.mHelp.setOnClickListener(this);
        viewBinding.mSetting.setOnClickListener(this);
        viewBinding.mScrollView.setLoadingListener(new LoadingListener() {
            @Override
            public void onRefresh() {
                initState();
            }
        });
    }

    /**
     * 获取个人资料数据
     */
    private void getUserInfo() {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppUserInfoDetail, new Gson().toJson(mMap), new HttpCallback<PersonalBean>() {
            @Override
            public void onSuccess(PersonalBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().getRider() != null) {
                    PersonalDataParam data = response.getData().getRider();
                    UserManager.Companion.setUserInfo(data);
                    GlideUtil.load(context, data.getHeadImgUrl(), viewBinding.lpcImg, GlideUtil.getHeadImgOption());
                    viewBinding.mName.setText(data.getName());
                    viewBinding.mPhone.setText(data.getMobile());
                }
            }
        });
    }

    private void initState() {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppUserInfoDetail, new Gson().toJson(mMap), new HttpCallback<PersonalBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(PersonalBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().getRider() != null) {
                    viewBinding.mScrollView.refreshComplete();//刷新成功
                    PersonalDataParam personData = response.getData().getRider();
                    UserManager.Companion.setUserInfo(personData);
                    //auditStatus 0未提交    1已提交    2审核通过   3审核不通过
                    if (personData.getAuditStatus() == 2) {//审核通过
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (personData.getAuditStatus() == 3) {//审核不通过
                        Intent intent = new Intent(context, IdentityExNoActivity.class).putExtra("reason", personData.getRefuseReason());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 收到推送消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(PublicBean event) {
        if (event.getMessage() != null && !event.getMessage().equals("")) {
            PushBean param = new Gson().fromJson(event.getMessage(), PushBean.class);
            if (param.getType() == 2) {
                initState();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            getUserInfo();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_user_re:            //用户信息
                startActivityForResult(new Intent(context, PersonalInfoActivity.class), 100);
                break;
            case R.id.m_order_fee:          //订单费用
                startActivity(new Intent(context, OrderFeeListActivity.class));
                break;
            case R.id.m_settlement:         //结算对账
                startActivity(new Intent(context, BillListActivity.class));
                break;
//            case R.id.m_zizhi:              //我的资质
////                startActivity(new Intent(context, ComplaintActivity.class));
////                break;
            case R.id.m_money:              //我的钱包
                startActivity(new Intent(context, MyWalletActivity.class));
                break;
            case R.id.m_pingtai:            //平台规则
                startActivity(new Intent(context, PlatformRulesActivity.class));
                break;
            case R.id.m_help:               //帮助中心
                startActivity(new Intent(context, HelpCenterActivity.class));
                break;
            case R.id.m_setting:            //设置
                startActivity(new Intent(context, SettingActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

