package com.lingmiao.distribution.ui.main.bean
import android.util.Log
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.lingmiao.distribution.util.MathExtend
import com.lingmiao.distribution.util.PublicUtil
import java.io.Serializable


/**
Create Date : 2020/12/2810:37 AM
Auther      : Fox
Desc        :
 **/
class DispatchOrderRecordBean : Serializable, MultiItemEntity {
    @SerializedName("acceptTime")
    var acceptTime: String? = ""
    @SerializedName("arriveShopTime")
    var arriveShopTime: String? = ""
    @SerializedName("arriveStationTime")
    var arriveStationTime: Any? = Any()
    @SerializedName("compensateCost")
    var compensateCost: Double? = 0.0
    @SerializedName("createTime")
    var createTime: String? = ""
    @SerializedName("deliveryCost")
    var deliveryCost: Double? = 0.0
    @SerializedName("dispatchNo")
    var dispatchNo: String? = ""
    @SerializedName("dispatchStatus")
    var dispatchStatus: Int? = 0

    /**
     * 骑手类型 1司机(四轮) 2 骑手(两轮) '
     */
    @SerializedName("riderType")
    var riderType: Int? = 0
    @SerializedName("fineCost")
    var fineCost: Double? = 0.0
    @SerializedName("goodsInfo")
    var goodsInfo: String? = ""
    @SerializedName("goodsName")
    var goodsName: String? = ""
    @SerializedName("goodsPiece")
    var goodsPiece: Int? = 0
    @SerializedName("goodsType")
    var goodsType: Any? = Any()
    @SerializedName("goodsVolume")
    var goodsVolume: Int? = 0
    @SerializedName("goodsWeight")
    var goodsWeight: Int? = 0
    @SerializedName("id")
    var id: String? = ""
    @SerializedName("orderList")
    var orderList: List<DispatchOrderItemBean>? = listOf()
    @SerializedName("originDistance")
    var originDistance: String? = ""
    @SerializedName("pickupTime")
    var pickupTime: String? = ""
    @SerializedName("remarks")
    var remarks: Any? = Any()
    @SerializedName("rewardCost")
    var rewardCost: Double? = 0.0
    @SerializedName("riderId")
    var riderId: String? = ""
    @SerializedName("riderMobile")
    var riderMobile: String? = ""
    @SerializedName("riderName")
    var riderName: String? = ""
    @SerializedName("signedTime")
    var signedTime: String? = ""
    @SerializedName("tipCost")
    var tipCost: Double? = 0.0
    @SerializedName("totalCost")
    var totalCost: Double? = 0.0
    @SerializedName("vehiclePlate")
    var vehiclePlate: Int? = 0
    @SerializedName("vehicleType")
    var vehicleType: Int? = 0
    //默认为1 表示二轮
    var viewModelType : Int = 1
    @SerializedName("pickOrderFlag")
    val pickOrderFlag : String?=""
    // 业务字段
    var req : DispatchListReq?=null;

