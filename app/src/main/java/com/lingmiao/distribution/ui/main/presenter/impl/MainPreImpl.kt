package com.lingmiao.distribution.ui.main.presenter.impl

import com.fisheagle.mkt.base.UserManager
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.ui.login.api.LoginRepository
import com.lingmiao.distribution.ui.main.presenter.IMainPresenter
import com.lingmiao.distribution.ui.main.presenter.IVersionPresenter
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
class MainPreImpl(var view : IMainPresenter.View) : BasePreImpl(view), IMainPresenter {

    private val versionPresenter: IVersionPresenter by lazy {
        VersionPreImpl(view);
    }

    override fun getTabTitles(): Array<String> {
        return arrayOf("待接单", "待取货", "待送达");
    }

    override fun getUser() {
        mCoroutine.launch {
            val resp = LoginRepository.apiService.queryRider().awaitHiResponse();
            handleResponse(resp, {
                it?.data?.rider?.apply {
                    UserManager.setUserInfo(this);
                    view?.setUser(this)
                }
            }, {

            })

        }

    }

    override fun checkVersion(string: String) {
        versionPresenter?.checkVersion(string);
    }

}