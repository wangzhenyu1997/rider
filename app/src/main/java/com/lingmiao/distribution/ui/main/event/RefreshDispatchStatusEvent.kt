package com.lingmiao.distribution.ui.main.event

/**
Create Date : 2020/12/284:57 PM
Auther      : Fox
Desc        : 刷新单个调度单列表
 **/
class RefreshDispatchStatusEvent(var status: Int = 0) {

    fun isRefresh(currentStatus: Int?): Boolean {
        return status == currentStatus;// status == 0 ||
    }

}