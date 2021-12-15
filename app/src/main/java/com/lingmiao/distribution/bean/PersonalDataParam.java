package com.lingmiao.distribution.bean;

/**
 * BaseBean
 *
 * @author yandaocheng <br/>
 * 个人资料Bean类
 * 2020-07-17
 * 修改者，修改日期，修改内容
 */
public class PersonalDataParam {
    private int auditStatus;//integer, optional): 审核状态 ,
    private String createTime;//string, optional),
    private String createrId;//string, optional),
    private String handIdCardUrl;//string, optional): 手持身份证url ,
    private String headImgUrl;//string, optional): 头像 ,
    private String id;//string, optional),
    private String idCard;//string, optional): 身份证号 ,
    private String idCardBackUrl;//string, optional): 身份证反面url ,
    private String idCardFrontUrl;//string, optional): 身份证正面url ,
    private String isDelete;//integer, optional),
    private String memberId;//string, optional): 会员id ,
    private String merchantId;//string, optional),
    private String mobile;//string, optional): 手机号 ,
    private String name;//string, optional): 骑手姓名 ,
    private String orgId;//string, optional),
    private String refuseReason;//string, optional): 拒绝原因 ,
    private String remarks;//string, optional),
    private String riderUrl;//string, optional): 骑手照片 ,
    private String status;//integer, optional): 状态 ,
    private String updateTime;//string, optional),
    private String updaterId;//string, optional)
    private String nickName;//昵称
    private String provinceName;
    private String cityName;
    private String districtName;

    private int workStatus;   //，0休息 1工作中

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getHandIdCardUrl() {
        return handIdCardUrl;
    }

    public void setHandIdCardUrl(String handIdCardUrl) {
        this.handIdCardUrl = handIdCardUrl;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCardBackUrl() {
        return idCardBackUrl;
    }

    public void setIdCardBackUrl(String idCardBackUrl) {
        this.idCardBackUrl = idCardBackUrl;
    }

    public String getIdCardFrontUrl() {
        return idCardFrontUrl;
    }

    public void setIdCardFrontUrl(String idCardFrontUrl) {
        this.idCardFrontUrl = idCardFrontUrl;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRiderUrl() {
        return riderUrl;
    }

    public void setRiderUrl(String riderUrl) {
        this.riderUrl = riderUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictAra() {
        return String.format("%s%s%s", getNotNull(provinceName), getNotNull(cityName), getNotNull(districtName));
    }

    String getNotNull(String str) {
        return isNull(str) ? "" : str;
    }

    boolean isNull(String str) {
        return str == null || str.equals("null");
    }
}
