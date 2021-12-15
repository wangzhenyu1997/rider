package com.lingmiao.distribution.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;

import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.HomeDetailParam;
import com.lingmiao.distribution.databinding.ActivityCostDetailBinding;
import com.lingmiao.distribution.util.MathExtend;
import com.lingmiao.distribution.view.LayoutTopView;

/**
 * CostDetailActivity
 *
 * @author yandaocheng <br/>
 * 费用详细
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class CostDetailActivity extends ActivitySupport {

    private ActivityCostDetailBinding viewBinding;
    private HomeDetailParam mData;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityCostDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        mData = (HomeDetailParam) getIntent().getSerializableExtra("data");
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
        }, true, "费用明细", 0, null);

    }

    /**
     * 获取列表数据
     */
    @SuppressLint("SetTextI18n")
    private void getData() {
        viewBinding.mAllMoney.setText(MathExtend.round(mData.totalCost, 2));                         //总金额
        viewBinding.mViewOne.setText(MathExtend.round(mData.tipCost, 2) + "元");                     //小费金额
        viewBinding.mViewTwo.setText(MathExtend.round(mData.rewardCost, 2) + "元");                  //奖励金额
        viewBinding.mViewThree.setText(MathExtend.round(mData.deliveryCost, 2) + "元");              //配送费金额
        viewBinding.mViewFour.setText(MathExtend.round(mData.fineCost, 2) + "元");                   //扣罚金额
        viewBinding.mViewFive.setText(MathExtend.round(mData.compensateCost, 2) + "元");             //赔偿金额
    }
}