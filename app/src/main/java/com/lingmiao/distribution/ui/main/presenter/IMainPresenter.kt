package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.bean.PersonalDataParam
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2020/12/263:43 PM
Auther      : Fox
Desc        :
 **/
interface IMainPresenter : IVersionPresenter {

    fun getTabTitles() : Array<String>;

    fun getUser();

    interface View : IVersionPresenter.View {

        fun setUser(data : PersonalDataParam?);
    }

}