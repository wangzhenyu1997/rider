package com.lingmiao.distribution.bean;

/**
 * BasicBean
 *
 * @author yandaocheng <br/>
 * 基本Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class OrderNumBean {
    private String code;
    private String message;

    private OrderNumBean data;
    // 待取货
    private long pickupNum;
    // 待送达
    private long arriveNum;
    // 待接单
    private long receivedNum;

    public OrderNumBean getData() {
        return data;
    }

    public void setData(OrderNumBean data) {
        this.data = data;
    }

    public long getPickupNum() {
        return pickupNum;
    }

    public void setPickupNum(long pickupNum) {
        this.pickupNum = pickupNum;
    }

    public long getReceivedNum() {
        return receivedNum;
    }

    public void setReceivedNum(long receivedNum) {
        this.receivedNum = receivedNum;
    }

    public long getArriveNum() {
        return arriveNum;
    }

    public void setArriveNum(long arriveNum) {
        this.arriveNum = arriveNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
