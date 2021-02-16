package com.lingmiao.distribution.bean;
/**
 * TradeRecordBean
 * @author niuxinyu <br/>
 * 交易记录
 * 2019-06-04
 * 修改者，修改日期，修改内容
 */
public class BillItemDataParam {
    private String id;//明细项id
    private String settleBillId;//结算单id
    private String itemBillId;//业务单据id
    private String itemBillNo;//业务单据编号
    private int itemType;//类型
    private String itemTypeName;//类型名称"
    private String amount;//明细项金额
    private String occurTime;//业务发生时间
    private String memberId;//会员id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSettleBillId() {
        return settleBillId;
    }

    public void setSettleBillId(String settleBillId) {
        this.settleBillId = settleBillId;
    }

    public String getItemBillId() {
        return itemBillId;
    }

    public void setItemBillId(String itemBillId) {
        this.itemBillId = itemBillId;
    }

    public String getItemBillNo() {
        return itemBillNo;
    }

    public void setItemBillNo(String itemBillNo) {
        this.itemBillNo = itemBillNo;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
