package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.bean.HomeModelEvent
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2020/12/263:43 PM
Auther      : Fox
Desc        :
 **/
interface ITakingSettingPresenter : BasePresenter{

    fun updateSetting(data: HomeModelEvent?);

    fun getSetting(back : (HomeModelEvent?) ->Unit, failed : (String?) -> Unit);

    interface View : BaseView {

        fun updateSettingSuccess(str : String?, data: HomeModelEvent?);
        fun updateSettingFailed(str : String?, data: HomeModelEvent?);
    }

}