    companion object {
        // 已取消
        const val CANCEL = -1;
        // 创建调度单
        const val CREATE = 0;
        // 骑手拒绝
        const val REFUSE = 1;
        // 待骑手确认
        const val WAIT_ARRANGED = 2;
        // 待到店
        const val WAIT_ARRIVE_SHOP = 3;
        // 待取货
        const val WAIT_PICKUP = 4;
        // 待送达
        const val WAIT_ARRIVE_STATION = 5;
        // 待签收
        const val WAIT_SIGN = 6;
        // 已签收
        const val SIGNED = 7;
        // 签收失败
        const val SIGN_FAIL = 8;
        // 取货失败
        const val PICKUP_FAIL = 9;

        fun getDispatchStatusStr(status : Int): String {
            when (status) {
                CANCEL -> return "已取消"
                CREATE -> return "创建调度单"
                REFUSE -> return "骑手拒绝"
                WAIT_ARRANGED -> return "待骑手确认"
                WAIT_ARRIVE_SHOP -> return "待到店"
                WAIT_PICKUP -> return "待取货"
                WAIT_ARRIVE_STATION -> return "待送达"
                WAIT_SIGN -> return "待签收"
                SIGNED -> return "已签收"
                SIGN_FAIL -> return "签收失败"
                PICKUP_FAIL -> return "取货失败"
                else -> return "";
            }
        }

        fun isDeliveryStatus(status : Int) : Boolean {
            return status == WAIT_ARRIVE_STATION || status == WAIT_SIGN;
        }

        fun isTakingStatus(status : Int) : Boolean {
            return status == WAIT_ARRIVE_SHOP || status == WAIT_PICKUP
        }

        fun getOption(status : Int?) : DispatchOptionBean? {
            when (status) {
                CANCEL -> return null
                CREATE -> return null
                REFUSE -> return null
                WAIT_ARRANGED -> return DispatchOptionBean("确认接单", "拒绝接单")
                WAIT_ARRIVE_SHOP -> return DispatchOptionBean("到达取货点", "上报异常")
                WAIT_PICKUP -> return return DispatchOptionBean("取货成功", "取货失败")
                WAIT_ARRIVE_STATION -> return DispatchOptionBean("到达收货点", "上报异常")
                WAIT_SIGN -> return DispatchOptionBean("签收成功", "签收失败")
                SIGNED -> return DispatchOptionBean("评价客户", "投诉客户")
                SIGN_FAIL -> return null
                PICKUP_FAIL -> return null
                else -> return null
            }
        }

        fun getDispatchOption(status : Int?) : DispatchOptionBean? {
            when (status) {
                CANCEL -> return null
                CREATE -> return null
                REFUSE -> return null
                WAIT_ARRANGED -> return DispatchOptionBean("全部接单", "全部拒绝")
                WAIT_ARRIVE_SHOP -> return DispatchOptionBean("全部到达取货点", "全部上报异常")
                WAIT_PICKUP -> return return DispatchOptionBean("全部取货成功", "全部取货失败")
                WAIT_ARRIVE_STATION -> return DispatchOptionBean("全部送达收货点", "全部上报异常")
                WAIT_SIGN -> return DispatchOptionBean("全部拍照签收", "全部签收失败")
                SIGNED -> return DispatchOptionBean("全部评价客户", "全部投诉客户")
                SIGN_FAIL -> return null
                PICKUP_FAIL -> return null
                else -> return null
            }
        }
    }

    fun getOption() : DispatchOptionBean? {
        return getOption(dispatchStatus);
    }

    fun getDispatchOption() : DispatchOptionBean? {
        return getDispatchOption(dispatchStatus);
    }

    fun getDispatchStatusStr() : String {
        return getDispatchStatusStr(dispatchStatus!!);
    }

    fun getPriceDetailStr() : String {
        return String.format(
            "(包含小费<font color='#C73F4D'>¥%s</font>，奖励<font color='#C73F4D'>¥%s</font>)",
            MathExtend.round(tipCost!!, 2),
            MathExtend.round(rewardCost!!, 2)
        );
    }

    fun isTrucker() : Boolean {
        return riderType == 1;
    }

    override fun getItemType(): Int {
        return viewModelType;
    }

    fun getFirstOrder() : DispatchOrderItemBean? {
        return if(getOrderCount() == 0) null else orderList?.get(0);
    }

    fun getOrderCount() : Int {
        return orderList?.size ?: 0;
    }

    fun getConsignerAddress() : String {
        return getFirstOrder()?.getConsignerAddressStr()?:"";
    }

    fun getConsigneeAddress() : String {
        return getFirstOrder()?.getConsigneeAddressStr()?:"";
    }

    fun getVehicleTypeStr() : String {
        return ""
    }

    fun getVehiclePlateStr() : String {
        return ""
    }

    //获取接单时间
    fun getAcceptTimeStr() : String {
        return if(acceptTime == null || acceptTime?.length ?:0 == 0) "" else String.format("%s 接单", PublicUtil.isNull(acceptTime))
    }

}


