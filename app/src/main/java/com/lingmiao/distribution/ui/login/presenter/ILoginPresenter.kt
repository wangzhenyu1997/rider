package com.lingmiao.distribution.ui.login.presenter

import com.lingmiao.distribution.bean.PersonalDataParam
import com.lingmiao.distribution.ui.login.bean.LoginBean
import com.lingmiao.distribution.ui.login.bean.LoginVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2020/12/263:39 PM
Auther      : Fox
Desc        :
 **/
interface ILoginPresenter : BasePresenter {

    fun login(request : LoginVo);

    interface View : BaseView {

        fun onLoginSuccess(data : PersonalDataParam);

        fun onLoginError();
    }
}