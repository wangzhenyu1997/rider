package com.lingmiao.distribution.bean;

import java.util.List;

/**
 * HomeBean
 *
 * @author yandaocheng <br/>
 * 首页Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class HomeBean {
    private String code;
    private String message;
    private HomeBean data;
    private int totalCount;
    private List<HomeParam> records;

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

    public HomeBean getData() {
        return data;
    }

    public void setData(HomeBean data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<HomeParam> getRecords() {
        return records;
    }

    public void setRecords(List<HomeParam> records) {
        this.records = records;
    }
}
