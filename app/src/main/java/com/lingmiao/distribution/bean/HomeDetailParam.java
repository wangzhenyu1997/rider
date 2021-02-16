package com.lingmiao.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * HomeDetailBean
 *
 * @author yandaocheng <br/>
 * 首页Bean类
 * 2018-06-19
 * 修改者，修改日期，修改内容
 */
public class HomeDetailParam implements Serializable {

    public String dispatchNo;                           //调度单号
    public int dispatchStatus;                          //调度单状态
    public String totalCost;                            //总配送费
    public String tipCost;                              //小费金额
    public String rewardCost;                           //奖励金额
    public String deliveryCost;                         //配送金额
    public String fineCost;                             //扣罚金额
    public String compensateCost;                       //赔偿金额
    public List<HomeTwoParam> orderList;
    public List<HomeGjParam> orderTrackList;

    public String riderName;                            //骑手名称
    public String riderMobile;                            //骑手手机号
    public String vehiclePlate;                            //车牌号
    public String vehicleType;                            //车型

    public String createTime;                            //下单时间
    public String acceptTime;                            //接单时间
    public String signedTime;                           //完成时间（签收时间）

    public String goodsInfo;                           //商品信息

    public int acceptIsOvertime;                            //接单是否超时
    public int signIsOvertime;                              //签收是否超时
    public int exceptionNum;                            //异常个数
    public int complaintNum;                            //投诉个数

    public String id;
}
