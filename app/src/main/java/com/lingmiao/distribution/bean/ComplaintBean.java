package com.lingmiao.distribution.bean;

import java.util.List;

/**
 * ComplaintBean
 *
 * @author yandaocheng <br/>
 * 投诉记录类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class ComplaintBean {
    private String code;
    private String message;
    private ComplaintBean data;
    private int totalCount;
    private List<ComplaintParam> records;

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

    public ComplaintBean getData() {
        return data;
    }

    public void setData(ComplaintBean data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ComplaintParam> getRecords() {
        return records;
    }

    public void setRecords(List<ComplaintParam> records) {
        this.records = records;
    }
}
