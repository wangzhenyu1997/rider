package com.lingmiao.distribution.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.adapter.HomeDetailAdapter;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.HomeDetailBean;
import com.lingmiao.distribution.bean.HomeDetailParam;
import com.lingmiao.distribution.bean.HomeGjParam;
import com.lingmiao.distribution.bean.LabelsBean;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityHomeDetailBinding;
import com.lingmiao.distribution.dialog.HomeConfirmDialog;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.MathExtend;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HomeDetailActivity
 *
 * @author yandaocheng <br/>
 * 配送订单详细
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class HomeDetailActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityHomeDetailBinding viewBinding;
    private HomeDetailAdapter mAdapter;
    private List<HomeGjParam> mListData = new ArrayList<>();//列表数据

    private String mId;     //调度单id
//    private int mIndex;         //0.待取货   1.待送达   2.已结束

    private HomeDetailParam mDetailData;        //总数据，用于费用明细

    private String mConsignerPhone;     //商家电话
    private String mConsigneePhone;     //收货人电话

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityHomeDetailBinding.inflate(LayoutInflater.from(this));
        mId = getIntent().getStringExtra("id");
//        mIndex = getIntent().getIntExtra("index", 0);
        EventBus.getDefault().register(this);
        setContentView(viewBinding.getRoot());
        initView();
        getData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewBinding.topView.setData(new LayoutTopView.TopCallback() {
            @Override
            public void onTopBack() {
                finish();
            }

            @Override
            public void onRightTx() {
                startActivity(new Intent(getContext(), ComplaintActivity.class).putExtra("id", mDetailData.orderList.get(0).getId()));
            }
        }, true, "订单详情", 0, "投诉");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.mRecyclerView.setLayoutManager(linearLayoutManager);
        viewBinding.mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new HomeDetailAdapter(mListData, this);
        viewBinding.mRecyclerView.setAdapter(mAdapter);
        viewBinding.mCostView.setOnClickListener(this);
        viewBinding.mAbnormalMessage.setOnClickListener(this);
        viewBinding.mComplaintMessage.setOnClickListener(this);
        viewBinding.mBusinessName.setOnClickListener(this);
        viewBinding.mPersonName.setOnClickListener(this);

        viewBinding.mRefuseOrder.setOnClickListener(this);
        viewBinding.mSureOrder.setOnClickListener(this);
        viewBinding.mUpAbnormal.setOnClickListener(this);
        viewBinding.mPickGood.setOnClickListener(this);
        viewBinding.mPickFail.setOnClickListener(this);
        viewBinding.mPickSuccess.setOnClickListener(this);
        viewBinding.mUpAbnormalTwo.setOnClickListener(this);
        viewBinding.mReceivingGoods.setOnClickListener(this);
        viewBinding.mSignFail.setOnClickListener(this);
        viewBinding.mSignSuccess.setOnClickListener(this);
        viewBinding.mComplaintCustomer.setOnClickListener(this);
        viewBinding.mEvaluateCustomer.setOnClickListener(this);
    }

    /**
     * 获取列表数据
     */
    private void getData() {
        viewBinding.mBtnOne.setVisibility(View.GONE);
        viewBinding.mBtnTwo.setVisibility(View.GONE);
        viewBinding.mBtnThree.setVisibility(View.GONE);
        viewBinding.mBtnFour.setVisibility(View.GONE);
        viewBinding.mBtnFive.setVisibility(View.GONE);
        viewBinding.mBtnSix.setVisibility(View.GONE);
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("id", mId);
        OkHttpUtils.postAync(Constant.AppDispatchDetail, new Gson().toJson(mMap), new HttpCallback<HomeDetailBean>(context, getProgressDialog()) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(HomeDetailBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null) {
                    mDetailData = response.getData();
                    viewBinding.mStatus.setText(getDispatchName(response.getData().dispatchStatus));                                    //账单状态

                    if (response.getData().dispatchStatus == 2 || response.getData().dispatchStatus == 3 || response.getData().dispatchStatus == 4) {
                        viewBinding.mStartView.setImageResource(R.mipmap.home_blue_circle_no);
                        viewBinding.mEndView.setImageResource(R.mipmap.home_red_circle_no);
                    } else if (response.getData().dispatchStatus == 5 || response.getData().dispatchStatus == 6) {
                        viewBinding.mStartView.setImageResource(R.mipmap.home_blue_circle);
                        viewBinding.mEndView.setImageResource(R.mipmap.home_red_circle_no);
                    } else {
                        viewBinding.mStartView.setImageResource(R.mipmap.home_blue_circle);
                        viewBinding.mEndView.setImageResource(R.mipmap.home_red_circle);
                    }

                    viewBinding.mMoney.setText("¥" + MathExtend.round(response.getData().totalCost, 2));                         //总配送费
                    viewBinding.llMOutNo.setVisibility(View.GONE);
                    viewBinding.lMOutNo.setVisibility(View.GONE);
                    if (response.getData().orderList != null && response.getData().orderList.size() != 0) {
                        viewBinding.mOutNo.setText(response.getData().orderList.get(0).upsBillNo);
                        viewBinding.llMOutNo.setVisibility(response.getData().orderList.get(0).outOrderNoIsEmpty() ? View.GONE : View.VISIBLE);
                        viewBinding.lMOutNo.setVisibility(response.getData().orderList.get(0).outOrderNoIsEmpty() ? View.GONE : View.VISIBLE);

                        viewBinding.mNumber.setText(response.getData().orderList.get(0).orderNo);                                       //定单号
                        if (response.getData().orderList.get(0).isAppointment == 1) {
                            viewBinding.mOrderTag.setVisibility(View.VISIBLE);                                                          //订单号前的tab标识(是否预约)
                        } else {
                            viewBinding.mOrderTag.setVisibility(View.GONE);                                                             //订单号前的tab标识(是否预约)
                        }
                        viewBinding.mTime.setText(PublicUtil.isNull(response.getData().orderList.get(0).planDeliveryTime));      //配送时效时间
                        viewBinding.mTimeRequire.setText(PublicUtil.isNull(response.getData().orderList.get(0).showTimeRequire));//时效
                        viewBinding.mPerson.setText(PublicUtil.isNull(response.getData().orderList.get(0).consigneeName));              //下单用户
                        viewBinding.mBusinessName.setText(PublicUtil.isNull(response.getData().orderList.get(0).consignerCustomerName) + "," + response.getData().orderList.get(0).consignerPhone);//商家名称
                        String mConsignerDetailAddress = PublicUtil.isNull(response.getData().orderList.get(0).consignerProvince) + PublicUtil.isNull(response.getData().orderList.get(0).consignerCity) +
                                PublicUtil.isNull(response.getData().orderList.get(0).consignerDistrict) + PublicUtil.isNull(response.getData().orderList.get(0).consignerStreet) +
                                PublicUtil.isNull(response.getData().orderList.get(0).consignerAddress);
                        viewBinding.mStartAddress.setText(PublicUtil.isNull(mConsignerDetailAddress));     //商家地址
                        if (response.getData().orderList.get(0).originDistance != null && !response.getData().orderList.get(0).originDistance.equals("")) {
                            viewBinding.mStartDistance.setText(PublicUtil.isNull(response.getData().orderList.get(0).originDistance) + "公里");       //商家距离
                        } else {
                            viewBinding.mStartDistance.setText("");       //商家距离
                        }
                        mConsignerPhone = response.getData().orderList.get(0).consignerPhone;                                           //商家电话
                        viewBinding.mPersonName.setText(response.getData().orderList.get(0).consigneeName + "," + response.getData().orderList.get(0).consigneePhone);        //收货人姓名+电话
                        mConsigneePhone = response.getData().orderList.get(0).consigneePhone;                                           //收货人电话
                        String mConsigneeDetailAddress = PublicUtil.isNull(response.getData().orderList.get(0).consigneeProvince) + PublicUtil.isNull(response.getData().orderList.get(0).consigneeCity) +
                                PublicUtil.isNull(response.getData().orderList.get(0).consigneeDistrict) + PublicUtil.isNull(response.getData().orderList.get(0).consigneeStreet) +
                                PublicUtil.isNull(response.getData().orderList.get(0).consigneeAddress);
                        viewBinding.mEndAddress.setText(PublicUtil.isNull(mConsigneeDetailAddress));       //收货人地址
                        if (response.getData().orderList.get(0).targetDistance != null && !response.getData().orderList.get(0).targetDistance.equals("")) {
                            viewBinding.mEndDistance.setText(PublicUtil.isNull(response.getData().orderList.get(0).targetDistance) + "公里");        //收货人距离
                        } else {
                            viewBinding.mEndDistance.setText("");        //收货人距离
                        }
                        //tap标签
                        viewBinding.mTagLi.removeAllViews();
                        if (response.getData().orderList.get(0).labels != null) {
                            List<LabelsBean> mValue = new Gson().fromJson(response.getData().orderList.get(0).labels, new TypeToken<List<LabelsBean>>() {
                            }.getType());
                            for (LabelsBean param : mValue) {
                                @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.tag_view, null);
                                ((TextView) view.findViewById(R.id.m_tag)).setText(param.getLabel());
                                ((TextView) view.findViewById(R.id.m_tag)).setTextColor(Color.parseColor(param.getColor()));
                                GradientDrawable gd = new GradientDrawable();
                                gd.setColor(Color.parseColor("#ffffff"));
                                gd.setCornerRadius(5);
                                gd.setStroke(2, Color.parseColor(param.getColor()));
                                view.setBackgroundDrawable(gd);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.findViewById(R.id.m_tag).getLayoutParams());
                                params.setMargins(20, 0, 0, 0);
                                view.setLayoutParams(params);
                                viewBinding.mTagLi.addView(view);
                            }
                        }
                        viewBinding.mDtTime.setText((PublicUtil.isNull(response.getData().orderList.get(0).planDeliveryTime).equals("") ? "立即配送" : PublicUtil.isNull(response.getData().orderList.get(0).planDeliveryTime) + " 配送"));
                        viewBinding.mGood.setText(response.getData().goodsInfo);                                                        //商品信息
                        if (response.getData().orderList.get(0).customerRemark != null && !response.getData().orderList.get(0).customerRemark.equals("")) {
                            viewBinding.mRemarkView.setVisibility(View.VISIBLE);
                            viewBinding.mRemark.setText(PublicUtil.isNull(response.getData().orderList.get(0).customerRemark));             //备注
                        }
                    }

                    viewBinding.mDispatcher.setText(response.getData().riderName);                                                      //配送员
                    viewBinding.mOrderPhone.setText(response.getData().riderMobile);                                                    //手机号
                    viewBinding.mPlateNum.setText(PublicUtil.isNull(response.getData().vehiclePlate));                                  //车牌号
                    viewBinding.mModel.setText(PublicUtil.isNull(response.getData().vehicleType));                                      //车型

                    viewBinding.mDownTime.setText("下单时间：" + PublicUtil.isNull(response.getData().createTime));                      //下单时间
                    viewBinding.mReceivingTime.setText("接单时间：" + PublicUtil.isNull(response.getData().acceptTime));                 //接单时间
                    viewBinding.mCompleteTime.setText("完成时间：" + PublicUtil.isNull(response.getData().signedTime));                  //完成时间
                    viewBinding.mReceivingValidTime.setText("接单时效：" + PublicUtil.isNull(response.getData().acceptTime));            //接单时效
                    if (response.getData().acceptIsOvertime == 1) {  //超时
                        viewBinding.mReceivingValidState.setText("超时");                                                                //接单时效状态
                        viewBinding.mReceivingValidState.setTextColor(getResources().getColor(R.color.colorPrice));
                    } else {
                        viewBinding.mReceivingValidState.setText("正常");                                                                //接单时效状态
                        viewBinding.mReceivingValidState.setTextColor(getResources().getColor(R.color.colorNormal));
                    }
                    viewBinding.mSignValidTime.setText("签收时间：" + PublicUtil.isNull(response.getData().signedTime));                 //签收时效
                    if (response.getData().signIsOvertime == 1) {  //超时
                        viewBinding.mSignValidState.setText("超时");                                                                      //签收时效状态
                        viewBinding.mSignValidState.setTextColor(getResources().getColor(R.color.colorPrice));
                    } else {
                        viewBinding.mSignValidState.setText("正常");                                                                      //签收时效状态
                        viewBinding.mSignValidState.setTextColor(getResources().getColor(R.color.colorNormal));
                    }
                    viewBinding.mAbNum.setText(response.getData().exceptionNum + "");                                                    //异常信息数量
                    viewBinding.mCpNum.setText(response.getData().complaintNum + "");                                                    //投诉信息数量

                    //订单轨迹预留
                    if (response.getData().orderTrackList != null && response.getData().orderTrackList.size() != 0) {
                        mListData.clear();
                        mListData.addAll(response.getData().orderTrackList);
                        mAdapter.notifyDataSetChanged();
                    }
                    if (response.getData().dispatchStatus == 2) {   //拒绝接单/确认接单
                        viewBinding.mBtnOne.setVisibility(View.VISIBLE);
                    } else if (response.getData().dispatchStatus == 3) { //上报异常/到达取货点
                        viewBinding.mBtnTwo.setVisibility(View.VISIBLE);
                    } else if (response.getData().dispatchStatus == 4) {//取货失败/取货成功
                        viewBinding.mBtnThree.setVisibility(View.VISIBLE);
                    } else if (response.getData().dispatchStatus == 5) { //上报异常/到达收货点
                        viewBinding.mBtnFour.setVisibility(View.VISIBLE);
                    } else if (response.getData().dispatchStatus == 6) { //签收失败/签收成功
                        viewBinding.mBtnFive.setVisibility(View.VISIBLE);
                    } else if (response.getData().dispatchStatus == 7) { // 投诉客户/评价客户
                        viewBinding.mBtnSix.setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 获取账单状态
     */
    private String getDispatchName(int dispatchStatus) {
        String mName = "";
        switch (dispatchStatus) {
            case -1:
                mName = "已取消";
                break;
            case 0:
                mName = "创建调度单";
                break;
            case 1:
                mName = "骑手拒绝";
                break;
            case 2:
                mName = "待骑手确认";
                break;
            case 3:
                mName = "待到店";
                break;
            case 4:
                mName = "待取货";
                break;
            case 5:
                mName = "待送达";
                break;
            case 6:
                mName = "待签收";
                break;
            case 7:
                mName = "已签收";
                break;
            case 8:
                mName = "签收失败";
                break;
            case 9:
                mName = "取货失败";
                break;
        }
        return mName;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(PublicBean event) {
        if (event.getCode() == 11) {
            getData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_cost_view:      //费用详情
                startActivity(new Intent(context, CostDetailActivity.class).putExtra("data", mDetailData));
                break;
            case R.id.m_abnormal_message:  //异常记录
                startActivity(new Intent(context, ReportExceptionListActivity.class).putExtra("id", mId));
                break;
            case R.id.m_complaint_message:  //投诉记录
                startActivity(new Intent(context, ComplaintListActivity.class).putExtra("id", mId));
                break;
            case R.id.m_business_name:      //商家电话
                if (mConsignerPhone.isEmpty()) {
                    ToastUtil.showToast(context, "暂无号码！");
                    return;
                }
                PublicUtil.callPhone(mConsignerPhone, this);
                break;
            case R.id.m_person_name:        //收货人电话
                if (mConsigneePhone.isEmpty()) {
                    ToastUtil.showToast(context, "暂无号码！");
                    return;
                }
                PublicUtil.callPhone(mConsigneePhone, this);
                break;
            case R.id.m_refuse_order:
                startActivity(new Intent(context, RejectionActivity.class).putExtra("id", mDetailData.id));
                break;
            case R.id.m_sure_order:
                HomeConfirmDialog dialog = new HomeConfirmDialog(context, value -> {
                    if (value) {
                        agreeAccept(mDetailData.id);
                    }
                }, "接单提示", "请确认是否接单配送？", "取消", "确认接单");
                dialog.show();
                break;
            case R.id.m_up_abnormal:
            case R.id.m_up_abnormal_two:
                startActivity(new Intent(context, ReportExceptionActivity.class).putExtra("id", mDetailData.id));
                break;
            case R.id.m_pick_good:
                HomeConfirmDialog dialog4 = new HomeConfirmDialog(context, value -> {
                    if (value) {
                        arriveShop(mDetailData.id);
                    }
                }, "到达提示", "请确认是否已到达取货点？", "取消", "确认到达");
                dialog4.show();
                break;
            case R.id.m_pick_fail:
                startActivity(new Intent(getContext(), FailedToSignInActivity.class).putExtra("id", mDetailData.id));
                break;
            case R.id.m_pick_success:
                HomeConfirmDialog dialog1 = new HomeConfirmDialog(context, value -> {
                    if (value) {
                        pickUp(mDetailData.id);
                    }
                }, "取货提示", "请确认是否已取货成功？", "取消", "确认成功");
                dialog1.show();
                break;
            case R.id.m_receiving_goods:
                HomeConfirmDialog dialog2 = new HomeConfirmDialog(context, value -> {
                    if (value) {
                        arriveStation(mDetailData.id);
                    }
                }, "送达提示", "请确认是否已送达收货点？", "取消", "确认送达");
                dialog2.show();
                break;
            case R.id.m_sign_fail:
                startActivity(new Intent(getContext(), FailedToSignInTwoActivity.class).putExtra("id", mDetailData.id));
                break;
            case R.id.m_sign_success:
                HomeConfirmDialog dialog3 = new HomeConfirmDialog(context, value -> {
                    if (value) {
                        signedSuccess(mDetailData.id);
                    }
                }, "签收提示", "请确认是否已签收成功？", "取消", "确认成功");
                dialog3.show();
                break;
            case R.id.m_complaint_customer:
                startActivity(new Intent(getContext(), ComplaintActivity.class).putExtra("id", mDetailData.orderList.get(0).getId()));
                break;
            case R.id.m_evaluate_customer:
                startActivity(new Intent(context, EvaluateCustomerActivity.class).putExtra("id", mDetailData.orderList.get(0).getId()));
                break;
        }
    }

    /**
     * 确认接单
     */
    private void agreeAccept(String id) {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("id", id);
        OkHttpUtils.postAync(Constant.AppAgreeAccept, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    getData();
                    EventBus.getDefault().post(new PublicBean(10));
                    EventBus.getDefault().post(new PublicBean(12));
                }
            }
        });
    }

    /**
     * 到达取货点
     */
    private void arriveShop(String id) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("dispatchId", id);
        mMap.put("address", PublicUtil.isNull(Constant.LOCATIONADDRESS));
        mMap.put("latitude", Constant.LOCATIONLATITUDE);
        mMap.put("longitude", Constant.LOCATIONLONGITUDE);
        OkHttpUtils.postAync(Constant.AppArriveShop, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    getData();
                    EventBus.getDefault().post(new PublicBean(10));
                    EventBus.getDefault().post(new PublicBean(12));
                }
            }
        });
    }

    /**
     * 到达送货点
     */
    private void arriveStation(String id) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("dispatchId", id);
        mMap.put("address", PublicUtil.isNull(Constant.LOCATIONADDRESS));
        mMap.put("latitude", Constant.LOCATIONLATITUDE);
        mMap.put("longitude", Constant.LOCATIONLONGITUDE);
        OkHttpUtils.postAync(Constant.AppArriveStation, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    getData();
                    EventBus.getDefault().post(new PublicBean(10));
                    EventBus.getDefault().post(new PublicBean(12));
                }
            }
        });
    }

    /**
     * 取货成功
     */
    private void pickUp(String id) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("dispatchId", id);
        mMap.put("address", PublicUtil.isNull(Constant.LOCATIONADDRESS));
        mMap.put("latitude", Constant.LOCATIONLATITUDE);
        mMap.put("longitude", Constant.LOCATIONLONGITUDE);
        OkHttpUtils.postAync(Constant.AppPickUp, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    getData();
                    EventBus.getDefault().post(new PublicBean(10));
                    EventBus.getDefault().post(new PublicBean(12));
                }
            }
        });
    }

    /**
     * 签收成功
     */
    private void signedSuccess(String id) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("dispatchId", id);
        mMap.put("address", PublicUtil.isNull(Constant.LOCATIONADDRESS));
        mMap.put("latitude", Constant.LOCATIONLATITUDE);
        mMap.put("longitude", Constant.LOCATIONLONGITUDE);
        OkHttpUtils.postAync(Constant.AppSignedSuccess, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    getData();
                    EventBus.getDefault().post(new PublicBean(10));
                    EventBus.getDefault().post(new PublicBean(12));
                }
            }
        });
    }

}