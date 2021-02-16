package com.lingmiao.distribution.bean;

/**
 * OcrBean
 *
 * @author yandaocheng <br/>
 * 基本Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class OcrBean {
    private String code;
    private String message;
    private OcrDataBean data;

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

    public OcrDataBean getData() {
        return data;
    }

    public void setData(OcrDataBean data) {
        this.data = data;
    }
}
