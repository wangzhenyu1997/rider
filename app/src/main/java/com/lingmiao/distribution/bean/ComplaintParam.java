package com.lingmiao.distribution.bean;

import java.util.List;

/**
 * ComplaintParam
 *
 * @author yandaocheng <br/>
 * 投诉记录类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class ComplaintParam {

    private int complaintObj;
    private String createTime;
    private String customerId;
    private String dispatchId;
    private String dispatchNo;
    private String explainReason;
    private String orderId;
    private String orderNo;
    private String riderId;
    private String type;
    private String typeName;
    private String urls;
    private String content;
    private String exceptionTypeName;

    public String createrName;
    public String title;
//    public String createTime;
//    public String content;

    public String getExceptionTypeName() {
        return exceptionTypeName;
    }

    public void setExceptionTypeName(String exceptionTypeName) {
        this.exceptionTypeName = exceptionTypeName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getComplaintObj() {
        return complaintObj;
    }

    public void setComplaintObj(int complaintObj) {
        this.complaintObj = complaintObj;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getExplainReason() {
        return explainReason;
    }

    public void setExplainReason(String explainReason) {
        this.explainReason = explainReason;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
