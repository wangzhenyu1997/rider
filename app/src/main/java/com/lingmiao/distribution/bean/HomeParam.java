package com.lingmiao.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * HomeParam
 *
 * @author yandaocheng <br/>
 * 首页Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class HomeParam implements Serializable {

    private String id;
    private int dispatchStatus;         //调度单状态
    private String riderId;
    private String riderName;
    private List<HomeTwoParam> orderList;

    public String totalCost;        //总计
    public String tipCost;          //小费
    public String rewardCost;          //奖励
    public String goodsInfo;          //商品信息

    public List<HomeTwoParam> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<HomeTwoParam> orderList) {
        this.orderList = orderList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(int dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public HomeTwoParam getFirstOrder() {
        return getOrderCount() <= 0 ? null : orderList.get(0);
    }

    public int getOrderCount() {
        return orderList == null ? 0 : orderList.size();
    }

    public String getFirstOrderConsignerPhone() {
        return getFirstOrder() == null ? "" : getFirstOrder().consignerPhone;
    }
}
