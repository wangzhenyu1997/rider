package com.lingmiao.distribution.ui.main.event

import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.fragment.DispatchTabFragment

/**
 * 设置调单TAB
 */
class DispatchTabEvent(val status: Int = DispatchConstants.DISPATCH_STATUS_AGREEING) {

    /**
     * 出售中
     */
    fun getTabIndex(): Int {
        return getTabIndexByStatus(status);
    }

    companion object {
        /**
         * 为了扩展状态所在的tab index
         */
        fun getTabIndexByStatus(status: Int): Int {
            return when (status) {
                DispatchConstants.DISPATCH_STATUS_AGREEING -> 0
                DispatchConstants.DISPATCH_STATUS_TAKING -> 1
                DispatchConstants.DISPATCH_STATUS_DELIVERING -> 2
                else -> 0
            }
        }
    }
}