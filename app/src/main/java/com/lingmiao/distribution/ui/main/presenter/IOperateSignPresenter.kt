package com.lingmiao.distribution.ui.main.presenter

import com.lingmiao.distribution.base.bean.DataVO
import com.lingmiao.distribution.ui.common.bean.UploadDataBean
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2021/1/57:33 PM
Auther      : Fox
Desc        :
 **/
interface IOperateSignPresenter : BasePresenter {

    fun sign(id : String, urls : String, type : Int);

    fun batchSign(
        ids: ArrayList<String>?,
        urls : String,
        remarks : String,
        shortMessageFlag : Int
    )

    fun uploadFile(path : String);

    interface View : BaseView {

        fun signSuccess();

        fun onSignedFail();

        fun onUploadFileSuccess(path : String, item : UploadDataBean);
    }
}