package com.lingmiao.distribution.app;

import android.app.Activity;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.fisheagle.mkt.base.UserManager;
import com.google.gson.Gson;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.bean.PersonalBean;
import com.lingmiao.distribution.bean.PersonalDataParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.ui.activity.HomeActivity;
import com.lingmiao.distribution.ui.activity.IdentityExNoActivity;
import com.lingmiao.distribution.ui.activity.IdentityExamineActivity;
import com.lingmiao.distribution.ui.activity.IdentityNoActivity;
import com.lingmiao.distribution.ui.login.LoginActivity;
import com.lingmiao.distribution.ui.main.MainActivity;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Activity
 *
 * @author yandaocheng <br/>
 * 名称
 * 2018-08-14
 * 修改者，修改日期，修改内容
 */
public class AuthLogin {

    public static void autoLogin(Activity activity, String userName, String password, Boolean state, final HttpCallback callback) {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("account", userName);
        mMap.put("password", password);
        OkHttpUtils.postAync(Constant.AppLogin, new Gson().toJson(mMap), new HttpCallback<LoginBean>() {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    UserManager.Companion.setUserName(userName);
                    UserManager.Companion.setUserPwd(password);

                    UserManager.Companion.setLoginInfo(response.convert());

//                    PreferUtil.setPrefString(activity, Constant.IS_USERNAME, userName); //登录成功、把userName存到本地
//                    PreferUtil.setPrefString(activity, Constant.IS_USERPASS, password); //登录成功、把password存到本地
                    Constant.TOKEN = response.getData().getToken();

                    Constant.loginState = true;
                    //获取骑手资料  判断跳转方向
                    getUserInfo(activity);
                } else {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            }

            @Override
            public void onFailure(int code, String message) {
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });
    }

    /**
     * 获取个人资料数据
     */
    private static void getUserInfo(Activity activity) {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppUserInfoDetail, new Gson().toJson(mMap), new HttpCallback<PersonalBean>() {
            @Override
            public void onSuccess(PersonalBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().getRider() != null) {
                    PersonalDataParam personData = response.getData().getRider();
//                    PreferUtil.setPrefString(activity, Constant.IS_ID, personData.getId());

                    Constant.WORKSTATES = personData.getWorkStatus();

                    Constant.user = personData;
//                    UserManager.Companion.setUserInfo(personData);

                    UserManager.Companion.setPushID(personData.getId());

                    setJPushTag(activity, personData.getId());

                    //auditStatus 0未提交    1已提交    2审核通过   3审核不通过
                    if (personData.getAuditStatus() == 2) {//审核通过
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    } else if (personData.getAuditStatus() == 0) {//未提交
                        Intent intent = new Intent(activity, IdentityNoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    } else if (personData.getAuditStatus() == 1) {//已提交
                        Intent intent = new Intent(activity, IdentityExamineActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    } else if (personData.getAuditStatus() == 3) {//审核不通过
                        Intent intent = new Intent(activity, IdentityExNoActivity.class).putExtra("reason",personData.getRefuseReason());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            }
        });
    }

    /**
     * 设置推送TAG
     */
    private static void setJPushTag(Activity activity, String user_id) {
        Set<String> tagSet = new LinkedHashSet<>();
        tagSet.add("test_" + user_id);//其他环境用test_   生产环境pro_
        JPushInterface.setTags(activity, 1, tagSet);
    }
}
