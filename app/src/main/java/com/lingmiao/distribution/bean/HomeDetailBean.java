package com.lingmiao.distribution.bean;

/**
 * HomeDetailBean
 *
 * @author yandaocheng <br/>
 * 首页Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class HomeDetailBean {

    private String code;
    private String message;
    private HomeDetailParam data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HomeDetailParam getData() {
        return data;
    }

    public void setData(HomeDetailParam data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
