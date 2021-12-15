package com.lingmiao.distribution.bean;

import com.lingmiao.distribution.util.PublicUtil;

import java.io.Serializable;

/**
 * HomeTwoParam
 *
 * @author yandaocheng <br/>
 * 首页Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class HomeTwoParam implements Serializable, Cloneable {

    private String id;
    public String timeRequire;                  //时效要求
    public String showTimeRequire;
    public String consignerCustomerName;        //商家名称
    public String consignerProvince;
    public String consignerCity;
    public String consignerDistrict;
    public String consignerStreet;
    public String consignerAddress;             //商家地址
    public String consignerPhone;               //商家电话

    public String consigneeProvince;
    public String consigneeCity;
    public String consigneeDistrict;
    public String consigneeStreet;
    public String consigneeAddress;             //收货地址
    public String consigneeName;                //收货人姓名
    public String consigneePhone;               //收货人电话

    public String labels;                       //标签

    public String planDeliveryTime;             //计划配送时间
    public String orderNo;
    public String customerRemark;             //用户备注
    public String originDistance;             //到商家距离
    public String targetDistance;             //到用户距离

    public double consignerLat;              //发货人纬度
    public double consignerLng;
    public double consigneeLat;
    public double consigneeLng;

    public int isAppointment;                   //是否预约   1.已预约

//    public String consigneeName;                //下单用户

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String upsBillNo;
    public String upsBillId;

    public String consignerCustomerId;
    public String consignerCustomerMobile;
    public String consignerName;
    public String dispatchId;
    public String dispatchNo;
    public double distance;
    public double estimateCost;
    public String goodsName;
    public double goodsPiece;
//    public String goodsType;
//    public double goodsVolume;
//    public double goodsWeight;
    public double insuranceCost;
    public int isCollecting;
    public int isComment;
    public int isDelete;
    public String merchantId;
    public String remarks;
    // public String createTime;
    // public String updateTime
    // targetDistance
    // originDistance
    // orgId
    // collectingFreightCost
    // collectingGoodsCost
    public int orderStatus;
    public double totalCost;

    @Override
    public Object clone() {
        Object obj=null;
        //调用Object类的clone方法，返回一个Object实例
        try {
            obj= super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public String getOrderStatusName() {
        switch (orderStatus) {
            case -1:
                return "已取消";
            case 0:
                return "待派单";
            case 1:
                return "待重新派单";
            case 2:
                return "待接单";
            case 3:
                return "待到店";
            case 4:
                return "待取货";
            case 5:
                return "待到达";
            case 6:
                return "待签收";
            case 7:
                return "已签收";
            case 8:
                return "签收失败";
            case 9:
                return "取货失败";
            default:
                return "未知";
        }
    }

    public String getConsigneeFullAddress() {
        return String.format("%s%s%s%s%s",
                PublicUtil.isNull(consigneeProvince),
                PublicUtil.isNull(consigneeCity),
                PublicUtil.isNull(consigneeDistrict),
                PublicUtil.isNull(consigneeStreet),
                PublicUtil.isNull(consigneeAddress));
    }

    public String getConsignorFullAddress() {
        return String.format("%s%s%s%s%s",
                PublicUtil.isNull(consignerProvince),
                PublicUtil.isNull(consignerCity),
                PublicUtil.isNull(consignerDistrict) ,
                PublicUtil.isNull(consignerStreet),
                PublicUtil.isNull(consignerAddress));
    }

    public String getOriginDistanceName() {
        return originDistance == null || originDistance.length() == 0 ? "" : String.format("%s公里", PublicUtil.isNull(originDistance));
    }

    public String getTargetDistanceName() {
        return targetDistance == null || targetDistance.length() == 0 ? "" : String.format("%s公里", PublicUtil.isNull(targetDistance));
    }

    public boolean outOrderNoIsEmpty() {
        return upsBillNo == null || upsBillNo.length() == 0;
    }

    public String getOutOrderNo() {
        return String.format("外部单号：%s", upsBillNo);
    }

    public String getTimeRequireStr() {
        return timeRequire == null || timeRequire.length() == 0 || timeRequire.equals("null") ? "" : String.format("%s送达", timeRequire);
    }

    public String getTempAddress() {
        return String.format("%s%s%s%s%s", consignerProvince, consignerCity, consignerDistrict, consignerStreet, consignerAddress);
    }

}