package com.lingmiao.distribution.bean;
/**
 * TradeRecordBean
 * @author niuxinyu <br/>
 * 交易记录
 * 2019-06-04
 * 修改者，修改日期，修改内容
 */
public class TradeRecordDataParam {
    private String serialNo;//交易流水号
    private String tradeVoucherNo;//业务单号
    private String memberName;//交易会员姓名
    private String otherMemberName;//交易对方会员姓名
    private String tradeTypeName;//交易类型名称
    private String tradeChannel;//交易渠道
    private String amount;//交易金额
    private String tradeContent;//交易内容
    private int fundFlowType;//资金流向 10收 20支
    private String tradeStatus;//交易状态
    private String tradeTime;//交易时间

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTradeVoucherNo() {
        return tradeVoucherNo;
    }

    public void setTradeVoucherNo(String tradeVoucherNo) {
        this.tradeVoucherNo = tradeVoucherNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getOtherMemberName() {
        return otherMemberName;
    }

    public void setOtherMemberName(String otherMemberName) {
        this.otherMemberName = otherMemberName;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }

    public String getTradeChannel() {
        return tradeChannel;
    }

    public void setTradeChannel(String tradeChannel) {
        this.tradeChannel = tradeChannel;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTradeContent() {
        return tradeContent;
    }

    public void setTradeContent(String tradeContent) {
        this.tradeContent = tradeContent;
    }

    public int getFundFlowType() {
        return fundFlowType;
    }

    public void setFundFlowType(int fundFlowType) {
        this.fundFlowType = fundFlowType;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }
}
