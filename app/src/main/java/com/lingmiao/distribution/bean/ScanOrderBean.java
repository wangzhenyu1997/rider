package com.lingmiao.distribution.bean;

import java.io.Serializable;

public class ScanOrderBean implements Serializable {

    private String code;
    private String message;
    private HomeTwoParam data;

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

    public HomeTwoParam getData() {
        return data;
    }

    public void setData(HomeTwoParam data) {
        this.data = data;
    }

    public boolean isResultSuccess() {
        return code.equals("0");
    }
}
