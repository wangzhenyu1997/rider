package com.lingmiao.distribution.bean;

/**
 * WeixinBean
 *
 * @author yandaocheng <br/>
 * 微信Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class WeixinBean {
    private String noncestr;
    private String appid;
    private String sign;
    private String partnerid;
    private String prepayid;
    private String timestamp;

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
