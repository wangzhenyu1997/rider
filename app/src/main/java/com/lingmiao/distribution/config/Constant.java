package com.lingmiao.distribution.config;

import android.os.Environment;

import com.fisheagle.mkt.base.UserManager;
import com.lingmiao.distribution.bean.HomeModelEvent;
import com.lingmiao.distribution.bean.PersonalDataParam;

/**
 * 公共静态变量
 */

public class Constant {
    /**
     * 欢迎页停留时间
     */
    public final static int TIME_DELAY_WELCOME = 1000;
    /**
     * 微信支付
     */
    public static final String APP_ID = "wx40d69d15426d5009";
    public static final int WEIXIN_PAY_RESULT_SUCCESS = 0;
    public static final int WEIXIN_PAY_RESULT_ERROR = -1;
    public static final int WEIXIN_PAY_RESULT_CANCEL = -2;
    /**
     * 网络访问提示
     */
    public static final String SUCCESS = "0";// 成功
    public static final int UNLOGIN = 50002;//未登录
    public static String TOKEN = ""; //请求头token
    public static HomeModelEvent Home_Model_Event = UserManager.Companion.getTakingModel();
    /**
     * 用户登录状态,页面返回刷新
     */
    public static Boolean loginState = false;
    public static Boolean refreshView = false;
    //0休息 1工作中
    public static int WORKSTATES = 0;
    public static int WORK_STATES_OF_REST = 0;
    public static int WORK_STATES_OF_WORK = 1;
    public static PersonalDataParam user;

    /**
     * 域名
     */
    public final static String URL = "http://8.136.135.41:8778/rider-mobile-compose/";     //正常请求
    public final static String URLIMAGE = "http://8.136.135.41:8778/base-compose/";        //用于文件上传

    /**
     * 阎道成 --- 接口
     */
    public static final String AppLogin = URL + "login";                                                                 //登录
    public static final String AppCheckRegister = URL + "login/checkIsExist";                                            //校验手机号码是否存在
    public static final String AppRegisterGetPhone = URL + "login/getMobileCaptcha";                                     //注册获取短信验证码
    public static final String AppRegister = URL + "login/register";                                                     //注册接口
    public static final String AppAgreeAccept = URL + "dispatch/agreeAccept";                                            //同意接单
    public static final String AppRefuseAccept = URL + "dispatch/refuseAccept";                                          //拒接接单
    public static final String AppPickFail = URL + "dispatch/pickupFail";                                                //取货失败
    public static final String AppArriveShop = URL + "dispatch/arriveShop";                                              //到达取货点
    public static final String AppArriveStation = URL + "dispatch/arriveStation";                                        //到达送货点
    public static final String AppPickUp = URL + "dispatch/pickup";                                                      //取货成功
    public static final String AppSignedSuccess = URL + "dispatch/signed";                                               //签收成功
    public static final String AppUploadFile = URLIMAGE + "file/uploadFile";                                             //上传单个图片
    public static final String AppUpdateOnLine = URL + "app/upgrade";                                                    //在线升级
    public static final String AppValidateModify = URL + "login/validateMobile";                                         //修改手机号第一步
    public static final String AppDispathList = URL + "dispatch/queryDispatchList";                                      //调度单列表接口
    public static final String AppUserInfoDetail = URL + "rider/queryRider";                                             //获取个人资料
    public static final String AppGetListByType = URLIMAGE + "dict/queryListByType";                                     //数据字段接口
    public static final String AppUploadMoreImage = URLIMAGE + "file/uploadPics";                                        //上传多张图片
    public static final String AppDeleteUploadImage = URLIMAGE + "file/delFileByIds";                                    //图片删除（暂不使用）
    public static final String AppUploadException = URL + "dispatch/uploadException";                                    //上传异常
    public static final String AppSignFail = URL + "dispatch/signFail";                                                  //签收失败
    // 指派并自动接单
    public static final String AssignAndAcecptOrder = URL + "dispatch/assignAndAccept";
    // 接单
    public static final String takeOrder = URL + "setting/addSetting";
    //指派并自动取货
    public static final String AssignAndPickup = URL + "dispatch/assignAndPickup";
    //根据上游单号查询订单
    public static final String QueryOrderByUpsBillNo = URL + "dispatch/queryOrderByUpsBillNo";
    public static final String AppAddComplaint = URL + "complaint/addComplaint";                                         //我要投诉
    public static final String AppAddComment = URL + "comment/addComment";                                               //添加评论
    public static final String AppComplaintList = URL + "complaint/queryComplaintList";                                  //投诉记录
    public static final String AppExceptionList = URL + "dispatch/queryExceptionList";                                   //异常记录
    public static final String AppDispatchDetail = URL + "dispatch/queryDispatchById";                                   //调度单详细
    public static final String AppDispatchNum = URL + "dispatch/countDispatchNum";                                       //汇总调度个数
    public static final String AppMsgList = URL + "noticeMsg/queryNoticeMsgList";                                        //查询消息列表
    public static final String AppUpdateWorkStatus = URL + "rider/updateWorkStatus";                                     //更新工作状态

