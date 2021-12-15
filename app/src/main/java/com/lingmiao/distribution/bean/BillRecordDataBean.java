package com.lingmiao.distribution.bean;

import java.util.List;

/**
 * TradeRecordDataBean
 * @author niuxinyu <br/>
 * 交易记录
 * 2020-07-22
 * 修改者，修改日期，修改内容
 */
public class BillRecordDataBean {
    private int totalCount;
    private List<BillRecordDataParam> records;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<BillRecordDataParam> getRecords() {
        return records;
    }

    public void setRecords(List<BillRecordDataParam> records) {
        this.records = records;
    }
}
