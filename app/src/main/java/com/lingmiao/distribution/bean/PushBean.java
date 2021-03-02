package com.lingmiao.distribution.bean;

import com.lingmiao.distribution.ui.main.bean.DispatchOrderItemBean;
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean;

/**
 * PushBean
 *
 * @author yandaocheng <br/>
 * 基本Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class PushBean {
    private String dispatchId;
    private int type;  //1牛骑士派单通知   2审核结果通知
    private int auditStatus;  //2审核通过   3审核不通过
    private DispatchOrderItemBean order;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public DispatchOrderItemBean getOrder() {
        return order;
    }

    public void setOrder(DispatchOrderItemBean order) {
        this.order = order;
    }
}
