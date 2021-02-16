package com.lingmiao.distribution.event;

import com.lingmiao.distribution.bean.HomeTwoParam;

import java.io.Serializable;
import java.util.List;

public class ScanList implements Serializable {
    private List<HomeTwoParam> list;

    public List<HomeTwoParam> getList() {
        return list;
    }

    public void setList(List<HomeTwoParam> list) {
        this.list = list;
    }

    public ScanList(List<HomeTwoParam> list) {
        this.list = list;
    }

}
