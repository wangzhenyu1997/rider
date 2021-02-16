package com.fisheagle.mkt.base

import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.*
import com.google.gson.Gson
import com.lingmiao.distribution.base.SPConstants
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.bean.PersonalDataParam
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.login.bean.LoginBean
import com.james.common.utils.exts.isNotBlank

class UserManager {
    companion object {


        private var hasLogin = false
        private var loginInfo: LoginBean? = null
        private var userInfo: PersonalDataParam? = null

        fun getLoginInfo(): LoginBean? {
            val loginInfoString: String? = SPUtils.getInstance().getString(SPConstants.KEY_LOGIN_USER, "")
            if (!TextUtils.isEmpty(loginInfoString)) {
                loginInfo = Gson().fromJson(loginInfoString, LoginBean::class.java)
            }
            return loginInfo
        }

        fun isLogin(): Boolean {
            return SPUtils.getInstance().getBoolean(SPConstants.KEY_LOGIN, false)
        }

        fun setLogin(isLogin: Boolean) {
            hasLogin = isLogin
            SPUtils.getInstance().put(SPConstants.KEY_LOGIN, isLogin)
        }

        fun setLoginInfo(info: LoginBean) {
            SPUtils.getInstance().put(SPConstants.KEY_LOGIN_USER, GsonUtils.toJson(info))
            SPUtils.getInstance().put(SPConstants.KEY_LOGIN, true)
        }

        fun setUserInfo(rider: PersonalDataParam) {
            userInfo = rider;
            SPUtils.getInstance().put(SPConstants.KEY_USER_INFO, GsonUtils.toJson(rider));
        }

        fun getUserInfo() : PersonalDataParam ? {
            if(userInfo == null) {
                val str = SPUtils.getInstance().getString(SPConstants.KEY_USER_INFO);
                if(str?.isNotBlank() == true) {
                    userInfo = GsonUtils.fromJson(str, GsonUtils.getType(PersonalDataParam::class.java));
                }
            }
            return userInfo;
        }

        fun loginOut() {
            hasLogin = false
            loginInfo = null
            userInfo = null
            setPushID("");
            setUserPwd("");
            SPUtils.getInstance().put(SPConstants.KEY_LOGIN_USER, "")
            SPUtils.getInstance().put(SPConstants.KEY_LOGIN, false)
            SPUtils.getInstance().put(SPConstants.KEY_USER_INFO, "");
            JPushInterface.deleteAlias(Utils.getApp(), 1)
        }

        fun setUserNameAndPwd(name: String, pwd: String) {
            setUserName(name);
            setUserPwd(pwd);
        }

        // login user name
        fun setUserName(name: String) {
            SPUtils.getInstance().put(SPConstants.IS_USERNAME, name);
        }

        fun getUserName(): String {
            return SPUtils.getInstance().getString(SPConstants.IS_USERNAME, "");
        }

        // login user pwd
        fun setUserPwd(pwd: String) {
            SPUtils.getInstance().put(SPConstants.IS_USERPASS, pwd);
        }

        fun getUserPwd(): String {
            return SPUtils.getInstance().getString(SPConstants.IS_USERPASS, "");
        }

        fun isStoredUser() : Boolean {
            return getUserName()?.isNotBlank() && getUserPwd()?.isNotBlank();
        }

        // push
        fun setPushID(id : String) {
            SPUtils.getInstance().put(SPConstants.IS_ID, id);
        }

        fun getPushID() : String{
            return SPUtils.getInstance().getString(SPConstants.IS_ID, "");
        }

        fun setTakingModel(event : HomeModelEvent) {
            SPUtils.getInstance().put(Constant.KEY_HOME_TAKING_MODEL, GsonUtils.toJson(event));
        }

        fun getDefaultTakingModel() : HomeModelEvent {
            return HomeModelEvent(HomeModelEvent.DEFAULT_MODEL, HomeModelEvent.DEFAULT_SORT_OF_TAKE, HomeModelEvent.DEFAULT_SORT_DELIVERY);
        }
        fun getTakingModel() : HomeModelEvent {
            var str = SPUtils.getInstance().getString(Constant.KEY_HOME_TAKING_MODEL, "");
            var event = if(str == null || str?.isEmpty()) getDefaultTakingModel() else GsonUtils.fromJson(str, GsonUtils.getType(HomeModelEvent::class.java))
            return event;
        }
    }
}