package com.lingmiao.distribution.bean;

/**
 * BasicBean
 *
 * @author yandaocheng <br/>
 * 基本Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class UploadDataBean {
    private String  id;
    private String url;
    private String path;

    public String getImageUrl() {
        if(path == null || path.length() == 0) {
            return url;
        }
        return path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
