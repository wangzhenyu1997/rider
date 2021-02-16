package com.lingmiao.distribution.bean;

import java.util.List;

/**
 * BasicBean
 *
 * @author yandaocheng <br/>
 * 共用Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class BasicListBean {
    private String code;
    private String message;
    private List<BasicListParam> data;

    public List<BasicListParam> getData() {
        return data;
    }

    public void setData(List<BasicListParam> data) {
        this.data = data;
    }

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
}
