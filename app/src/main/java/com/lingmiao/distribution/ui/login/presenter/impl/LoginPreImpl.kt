package com.lingmiao.distribution.ui.login.presenter.impl

import com.blankj.utilcode.util.LogUtils
import com.lingmiao.distribution.base.UserManager
import com.lingmiao.distribution.bean.PersonalDataParam
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.login.api.LoginRepository
import com.lingmiao.distribution.ui.login.bean.LoginBean
import com.lingmiao.distribution.ui.login.bean.LoginVo
import com.lingmiao.distribution.ui.login.presenter.ILoginPresenter
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

/**
Create Date : 2020/12/263:40 PM
Auther      : Fox
Desc        :
 **/
class LoginPreImpl(private val view: ILoginPresenter.View) : BasePreImpl(view), ILoginPresenter {

    override fun login(request: LoginVo) {
        mCoroutine.launch {
            view.showDialogLoading();
            LogUtils.dTag("login model", request);
            val resp = LoginRepository.apiService.login(request).awaitHiResponse();
            handleResponse(resp) {
                if(resp?.data?.isSuccessAndData() == true) {
                    var login = resp?.data?.data;
                    if(login == null || login?.token == null || login?.token.isBlank()) {
                        return@handleResponse;
                    }
                    Constant.TOKEN = login.token

                    UserManager.setLoginInfo(login);

                    UserManager.setUserNameAndPwd(request?.account, request?.password);

                    queryRider(request, login, {
                        view?.showToast(it);
                    });
                } else {
                    view?.showToast(resp?.data?.message);
                }
            }
            view.hideDialogLoading();
        }
    }

    fun queryRider(request: LoginVo, login : LoginBean, callback : (String)  -> Unit) {
        mCoroutine.launch {
            LogUtils.dTag("query rider");
            val resp = LoginRepository.apiService.queryRider().awaitHiResponse();
            handleResponse(resp, {
                var data : PersonalDataParam? = resp?.data?.data?.rider;
                if(data == null || data?.id?.isNullOrEmpty() == true) {
                    view.onLoginError();
                    return@handleResponse;
                }

                Constant.WORKSTATES = data.getWorkStatus()

                Constant.loginState = true

                Constant.user = data;

                UserManager.setPushID(data.id);

                UserManager.setUserInfo(data);

                view.onLoginSuccess(data);
            }, {

                UserManager.setLogin(false);

                callback?.invoke(resp?.data?.message!!);
            });
        }
    }

}