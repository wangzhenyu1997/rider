package com.lingmiao.distribution.bean;

import java.util.List;

/**
 * TradeRecordDataBean
 * @author niuxinyu <br/>
 * 交易记录
 * 2020-07-22
 * 修改者，修改日期，修改内容
 */
public class BillDetailDataBean {
    private String code;
    private String message;
    private BillDataParam data;

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

    public BillDataParam getData() {
        return data;
    }

    public void setData(BillDataParam data) {
        this.data = data;
    }
}