data class DispatchOrderItemBean(
    /**
     * 自提点
     */
    @SerializedName("isSelfTake")
    var isSelfTake : Int?=0,
    /**
     * 收货客户
     */
    @SerializedName("consigneeCustomerMobile")
    var consigneeCustomerMobile: String? = "",
    @SerializedName("consigneeCustomerName")
    var consigneeCustomerName: String? = "",
    @SerializedName("consigneeName")
    var consigneeName: String? = "",
    @SerializedName("consigneePhone")
    var consigneePhone: String? = "",
    /**
     * 收货地址
     */
    @SerializedName("consigneeProvince")
    var consigneeProvince: String? = "",
    /**
     * 收货地址-市
     */
    @SerializedName("consigneeCity")
    var consigneeCity: String? = "",
    /**
     * 收货地址-区
     */
    @SerializedName("consigneeDistrict")
    var consigneeDistrict: String? = "",
    /**
     * 收货地址-其他
     */
    @SerializedName("consigneeStreet")
    var consigneeStreet: String? = "",
    /**
     * 收货地址-详细地址
     */
    @SerializedName("consigneeAddress")
    var consigneeAddress: String? = "",
    @SerializedName("consigneeLat")
    var consigneeLat: Double? = 0.0,
    @SerializedName("consigneeLng")
    var consigneeLng: Double? = 0.0,


    @SerializedName("consignerCustomerId")
    var consignerCustomerId: String? = "",
    @SerializedName("consignerCustomerMobile")
    var consignerCustomerMobile: String? = "",
    @SerializedName("consignerCustomerName")
    var consignerCustomerName: String? = "",
    @SerializedName("consignerName")
    var consignerName: String? = "",
    @SerializedName("consignerPhone")
    var consignerPhone: String? = "",
    @SerializedName("consignerProvince")
    var consignerProvince: String? = "",
    @SerializedName("consignerCity")
    var consignerCity: String? = "",
    @SerializedName("consignerDistrict")
    var consignerDistrict: String? = "",
    @SerializedName("consignerStreet")
    var consignerStreet: String? = "",
    @SerializedName("consignerAddress")
    var consignerAddress: String? = "",
    @SerializedName("consignerLat")
    var consignerLat: Double? = 0.0,
    @SerializedName("consignerLng")
    var consignerLng: Double? = 0.0,

    /**
     * 预约
     */
    @SerializedName("siteManName")
    var siteManName: String? = "",
    /**
     * other
     */
    @SerializedName("createTime")
    var createTime: String? = "",
    @SerializedName("createrId")
    var createrId: Any? = Any(),
    @SerializedName("customerRemark")
    var customerRemark: String? = "",
    @SerializedName("dispatchId")
    var dispatchId: String? = "",
    @SerializedName("dispatchNo")
    var dispatchNo: String? = "",
    @SerializedName("distance")
    var distance: Double? = 0.0,
    @SerializedName("estimateCost")
    var estimateCost: Double? = 0.0,
    @SerializedName("goodsName")
    var goodsName: String? = "",
    @SerializedName("goodsPiece")
    var goodsPiece: Int? = 0,
    @SerializedName("goodsType")
    var goodsType: Any? = Any(),
    @SerializedName("goodsVolume")
    var goodsVolume: Int? = 0,
    @SerializedName("goodsWeight")
    var goodsWeight: Int? = 0,
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("insuranceCost")
    var insuranceCost: Double? = 0.0,
    @SerializedName("isAppointment")
    var isAppointment: Int? = 0,
    @SerializedName("isCollecting")
    var isCollecting: Int? = 0,
    @SerializedName("isComment")
    var isComment: Int? = 0,
    @SerializedName("isDelete")
    var isDelete: Int? = 0,
    @SerializedName("labels")
    var labels: String? = "",
    @SerializedName("merchantId")
    var merchantId: String? = "",
    @SerializedName("orderNo")
    var orderNo: String? = "",
    @SerializedName("orderStatus")
    var orderStatus: Int? = 0,
    @SerializedName("orgId")
    var orgId: String? = "",
    @SerializedName("originDistance")
    var originDistance: String? = "",
    @SerializedName("planDeliveryTime")
    var planDeliveryTime: String? = "",
    @SerializedName("collectingFreightCost")
    var collectingFreightCost: Double? = 0.0,
    @SerializedName("collectingGoodsCost")
    var collectingGoodsCost: Double? = 0.0,
    @SerializedName("remarks")
    var remarks: String? = "",
    @SerializedName("showTimeRequire")
    var showTimeRequire: String? = "",
    @SerializedName("targetDistance")
    var targetDistance: String? = "",
    @SerializedName("timeRequire")
    var timeRequire: String? = "",
    @SerializedName("totalCost")
    var totalCost: Double? = 0.0,
    @SerializedName("updateTime")
    var updateTime: String? = "",
    @SerializedName("updaterId")
    var updaterId: Any? = Any(),
    @SerializedName("upsBillId")
    var upsBillId: String? = "",
    @SerializedName("upsBillNo")
    var upsBillNo: String? = "",
    @SerializedName("dispatchExceptionNum")
    var dispatchExceptionNum: Int? = 0,
    @SerializedName("complaintNum")
    var complaintNum: Int? = 0
) : Serializable {

    companion object {

        // 已取消
        const val CANCEL = -1;
        // 待派单
        const val WAIT_DISPATCH = 0;
        // 待重新派单
        const val WAIT_RE_DISPATCH = 1;
        // 待接单
        const val WAIT_ACCEPT = 2;
        // 待到店
        const val WAIT_ARRIVE_SHOP = 3;
        // 待取货
        const val WAIT_PICKUP = 4;
        // 待到达目的地
        const val WAIT_ARRIVE_STATION = 5;
        // 待签收
        const val WAIT_SIGN = 6;
        // 已签收
        const val SIGNED = 7;
        // 签收失败
        const val SIGN_FAIL = 8;
        // 取货失败
        const val PICKUP_FAIL = 9;

        /**
         * 待送达列表,调度单列表做拆分，拆分规则是一个调度单跟据自提点拆成多个列表
         * 如果：订单的状态>调度单状态
         * 调度单状态=待关达
         * 订单的状态=待签收
         * 此时，显示的是签收
         */
        fun isWaitSign(status : Int?) : Boolean {
            return WAIT_SIGN == status;
        }

        fun isTakingStatus(status : Int) : Boolean {
            return status == WAIT_ARRIVE_SHOP || status == WAIT_PICKUP
        }

        fun getOrderStatusStr(status : Int?): String {
            when (status) {
                CANCEL -> return "已取消"
                WAIT_DISPATCH -> return "待派单"
                WAIT_RE_DISPATCH -> return "待重新派单"
                WAIT_ACCEPT -> return "待接单"
                WAIT_ARRIVE_SHOP -> return "待到店"
                WAIT_PICKUP -> return "待取货"
                WAIT_ARRIVE_STATION -> return "待送达"
                WAIT_SIGN -> return "待签收"
                SIGNED -> return "已签收"
                SIGN_FAIL -> return "签收失败"
                PICKUP_FAIL -> return "取货失败"
                else -> return "";
            }
        }
    }

    fun getOrderStatusStr() : String {
        return getOrderStatusStr(orderStatus);
    }

    fun getConsignerAddressStr() : String {
        return PublicUtil.isNull(consignerProvince) +
                PublicUtil.isNull(consignerCity) +
                PublicUtil.isNull(consignerDistrict) +
                PublicUtil.isNull(consignerStreet) +
                PublicUtil.isNull(consignerAddress)
    }

    fun getConsigneeAddressStr() : String {
        return PublicUtil.isNull(consigneeProvince) +
                PublicUtil.isNull(consigneeCity) +
                PublicUtil.isNull(consigneeDistrict) +
                PublicUtil.isNull(consigneeStreet) +
                PublicUtil.isNull(consigneeAddress)
    }

    fun outOrderNoIsEmpty(): Boolean {
        return upsBillNo == null || upsBillNo!!.length == 0
    }

    fun getOutOrderNo(): String {
        return String.format("外部单号：%s", upsBillNo)
    }

    fun getPlanDeliveryTimeStr() : String {
        return if(planDeliveryTime == null || planDeliveryTime?.length ?:0 == 0) "立即配送" else String.format("%s 配送", PublicUtil.isNull(planDeliveryTime));
    }


}