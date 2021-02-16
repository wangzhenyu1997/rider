package com.lingmiao.distribution.bean;

/**
 * BaseBean
 *
 * @author yandaocheng <br/>
 * 个人资料Bean类
 * 2020-07-17
 * 修改者，修改日期，修改内容
 */
public class PersonalBean {

    private boolean success;//": false,
    private String code;//": "100000",
    private String message;//": "当前登陆用户获取失败，token为空!",
    private PersonalDataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public PersonalDataBean getData() {
        return data;
    }

    public void setData(PersonalDataBean data) {
        this.data = data;
    }
}
