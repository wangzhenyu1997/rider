package com.lingmiao.distribution.ui.main.bean

import com.lingmiao.distribution.ui.main.fragment.DispatchTabFragment

/**
Create Date : 2021/1/75:16 PM
Auther      : Fox
Desc        :
 **/
interface DispatchConstants {

    companion object {

        const val TYPE_DISPATCH = 1;

        const val TYPE_ORDER = 2;

        const val TYPE_ORDER_BATCH = 3;



        // 待接单
        const val DISPATCH_STATUS_VIE = 0;
        // 待接单
        const val DISPATCH_STATUS_AGREEING = DISPATCH_STATUS_VIE+1;
        // 待取货
        const val DISPATCH_STATUS_TAKING = DISPATCH_STATUS_AGREEING + 1;
        // 待送达
        const val DISPATCH_STATUS_DELIVERING = DISPATCH_STATUS_TAKING + 1;

        const val DISPATCH_LIST_HISTORY = DISPATCH_STATUS_DELIVERING + 1;

        fun newInstance(): DispatchTabFragment {
            return DispatchTabFragment()
        }

        fun isVieTab(type : Int) : Boolean {
            return type == DISPATCH_STATUS_VIE;
        }

        fun isAgreeingTab(type : Int) : Boolean {
            return type == DISPATCH_STATUS_AGREEING;
        }

        fun isTakingTab(type : Int) : Boolean {
            return type == DISPATCH_STATUS_TAKING;
        }

        fun isDeliveringTab(type : Int) : Boolean {
            return type == DISPATCH_STATUS_DELIVERING;
        }

        fun isHistoryTab(type : Int) : Boolean {
            return type == DISPATCH_LIST_HISTORY;
        }
    }

}