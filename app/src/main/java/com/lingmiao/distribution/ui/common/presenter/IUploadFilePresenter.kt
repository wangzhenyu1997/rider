package com.lingmiao.distribution.ui.common.presenter

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.ui.common.bean.UploadDataBean
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2021/1/84:54 PM
Auther      : Fox
Desc        :
 **/
interface IUploadFilePresenter : BasePresenter {

    fun uploadFile(path : String, showLoading : Boolean, successCallback : (DataVO<UploadDataBean>?) -> Unit, failedCallback : (String?) -> Unit);

}