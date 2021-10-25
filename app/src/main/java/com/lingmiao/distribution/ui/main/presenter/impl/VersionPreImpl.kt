package com.lingmiao.distribution.ui.main.presenter.impl

import com.lingmiao.distribution.ui.main.api.DispatchRepository
import com.lingmiao.distribution.ui.main.presenter.IVersionPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
open class VersionPreImpl(open var view : IVersionPresenter.View) : BasePreImpl(view),
    IVersionPresenter {

    override fun checkVersion(string: String) {
        mCoroutine.launch {

            val resp = DispatchRepository.upgrade(string);
            handleResponse(resp) {
                if(resp.data?.isSuccessAndData() == true) {
                    view.checkVersionSuccess(resp.data?.data);
                }
            }

        }
    }

}