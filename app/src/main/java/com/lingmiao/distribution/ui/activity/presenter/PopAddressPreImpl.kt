package com.lingmiao.distribution.ui.activity.presenter

import android.content.Context
import android.view.View
import com.amap.api.mapcore.util.it
import com.blankj.utilcode.util.ResourceUtils
import com.fisheagle.mkt.base.UserManager
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.distribution.common.commonpop.bean.RegionVo
import com.lingmiao.shop.business.commonpop.adapter.DefaultItemAdapter
import com.lingmiao.shop.business.commonpop.pop.AbsThreeItemPop
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author elson
 * @date 2020/7/16
 * @Desc 商品分类
 */
class PopAddressPreImpl() : BasePreImpl(null) {

    private var categoryName: String? = null
    private var addressPop: AbsThreeItemPop<RegionVo>? = null

    private var lv1Cache: MutableList<RegionVo> = mutableListOf()
    private var lv2CacheMap: HashMap<String, List<RegionVo>> = HashMap()
    private var lv3CacheMap: HashMap<String, List<RegionVo>> = HashMap()
    private var mList : MutableList<RegionVo> = mutableListOf();

    fun showAddressPop(context: Context, callback: (String?, List<RegionVo>?)-> Unit) {
        val regionsType = object : TypeToken<List<RegionVo>>(){}.type;
        mList = Gson().fromJson<MutableList<RegionVo>>(ResourceUtils.readAssets2String("areas.json"), regionsType);
        showAddressPop(context, mList, null, callback);
    }

    fun showAddressPop(
        context: Context,
        list: List<RegionVo>,
        old : List<RegionVo>?,
        callback: (String?, List<RegionVo>?)-> Unit)
    {
        var l1Adapter = DefaultItemAdapter<RegionVo>();
        var l2Adapter = DefaultItemAdapter<RegionVo>();
        var l3Adapter = DefaultItemAdapter<RegionVo>();

        addressPop = object : AbsThreeItemPop<RegionVo>(context, "请选择省市区"){

            override fun getAdapter1(): DefaultItemAdapter<RegionVo> {
                return l1Adapter;
            }

            override fun getAdapter2(): DefaultItemAdapter<RegionVo> {
                return l2Adapter;
            }

            override fun getAdapter3(): BaseQuickAdapter<RegionVo, BaseViewHolder> {
                return l3Adapter;
            }

            override fun getLevel(): Int {
                return LEVEL_3;
            }

            override fun getData2(data1: RegionVo): List<RegionVo> {
                return mutableListOf();
            }

            override fun getData3(data2: RegionVo): List<RegionVo> {
                return mutableListOf();
            }

        }.apply {
            lv1Callback = {
                mList.clear();
                mList.add(it);
                categoryName = it.localName;

                l1Adapter.setSelectedStr(it.localName);
                it.children?.toList()?.let { it1 -> addressPop?.setLv2Data(it1) };
            }
            lv2Callback = {
                if(mList.size <= 1) {
                    mList.add(it);
                } else {
                    mList[1] = it;
                }
//
                l2Adapter.setSelectedStr(it.localName);
                if(it.children == null || it.children?.size == 0) {
                    callback.invoke(categoryName, mList);
                    dismiss();
                } else {
                    categoryName = "${categoryName}/${it.localName}"
                    it.children?.toList()?.let { it1 ->
                        addressPop?.setLv3Data(it1)
                    }
                }
            }
            lv3Callback = {
                if(mList.size <= 2) {
                    mList.add(it);
                } else {
                    mList[2] = it;
                }
                l3Adapter.setSelectedStr(it.localName);
                categoryName = "${categoryName}/${it.localName}"
                callback.invoke(categoryName, mList)
                dismiss();
            }
        }
        addressPop?.setLv1Data(list);
        if(old == null || old.isEmpty()) {
            addressPop?.showPopupWindow()
            return;
        }
        var first = old.get(0);
        val _firstList =  list?.filter { it?.id == first.id };
//        if(old.isNotEmpty()) {
//            var first = old.get(0);
//            val _list =  list?.filter { it?.id == first.id };
//            addressPop?.setLv2Data(_list);
//        }
        if(old.size > 2) {
            var second = old.get(1);
            var _secondList =  getListByName(list, first.localName);
            if (_secondList != null) {
                addressPop?.setLv2Data(_secondList);


                var _thirdList =  getListByName(_secondList, second.localName);
                _thirdList?.let {
                    addressPop?.setLv3Data(it);
                }
            };

            l1Adapter.setSelectedStr(old.get(0).localName);
            l2Adapter.setSelectedStr(old.get(1).localName);
            l3Adapter.setSelectedStr(old.get(2).localName);
        }


//        addressPop?.setPopTitle()
        addressPop?.showPopupWindow("请选择省市区")
    }

    fun getListByName(list : List<RegionVo>, first : String?, name : String?): List<RegionVo>? {
        if(name?.isNullOrBlank() == true) {
            return list;
        }
        list.forEachIndexed { index, regionVo ->
            if(regionVo.localName?.indexOf(first?:"")?:0 > -1) {
                val _list = regionVo.children?.toList();
                if(_list != null) {
                    return getListByName(_list, name);
                }
                return listOf<RegionVo>();
            }
        }
        return listOf<RegionVo>();
    }

    fun getListByName(list : List<RegionVo>, name : String?): List<RegionVo>? {
        if(name?.isNullOrBlank() == true) {
            return list;
        }
        list.forEachIndexed { index, regionVo ->
            if(regionVo.localName?.indexOf(name?:"")?:0 > -1) {
                val _list = regionVo.children?.toList();
                return _list;
            }
        }
        return listOf<RegionVo>();
    }

    override fun onDestroy() {
        lv1Cache.clear()
        lv2CacheMap.clear()
        lv3CacheMap.clear()
        super.onDestroy()
    }
}