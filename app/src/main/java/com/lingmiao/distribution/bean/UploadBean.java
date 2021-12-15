package com.lingmiao.distribution.bean;

/**
 * BasicBean
 *
 * @author yandaocheng <br/>
 * 基本Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class UploadBean {
    private String code;
    private String message;
    private UploadDataBean data;

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

    public UploadDataBean getData() {
        return data;
    }

    public void setData(UploadDataBean data) {
        this.data = data;
    }
}
