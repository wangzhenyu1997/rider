package com.lingmiao.distribution.ui.main.presenter.impl

import com.fisheagle.mkt.base.UserManager
import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.ui.main.api.DispatchRepository
import com.lingmiao.distribution.ui.main.presenter.IDispatchOptionPresenter
import com.lingmiao.distribution.ui.main.presenter.IDispatchTabPresenter
import com.lingmiao.distribution.ui.main.presenter.ITakingSettingPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
class DispatchTabPreImpl(private var view: IDispatchTabPresenter.View) :
    BasePreImpl(view),
    IDispatchTabPresenter {

    private val dispatchOptionPresenter: IDispatchOptionPresenter by lazy {
        DispatchOptionPreImpl(view);
    }

    private val takingPresenter: ITakingSettingPresenter by lazy {
        TakingSettingPreImpl(view)
    }

    override fun loadTabNumber(event : HomeModelEvent) {
        mCoroutine.launch {
//            view?.showDialogLoading()

            val resp = DispatchRepository.loadDispatchNumber(null, event);
            handleResponse(resp, {
                if(it?.isSuccessAndData()) {
                    view?.loadTabNumberSuccess(it?.data);
                }
            }, {

            })
//            view?.hideDialogLoading()
        }
    }

    override fun updateWorkStatus(status: Int) {
        mCoroutine.launch {
            view.showDialogLoading()

            val resp = DispatchRepository.updateWorkStatus(status);
            handleResponse(resp) {
                view.updateWorkStatusSuccess();
            }

            view.hideDialogLoading()
        }
    }

    override fun getModelData(): HomeModelEvent {
        return UserManager.getTakingModel();
    }

    // 接单
    override fun sureOrder(
        id: String,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        dispatchOptionPresenter?.sureOrder(id, successCallback, failedCallback);
    }

    // 抢单
    override fun takeOrder(
        id: String,
        successCallback: (DataVO<Unit>) -> Unit,
        failedCallback: () -> Unit
    ) {
        dispatchOptionPresenter?.takeOrder(id, successCallback, failedCallback);
    }

    override fun loadDispatchData(id: String) {
        mCoroutine.launch {
            val resp = DispatchRepository.queryDispatchById(id);
            if (resp.isSuccess && resp?.data != null && resp?.data?.data != null) {
                view?.loadedDispatchData(resp?.data?.data!!);
            }
        }
    }

    override fun updateSetting(data: HomeModelEvent?) {
        takingPresenter?.updateSetting(data);
    }

    override fun getSetting(back: (HomeModelEvent?) -> Unit, failed: (String?) -> Unit) {
        takingPresenter?.getSetting(back, failed);
    }

}