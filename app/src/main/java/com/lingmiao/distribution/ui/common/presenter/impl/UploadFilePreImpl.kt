package com.lingmiao.distribution.ui.common.presenter.impl

import com.fisheagle.mkt.base.CommonRepository
import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.ui.common.bean.UploadDataBean
import com.lingmiao.distribution.ui.common.presenter.IUploadFilePresenter
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import kotlinx.coroutines.launch
import java.io.File

/**
Create Date : 2020/12/263:47 PM
Auther      : Fox
Desc        :
 **/
open class UploadFilePreImpl(var view : BaseView) : BasePreImpl(view),
    IUploadFilePresenter {

    override fun uploadFile(path : String, showLoading : Boolean, successCallback : (DataVO<UploadDataBean>?) -> Unit, failedCallback : (String?) -> Unit) {
        mCoroutine.launch {
            if(showLoading) {
                view?.showDialogLoading("正在上传图片...")
            }
            val resp = CommonRepository.uploadImage(File(path), true)
            if(resp?.isSuccess) {
                if(resp?.data?.isSuccessAndData() == true) {
                    successCallback?.invoke(resp?.data)
                } else {
                    failedCallback?.invoke(resp?.data?.message)
                }
            }
            if(showLoading) {
                view?.hideDialogLoading()
            }
        }
    }

}