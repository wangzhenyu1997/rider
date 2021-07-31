package com.james.common.utils.permission.callback

import android.content.Context
import com.blankj.utilcode.util.ToastUtils
import com.yanzhenjie.permission.runtime.Permission

/**
 * Author : Elson
 * Date   : 2020/8/4
 * Desc   :
 */
abstract class NotificationCallback(context: Context) : BaseCallback(context) {

    override fun applyPermissions(): Array<String> {
        return Permission.Group.STORAGE
    }

    override fun handleDenied(permissions: List<String?>?) {
        ToastUtils.showShort("请打开推送权限")
    }

    override fun handleAlwaysDeniedPermission() {
        ToastUtils.showShort("请打开推送权限")
    }
}