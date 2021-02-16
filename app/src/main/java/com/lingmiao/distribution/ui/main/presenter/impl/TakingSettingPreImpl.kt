package com.lingmiao.distribution.ui.main.presenter.impl

import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.ui.main.api.DispatchRepository
import com.lingmiao.distribution.ui.main.presenter.ITakingSettingPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
open class TakingSettingPreImpl(open var view : ITakingSettingPresenter.View) : BasePreImpl(view),
    ITakingSettingPresenter {

    override fun updateSetting(event: HomeModelEvent?) {
        mCoroutine.launch {
            view?.showDialogLoading();
            val resp = DispatchRepository.updateSetting(event!!);
            resp?.apply {
                if(isSuccess) {
                    if(data?.isSuccessAndData() == true) {
                        view.updateSettingSuccess(resp?.data?.message, event);
                    } else {
                        view?.updateSettingFailed(resp?.data?.message, event);
                    }
                }
            }
            view?.hideDialogLoading();
        }
    }

    override fun getSetting(back : (HomeModelEvent?) ->Unit, failed : (String?) -> Unit) {
        mCoroutine.launch {
            val resp = DispatchRepository.querySetting();
            if(resp?.isSuccess && resp?.data!= null) {
                if(resp?.data?.isSuccessAndData() == true) {
                    back?.invoke(resp?.data?.data?.setting);
                } else {
                    failed?.invoke(resp?.data?.message);
                }
            }
        }
    }

}