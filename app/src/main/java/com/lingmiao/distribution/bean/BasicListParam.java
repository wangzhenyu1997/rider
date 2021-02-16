package com.lingmiao.distribution.bean;

/**
 * BasicListParam
 *
 * @author yandaocheng <br/>
 * 共用Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class BasicListParam {
    private String label;
    private String value;
    private String id;
    private String title;
    private String content;
    private boolean selectState = false;            //默认未选择

    public boolean isSelectState() {
        return selectState;
    }

    public void setSelectState(boolean selectState) {
        this.selectState = selectState;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
