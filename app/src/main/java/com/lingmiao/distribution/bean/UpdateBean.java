package com.lingmiao.distribution.bean;
/**
 * UpdateBean
 * @author xiayang <br/>
 * 在线升级Bean类
 * 2019-06-04
 * 修改者，修改日期，修改内容
 */
public class UpdateBean {
    private String code;
    private String message;

    private UpdateBean data;
    private boolean needUpgrade ;
    private boolean castUpdate ;
    private String upgradeContent;
    private String downloadUrl  ;

    public String getUpgradeContent() {
        return upgradeContent;
    }

    public void setUpgradeContent(String upgradeContent) {
        this.upgradeContent = upgradeContent;
    }

    public boolean isCastUpdate() {
        return castUpdate;
    }

    public void setCastUpdate(boolean castUpdate) {
        this.castUpdate = castUpdate;
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

    public UpdateBean getData() {
        return data;
    }

    public void setData(UpdateBean data) {
        this.data = data;
    }

    public boolean isNeedUpgrade() {
        return needUpgrade;
    }

    public void setNeedUpgrade(boolean needUpgrade) {
        this.needUpgrade = needUpgrade;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
