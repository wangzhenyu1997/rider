package com.lingmiao.distribution.bean;

import java.util.List;

/**
 * TradeRecordDataBean
 * @author niuxinyu <br/>
 * 交易记录
 * 2020-07-22
 * 修改者，修改日期，修改内容
 */
public class BillDataBean {
    private int totalCount;
    private List<BillDataParam> records;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<BillDataParam> getRecords() {
        return records;
    }

    public void setRecords(List<BillDataParam> records) {
        this.records = records;
    }
}