    /**
     * 牛新宇
     */
    public static final String AppLoginOut = URL + "login/logout";                                                  //退出登录
    public static final String AppUpdateRider = URL + "rider/updateRider";                                          //更新个人资料
    public static final String AppResetPassword = URL + "login/resetPassword";                                      //重置密码
    public static final String AppUpdatePassword = URL + "login/updatePassword";                                   //修改密码
    public static final String AppGetResetPasswordCaptcha = URL + "login/getResetPasswordCaptcha";                  //获取短信验证码
    public static final String AppUpdateMobile = URL + "login/updateMobile";                                        //修改手机号第二步
    public static final String AppOcrDiscern = URL + "rider/ocrDiscern";                                            //OCR识别
    public static final String AppSubmitIdCardAudit = URL + "rider/submitIdCardAudit";                              //身份认证提交审核
    public static final String AppWalletIndex = URL + "account/walletIndex";                                        //钱包首页
    public static final String AppBalanceIndex = URL + "account/balanceIndex";                                      //余额
    public static final String AppIntegralIndex = URL + "account/integralIndex";                                    //积分
    public static final String AppDepositIndex = URL + "account/depositIndex";                                      //押金
    public static final String AppQueryTradeRecordList = URL + "account/queryTradeRecordList";                      //押金
    public static final String AppBindBankCard = URL + "account/bindBankCard";                                      //绑定银行卡
    public static final String AppSubmitWithdrawAccount = URL + "account/submitWithdrawAccount";                    //绑定账户
    public static final String AppUpdateWithdrawAccount = URL + "account/updateWithdrawAccount";                    //修改账户
    public static final String AppQueryBankCardList = URL + "account/queryBankCardList";                            //绑定银行卡列表
    public static final String AppQueryChargeRate = URL + "account/withdraw/queryServiceChargeRate";
    public static final String AppQueryContentList = URL + "content/queryContentList";                              //内容列表
    public static final String AppQueryPayableChargeList = URL + "settleBill/queryPayableChargeList";               //订单费用
    public static final String AppQuerySettleBillById = URL + "settleBill/querySettleBillById";                     //账单详情
    public static final String AppQuerySettleBillList = URL + "settleBill/querySettleBillList";                     //账单列表
    public static final String AppUnBindBankCard = URL + "account/unBindBankCard";                                  //解绑银行卡
    public static final String AppApplyRecharge = URL + "account/applyRecharge";                                    //充值
    public static final String AppApplyWithdraw = URL + "account/applyWithdraw";                                    //提现
    public static final String AppQueryWithdrawAccountList = URL + "account/queryWithdrawAccountList";              //提现账号列表
    public static final String AppQueryWithdrawList = URL + "account/queryWithdrawList";                            //提现记录


    /**
     * 项目所有下载功能目录
     */
    public static final String downLoadPath = Environment.getExternalStorageDirectory() + "/YypsApp/";//总目录

    /**
     * 版本更新
     */
    public static String downLoadUrl = "";          //apk下载URL
    public static int downLoadState = 2;            //apk下载状态
    public static String apkDownLoadPath = Environment.getExternalStorageDirectory() + "/YypsApp/apkPath/";

    /**
     * 定位相关全局变量
     */
    public static String LOCATIONADDRESS = "";              //当前位置
    public static double LOCATIONLONGITUDE = 0;             //当前经度
    public static double LOCATIONLATITUDE = 0;              //当前纬度


    /**
     * 接单模式实体
     */
    public static final String KEY_HOME_TAKING_MODEL = "KEY_HOME_TAKING_MODEL";

}
