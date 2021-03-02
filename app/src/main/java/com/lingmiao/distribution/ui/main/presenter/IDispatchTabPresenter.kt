package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.ui.main.bean.DispatchNumberBean
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.james.common.base.BasePresenter

/**
Create Date : 2020/12/284:33 PM
Auther      : Fox
Desc        :
 **/
interface IDispatchTabPresenter : BasePresenter, ITakingSettingPresenter {

    fun loadTabNumber(event : HomeModelEvent);

    fun updateWorkStatus(status : Int);

    fun getModelData() : HomeModelEvent;

    fun sureOrder(id : String, successCallback: (DataVO<Unit>) -> Unit, failedCallback: () -> Unit)

    fun takeOrder(id : String, successCallback: (DataVO<Unit>) -> Unit, failedCallback: () -> Unit)

    fun loadDispatchData(id : String);

    interface View : ITakingSettingPresenter.View {

        fun loadTabNumberSuccess(data : DispatchNumberBean?);

        fun updateWorkStatusSuccess();

        fun loadedDispatchData(data : DispatchOrderRecordBean);
    }

}