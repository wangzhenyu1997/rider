package com.lingmiao.distribution.bean;
/**
 * TradeRecordBean
 * @author niuxinyu <br/>
 * 交易记录
 * 2019-06-04
 * 修改者，修改日期，修改内容
 */
public class BillRecordDataParam {
    private String billId;//业务单据id，后面查询详情时使用
    private String billNo;//单号
    private String billType;//单据类型
    private String totalCost;//费用合计
    private String status;//状态
    private String createTime;//创建时间

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
