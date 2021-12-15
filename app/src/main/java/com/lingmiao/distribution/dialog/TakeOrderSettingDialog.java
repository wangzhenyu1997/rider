package com.lingmiao.distribution.dialog;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.lingmiao.distribution.base.UserManager;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.bean.HomeModelEvent;
import com.lingmiao.distribution.databinding.DialogTakeOrderSettingBinding;

import org.greenrobot.eventbus.EventBus;


public class TakeOrderSettingDialog extends DialogFragment {

    public interface DialogPushConfirmClick {

    }

    private DialogPushConfirmClick mListener;

    private DialogTakeOrderSettingBinding mViewBinding;

    private Drawable mSortDrawable, mSortDescDrawable, mSortAscDrawable, mModelDrawable;

    private HomeModelEvent event;

    public static TakeOrderSettingDialog newInstance() {
        TakeOrderSettingDialog fragment = new TakeOrderSettingDialog();
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = DialogTakeOrderSettingBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();

        initView();

    }

    private void initData() {
        // Bundle bundle = getArguments();
        // event = (HomeModelEvent) bundle.getSerializable("modelData");

        event = UserManager.Companion.getTakingModel();
    }

    private void initView() {
        // drawable
        mSortDrawable = getContext().getDrawable(R.mipmap.ic_sort);
        mSortDrawable.setBounds(0, 0, 30, 30);

        mSortAscDrawable = getContext().getDrawable(R.mipmap.ic_sort_asc);
        mSortAscDrawable.setBounds(0, 0, 30, 30);

        mSortDescDrawable = getContext().getDrawable(R.mipmap.ic_sort_desc);
        mSortDescDrawable.setBounds(0, 0, 30, 30);

        mModelDrawable = getContext().getDrawable(R.mipmap.ic_selected);
        mModelDrawable.setBounds(0, 0, 30, 30);
        // 关闭
        mViewBinding.tvTakeOrderSettingExit.setOnClickListener(v -> {
            // 退出
            dismiss();
        });
        // 确定
        mViewBinding.tvTakeOrderSettingSubmit.setOnClickListener(v -> {
            UserManager.Companion.setTakingModel(event);
            EventBus.getDefault().post(event);
            dismiss();
        });
        // 模式
        mViewBinding.rgModelSort.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_take_order_setting_four:
                    // 四轮
                    setModelDrawable(HomeModelEvent.MODEL_FOUR);
                    event.setWorkStatus(HomeModelEvent.MODEL_FOUR);
                    break;
                case R.id.rb_take_order_setting_two:
                    // 二轮
                    setModelDrawable(HomeModelEvent.MODEL_TWO);
                    event.setWorkStatus(HomeModelEvent.MODEL_TWO);
                    break;
            }
        });
        // 取货
        mViewBinding.rgTakeSortDistance.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_take_order_setting_take_default:
                    // 取货-综合排序
                    setTakeDrawable(HomeModelEvent.SORT_DEFAULT);
                    event.setPickOrder(HomeModelEvent.SORT_DEFAULT);
                    break;
                case R.id.rb_take_order_setting_take_distance:
                    // 取货-距离
                    setTakeDrawable(HomeModelEvent.SORT_DISTANCE);
                    event.setPickOrder(HomeModelEvent.SORT_DISTANCE);
                    break;
                case R.id.rb_take_order_setting_take_time:
                    // 取货-时间
                    setTakeDrawable(HomeModelEvent.SORT_TIME);
                    event.setPickOrder(HomeModelEvent.SORT_TIME);
                    break;
            }
        });
        // 送货
        mViewBinding.rgDeliverySortDistance.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_take_order_setting_delivery_default:
                    //  送货-综合排序
                    setDeliveryDrawable(HomeModelEvent.SORT_DEFAULT);
                    event.setDeliveryOrder(HomeModelEvent.SORT_DEFAULT);
                    break;
                case R.id.rb_take_order_setting_delivery_distance:
                    //  送货-综合排序
                    setDeliveryDrawable(HomeModelEvent.SORT_DISTANCE);
                    event.setDeliveryOrder(HomeModelEvent.SORT_DISTANCE);
                    break;
                case R.id.rb_take_order_setting_delivery_time:
                    //  送货-综合排序
                    setDeliveryDrawable(HomeModelEvent.SORT_TIME);
                    event.setDeliveryOrder(HomeModelEvent.SORT_TIME);
                    break;
            }
        });
        mViewBinding.rbTakeOrderSettingFour.setOnClickListener(v -> {
            mViewBinding.rbTakeOrderSettingFour.setSelected(true);
            mViewBinding.rbTakeOrderSettingTwo.setSelected(false);

            setModelDrawable(HomeModelEvent.MODEL_FOUR);
            event.setWorkStatus(HomeModelEvent.MODEL_FOUR);
        });
        mViewBinding.rbTakeOrderSettingTwo.setOnClickListener(v -> {
            mViewBinding.rbTakeOrderSettingTwo.setSelected(true);
            mViewBinding.rbTakeOrderSettingFour.setSelected(false);
            setModelDrawable(HomeModelEvent.MODEL_TWO);
            event.setWorkStatus(HomeModelEvent.MODEL_TWO);
        });
        setDefaultChecked();
    }

    public void setDefaultChecked() {
        // set default
        switch (event.getWorkStatus()) {
            case HomeModelEvent.MODEL_FOUR:
                mViewBinding.rbTakeOrderSettingFour.setSelected(true);
                break;
            case HomeModelEvent.MODEL_TWO:
                mViewBinding.rbTakeOrderSettingTwo.setSelected(true);
                break;
        }
        setModelDrawable(event.getWorkStatus());

        switch (event.getPickOrder()) {
            case HomeModelEvent.SORT_DEFAULT:
                mViewBinding.rbTakeOrderSettingTakeDefault.setSelected(true);
                break;
            case HomeModelEvent.SORT_DISTANCE:
                mViewBinding.rbTakeOrderSettingTakeDistance.setSelected(true);
                break;
            case HomeModelEvent.SORT_TIME:
                mViewBinding.rbTakeOrderSettingTakeTime.setSelected(true);
                break;
        }
        setTakeDrawable(event.getPickOrder());

        switch (event.getDeliveryOrder()) {
            case HomeModelEvent.SORT_DEFAULT:
                mViewBinding.rbTakeOrderSettingDeliveryDefault.setSelected(true);
                break;
            case HomeModelEvent.SORT_DISTANCE:
                mViewBinding.rbTakeOrderSettingDeliveryDistance.setSelected(true);
                break;
            case HomeModelEvent.SORT_TIME:
                mViewBinding.rbTakeOrderSettingDeliveryTime.setSelected(true);
                break;
        }
        setDeliveryDrawable(event.getDeliveryOrder());
    }

    @Override
    public void onStart() {
        super.onStart();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        getDialog().getWindow().setAttributes(params);
        getDialog().getWindow().setBackgroundDrawable(null);
    }

    private void setModelDrawable(int value) {
        mViewBinding.rbTakeOrderSettingFour.setSelected(value == HomeModelEvent.MODEL_FOUR);
        setDrawable(mViewBinding.rbTakeOrderSettingFour, value == HomeModelEvent.MODEL_FOUR ? mModelDrawable : null);

        mViewBinding.rbTakeOrderSettingTwo.setSelected(value == HomeModelEvent.MODEL_TWO);
        setDrawable(mViewBinding.rbTakeOrderSettingTwo, value == HomeModelEvent.MODEL_TWO ? mModelDrawable : null);
    }

    private void setTakeDrawable(int value) {
        setDrawable(mViewBinding.rbTakeOrderSettingTakeDefault, value == HomeModelEvent.SORT_DEFAULT ? mSortDrawable : null);
        mViewBinding.rbTakeOrderSettingTakeDefault.setSelected(value == HomeModelEvent.SORT_DEFAULT);

        setDrawable(mViewBinding.rbTakeOrderSettingTakeDistance, value == HomeModelEvent.SORT_DISTANCE ? mSortDrawable : null);
        mViewBinding.rbTakeOrderSettingTakeDistance.setSelected(value == HomeModelEvent.SORT_DISTANCE);

        setDrawable(mViewBinding.rbTakeOrderSettingTakeTime, value == HomeModelEvent.SORT_TIME ? mSortDrawable : null);
        mViewBinding.rbTakeOrderSettingTakeTime.setSelected(value == HomeModelEvent.SORT_TIME);
    }

    private void setDeliveryDrawable(int value) {
        setDrawable(mViewBinding.rbTakeOrderSettingDeliveryDefault, value == HomeModelEvent.SORT_DEFAULT ? mSortDrawable : null);
        mViewBinding.rbTakeOrderSettingDeliveryDefault.setSelected(value == HomeModelEvent.SORT_DEFAULT);

        setDrawable(mViewBinding.rbTakeOrderSettingDeliveryDistance, value == HomeModelEvent.SORT_DISTANCE ? mSortDrawable : null);
        mViewBinding.rbTakeOrderSettingDeliveryDistance.setSelected(value == HomeModelEvent.SORT_DISTANCE);

        setDrawable(mViewBinding.rbTakeOrderSettingDeliveryTime, value == HomeModelEvent.SORT_TIME ? mSortDrawable : null);
        mViewBinding.rbTakeOrderSettingDeliveryTime.setSelected(value == HomeModelEvent.SORT_TIME);
    }

    private void setDrawable(TextView button, Drawable drawable) {
        button.setCompoundDrawables(null, null, drawable, null);
    }

    private void setDrawable(RadioButton button, Drawable drawable) {
        button.setCompoundDrawables(null, null, drawable, null);
    }
}
