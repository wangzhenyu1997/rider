package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.bean.PersonalDataBean
import com.lingmiao.distribution.bean.PersonalDataParam
import com.lingmiao.distribution.bean.UpdateBean
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2020/12/263:43 PM
Auther      : Fox
Desc        :
 **/
interface IVersionPresenter : BasePresenter{

    fun checkVersion(string: String);


    interface View : BaseView {

        fun checkVersionSuccess(data : UpdateBean);

    }

}