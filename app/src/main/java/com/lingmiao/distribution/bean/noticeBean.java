package com.lingmiao.distribution.bean;

/**
 * noticeBean
 *
 * @author yandaocheng <br/>
 * eventbus消息推送传值
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class noticeBean {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public noticeBean(String message) {
        this.message = message;
    }
}
