package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.databinding.ActivityBillConfirmBinding;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

/**
 * 账单确认
 * niuxinyu
 */
public class BillConfirmActivity extends ActivitySupport implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private ActivityBillConfirmBinding viewBinding;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityBillConfirmBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewBinding.topView.setData(new LayoutTopView.TopCallback() {
            @Override
            public void onTopBack() {
                super.onTopBack();
                finish();
            }
        }, true, "确认账单", 0, "");
        viewBinding.abcRg.setOnCheckedChangeListener(this);
        viewBinding.abcPass.setOnClickListener(this);
        viewBinding.abcUnPass.setOnClickListener(this);
        viewBinding.abcSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.abc_submit://点击提交
                int type = viewBinding.abcRg.getCheckedRadioButtonId();
                String text = "";
                switch (type) {
//                    case R.id.ac_customer_issues:
//                        text = "确认通过";
//                        break;
//                    case R.id.ac_order_error:
//                        text = "确认不通过";
//                        break;
                }
                submitData(text);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.abc_pass:
                viewBinding.abcConfirmLayout.setVisibility(View.GONE);
                viewBinding.abcConfirmPriceHint.setVisibility(View.GONE);
                break;
            case R.id.abc_un_pass:
                viewBinding.abcConfirmLayout.setVisibility(View.VISIBLE);
                viewBinding.abcConfirmPriceHint.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 提交数据
     */
    private void submitData(String text) {
        ToastUtil.showToast(context, "选择" + text);
    }


}
