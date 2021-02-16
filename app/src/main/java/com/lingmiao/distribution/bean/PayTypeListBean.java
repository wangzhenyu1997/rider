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
public class PayTypeListBean {
    private String code;
    private String message;
    private PayTypeListDataBean data;

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

    public PayTypeListDataBean getData() {
        return data;
    }

    public void setData(PayTypeListDataBean data) {
        this.data = data;
    }
}
