package com.lingmiao.distribution.bean;

import java.util.List;

/**
 * TradeRecordBean
 * @author niuxinyu <br/>
 * 交易记录
 * 2019-06-04
 * 修改者，修改日期，修改内容
 */
public class BillDataParam {
    private String id;//结算单id
    private String settleBillNo;//结算单号
    private String settleBillName;//结算单名称
    private String memberId;//会员id
    private String memberName;//会员名称
    private int billNum;//明细单据数量
    private String amount;//账单总金额
    private String startTime;//账单开始时间
    private String endTime;//账单结束时间
    private int status;//账单状态 1待确认 2待结算 3已结算
    private String createTime;//账单创建时间
    private List<BillItemDataParam> settleBillItemList;//结算单明细项

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSettleBillNo() {
        return settleBillNo;
    }

    public void setSettleBillNo(String settleBillNo) {
        this.settleBillNo = settleBillNo;
    }

    public String getSettleBillName() {
        return settleBillName;
    }

    public void setSettleBillName(String settleBillName) {
        this.settleBillName = settleBillName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getBillNum() {
        return billNum;
    }

    public void setBillNum(int billNum) {
        this.billNum = billNum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<BillItemDataParam> getSettleBillItemList() {
        return settleBillItemList;
    }

    public void setSettleBillItemList(List<BillItemDataParam> settleBillItemList) {
        this.settleBillItemList = settleBillItemList;
    }
}
