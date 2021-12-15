package com.lingmiao.distribution.bean;
/**
 * TradeRecordBean
 * @author niuxinyu <br/>
 * 交易记录
 * 2019-06-04
 * 修改者，修改日期，修改内容
 */
public class BillRecordBean {
    private String code;
    private String message;

    private BillRecordDataBean data;

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

    public BillRecordDataBean getData() {
        return data;
    }

    public void setData(BillRecordDataBean data) {
        this.data = data;
    }
}
