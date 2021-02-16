package com.lingmiao.distribution.bean;

import java.util.List;

/**
 * PublicListBean
 *
 * @author yandaocheng <br/>
 * 公共列表Bean类
 * 2019-07-01
 * 修改者，修改日期，修改内容
 */
public class PublicListBean {
    private String code;
    private String message;
    private PublicListBean data;
    private int totalCount;
    private List<PublicListParam> records;

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

    public PublicListBean getData() {
        return data;
    }

    public void setData(PublicListBean data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<PublicListParam> getRecords() {
        return records;
    }

    public void setRecords(List<PublicListParam> records) {
        this.records = records;
    }
}
