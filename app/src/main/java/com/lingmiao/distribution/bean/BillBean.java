package com.lingmiao.distribution.bean;

/**
 * 账单信息
 */
public class BillBean {
    private String code;
    private String message;
    private BillDataBean data;

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

    public BillDataBean getData() {
        return data;
    }

    public void setData(BillDataBean data) {
        this.data = data;
    }
}
