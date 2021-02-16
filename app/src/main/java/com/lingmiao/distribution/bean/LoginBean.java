package com.lingmiao.distribution.bean;
/**
 * LoginBean
 * @author xiayang <br/>
 * 登录Bean类
 * 2019-06-04
 * 修改者，修改日期，修改内容
 */
public class LoginBean {
    private String code;
    private String message;

    private LoginBean data;
    private String token;
    private String memberId;

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

    public LoginBean getData() {
        return data;
    }

    public void setData(LoginBean data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public static LoginBean convert(com.lingmiao.distribution.ui.login.bean.LoginBean bean) {
        LoginBean data = new LoginBean();
        data.memberId = bean.getMemberId();
        data.token = bean.getToken();
        return data;
    }

    public com.lingmiao.distribution.ui.login.bean.LoginBean convert() {
        com.lingmiao.distribution.ui.login.bean.LoginBean data = new com.lingmiao.distribution.ui.login.bean.LoginBean();
        data.setMemberId(memberId);
        data.setToken(token);
        return data;
    }

}
