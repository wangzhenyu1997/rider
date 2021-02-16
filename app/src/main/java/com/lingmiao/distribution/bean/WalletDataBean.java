package com.lingmiao.distribution.bean;
/**
 * LoginBean
 * @author xiayang <br/>
 * 登录Bean类
 * 2019-06-04
 * 修改者，修改日期，修改内容
 */
public class WalletDataBean {
    private WalletDataParam integralAccount;//":Object{...},
    private WalletDataParam depositAccount;//":Object{...},
    private WalletDataParam balanceAccount;//":Object{...}
    private int  bankCardNum;//":0,

    public WalletDataParam getIntegralAccount() {
        return integralAccount;
    }

    public void setIntegralAccount(WalletDataParam integralAccount) {
        this.integralAccount = integralAccount;
    }

    public WalletDataParam getDepositAccount() {
        return depositAccount;
    }

    public void setDepositAccount(WalletDataParam depositAccount) {
        this.depositAccount = depositAccount;
    }

    public WalletDataParam getBalanceAccount() {
        return balanceAccount;
    }

    public void setBalanceAccount(WalletDataParam balanceAccount) {
        this.balanceAccount = balanceAccount;
    }

    public int getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(int bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

}
