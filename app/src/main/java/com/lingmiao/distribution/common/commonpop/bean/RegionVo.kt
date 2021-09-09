package com.lingmiao.distribution.common.commonpop.bean

import com.google.gson.annotations.SerializedName
import com.lingmiao.shop.business.commonpop.bean.ItemData
import java.io.Serializable
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lingmiao.distribution.common.commonpop.adapter.AreasAdapter

/**
Create Date : 2021/9/911:28 下午
Auther      : Fox
Desc        :
 **/

data class Region(
    @SerializedName("children")
    var children: ArrayList<Region>? = null,
    @SerializedName("name")
    var name: String? = ""
) : Serializable {

}

/**
 * 指定配送区域
 */
data class Item(
    /**
     * 首重／首件
     */
    @SerializedName("first_company")
    var firstCompany: String? = "",
    /**
     * 运费
     */
    @SerializedName("first_price")
    var firstPrice: String? = "",
    /**
     * 续重／需件
     */
    @SerializedName("continued_company")
    var continuedCompany: String? = "",
    /**
     * 续费
     */
    @SerializedName("continued_price")
    var continuedPrice: String? = "",
    /**
     * 地区‘，‘分隔   示例参数：北京，山西，天津，上海
     */
    @SerializedName("area")
    var area: String? = "",
    /**
     * 地区id‘，‘分隔  示例参数：1，2，3，4
     */
    @SerializedName("area_id")
    var areaId: String? = "",
    @SerializedName("regions")
    var regions: List<Region>? = listOf(),
    var parsedArea : Map<String, Map<String, Any>> ? = null,
    var selectedIds : MutableList<String> ? = arrayListOf()
) : Serializable {

    fun getAreaStr() : String {
        val str = StringBuilder();
        regions?.forEachIndexed { index : Int, it : Region ->
            if(index > 0 && index < regions?.size ?: 0) {
                str.append("、")
            }
            str.append(it?.name);
            it?.children?.forEachIndexed { cIndex : Int, cItem : Region ->
                if(cIndex == 0 && it?.children?.size ?: 0 > 0) {
                    str.append("(");
                }
                str.append(cItem?.name);
                if(it?.children?.size == cIndex +1) {
                    str.append(")");
                }
            }
        }
        return str.toString();
    }

    fun existId(id : Int?) : Boolean {
        return selectedIds?.contains(String.format("%s", id)) ?: false;
    }

}

data class RegionVo(
    @SerializedName("children")
    var children: MutableList<RegionVo>? = mutableListOf(),
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("level")
    var level: Int? = 0,
    @SerializedName("local_name")
    var localName: String? = "",
    @SerializedName("parent_id")
    var parentId: Int? = 0,
    @SerializedName("selected_all")
    var selectedAll : Boolean ? = false,
    var isCheck : Boolean = false,
    var pPosition : Int ? = -1,
    var cPosition : Int ? = -1
) : AbstractExpandableItem<RegionVo>(), MultiItemEntity, ItemData, Serializable {

    override fun getLevel(): Int {
        return (level ?: 1) - 1;
    }

    fun isLastNode() : Boolean {
        return getLevel() == AreasAdapter.TYPE_AREA;
    }

    fun isCityLevel() : Boolean {
        return getLevel() == AreasAdapter.TYPE_LEVEL_1;
    }

    fun isProvinceLevel() : Boolean {
        return getLevel() == AreasAdapter.TYPE_LEVEL_0;
    }

    override fun getItemType(): Int {
        return getLevel();
    }

    fun shiftAllCheck(flag : Boolean) {
        when(getLevel()) {
            AreasAdapter.TYPE_LEVEL_0 -> {
                isCheck = flag;
                children?.apply {
                    forEach{ it : RegionVo ->
                        it.shiftAllCheck(flag);
                    }
                }
            }
            AreasAdapter.TYPE_LEVEL_1 -> {
                isCheck = flag;
                children?.apply {
                    forEach { area : RegionVo ->
                        area.shiftAllCheck(flag);
                    }
                }
            }
            AreasAdapter.TYPE_AREA -> {
                isCheck = flag;
            }
            else -> {

            }
        }
    }

    fun isCheckAll() : Boolean {
        var isCheckAll = true;
        when(getLevel()) {
            AreasAdapter.TYPE_LEVEL_0 -> {
                var isCheckAll = true;
                children?.apply {
                    forEach { c: RegionVo ->
                        if(!c?.isCheckAll()) {
                            isCheckAll = false;
                        }
                    }
                }
                return isCheckAll;
            }
            AreasAdapter.TYPE_LEVEL_1 -> {
                children?.apply {
                    forEach { a : RegionVo ->
                        if(!a?.isCheck) {
                            isCheckAll = false;
                        }
                    }
                }
            }
            AreasAdapter.TYPE_AREA -> {
                isCheckAll = isCheck;
            }
            else -> {

            }
        }
        return isCheckAll;
    }

    fun addItem(item: Item?, mTempArea: TempArea?) {
        if(item?.existId(id) == true) {
            shiftAllCheck(true);
        }
        when(getLevel()) {
            AreasAdapter.TYPE_LEVEL_0 -> {
                children?.apply {
                    forEach{ city : RegionVo ->
//                        LogUtils.dTag("addItem " + city?.level + " : " + city?.localName);
//                        addSubItem(city);
//                        city?.addItem(item, mTempArea);
                        if(item?.existId(city?.id) == true) {
                            city?.isCheck = true;
                            city?.shiftAllCheck(true);
                            addSubItem(city);
                            city?.addItem(item, mTempArea);
                        } else {
                            if(mTempArea?.existId(city?.id) != true) {
                                addSubItem(city);
                                city?.addItem(item, mTempArea);
                            }
                        }
                    }
                }
            }
            AreasAdapter.TYPE_LEVEL_1 -> {
                children?.apply {
                    forEach { area : RegionVo ->
//                        LogUtils.dTag("addItem " + area?.level + " : " + area?.localName);
                        if(item?.existId(area?.id) == true) {
                            area?.isCheck = true;
                            area?.shiftAllCheck(true);
                            addSubItem(area);
                            area?.addItem(item, mTempArea);
                        } else {
                            if(mTempArea?.existId(area?.id) != true) {
                                addSubItem(area);
                                area?.addItem(item, mTempArea);
                            }
                        }
                    }
                }
            }
            AreasAdapter.TYPE_AREA -> {
//                LogUtils.dTag("addItem " + level + " : " + localName);
            }
            else -> {

            }
        }
    }

    override fun getIValue(): String? {
        return id?.toString();
    }

    override fun getIName(): String? {
        return localName;
    }

    override fun getIHint(): String? {
        return localName;
    }

    override fun isItemChecked(): Boolean? {
        return isCheck;
    }

    override fun shiftChecked(flag: Boolean?) {
        isCheck = flag ?: false;
    }
}
