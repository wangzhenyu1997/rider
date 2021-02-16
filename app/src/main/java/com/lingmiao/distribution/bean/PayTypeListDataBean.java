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
public class PayTypeListDataBean {
    private List<PayTypeListDataParam> records;//":Array[1], " +
    private int totalCount;//":1,

    public List<PayTypeListDataParam> getRecords() {
        return records;
    }

    public void setRecords(List<PayTypeListDataParam> records) {
        this.records = records;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
