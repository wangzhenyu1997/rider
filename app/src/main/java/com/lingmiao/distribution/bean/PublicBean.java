package com.lingmiao.distribution.bean;

/**
 * PublicBean
 *
 * @author yandaocheng <br/>
 * 基本Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class PublicBean {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //10.调度单列表刷新
    // 11.调度单详细刷新
    // 12.调相关订单数量刷新接口
    // 13.微信支付成功通知
    // 15.通知我的钱包数据刷新
    // 16.通知充值页面关闭
    // 20: 调度单列表：待取货
    // 21: 调度单列表：待送达
    public PublicBean(int code) {
        //10.拒绝接单通知列表刷新
        this.code = code;
    }

    public PublicBean(String message) {
        this.message = message;
    }
}
