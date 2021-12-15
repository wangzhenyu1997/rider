package com.lingmiao.distribution.bean;

/**
 * LoginBean
 *
 * @author xiayang <br/>
 * 登录Bean类
 * 2019-06-04
 * 修改者，修改日期，修改内容
 */
public class WalletDataParam {
    private String id;//"a0f9ae8fb831c7818f60c8db73ff4f17",
    private String memberId;//"ab48045162ad9f4d7ba1269920a0bf4a",
    private String accountType;//3,
    private String accountTypeName;//"积分账户",
    private String nature;//1,
    private String totalAmount;//0,
    private String balanceAmount;//0,
    private String freezeAmount;//0,
    private String status;//1

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(String freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
