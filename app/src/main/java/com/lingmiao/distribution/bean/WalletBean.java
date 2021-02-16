package com.lingmiao.distribution.bean;
/**
 * LoginBean
 * @author xiayang <br/>
 * 登录Bean类
 * 2019-06-04
 * 修改者，修改日期，修改内容
 */
public class WalletBean {
    private String code;
    private String message;
    private WalletDataBean data;

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

    public WalletDataBean getData() {
        return data;
    }

    public void setData(WalletDataBean data) {
        this.data = data;
    }


}
