package com.lingmiao.distribution.ui.main.pop

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.*
import com.fisheagle.mkt.base.UserManager;
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.HomeModelEvent
import com.james.common.utils.exts.drawRight
import com.james.common.utils.hideYTranslateAnim
import com.james.common.utils.showYTranslateAnim
import razerdp.basepopup.BaseLazyPopupWindow

/**
Create Date : 2020/12/302:18 PM
Auther      : Fox
Desc        :
 **/
class TakingOrderSettingPop(context: Context, var data: HomeModelEvent) :
    BaseLazyPopupWindow(context) {

    private var listener: ((HomeModelEvent) -> Unit)? = null

    private var mSortDescDrawable: Drawable? = null
    private var mSortAscDrawable: Drawable? = null
    private var mModelDrawable: Drawable? = null
    private var mDoubleSortDrawable: Drawable? = null
    private var mDoubleSortSelectedDrawable: Drawable? = null

    private var event: HomeModelEvent? = null

    fun setOnClickListener(listener: ((HomeModelEvent) -> Unit)?) {
        this.listener = listener
    }

    fun setTakingModel(event: HomeModelEvent) {
        this.event = event
    }

    /**
     *
     *
     * 返回一个contentView以作为PopupWindow的contentView
     *
     * <br></br>
     * **强烈建议使用[BasePopupWindow.createPopupById]，该方法支持读取View的xml布局参数，否则可能会出现与布局不一样的展示从而必须手动传入宽高等参数**
     */
    override fun onCreateContentView(): View {
        return createPopupById(R.layout.dialog_take_order_setting)
    }

    override fun onCreateShowAnimation(): Animation {
        return showYTranslateAnim(300)
    }

    override fun onCreateDismissAnimation(): Animation{
        return hideYTranslateAnim(300)
    }

    override fun onViewCreated(contentView: View) {
        popupGravity = Gravity.BOTTOM or Gravity.CENTER

        initData()

        initView()

    }
    private fun initData() {
        event = UserManager.getTakingModel()

        // drawable
        mModelDrawable = context.getDrawable(R.mipmap.ic_selected)
        mModelDrawable?.setBounds(0, 0, 30, 30)
        mSortAscDrawable = context.getDrawable(R.mipmap.ic_sort_asc)
        mSortAscDrawable?.setBounds(0, 0, 40, 40)
        mSortDescDrawable = context.getDrawable(R.mipmap.ic_sort_desc)
        mSortDescDrawable?.setBounds(0, 0, 40, 40)
        mDoubleSortDrawable = context.getDrawable(R.mipmap.ic_default_sort)
        mDoubleSortDrawable?.setBounds(0, 0, 40, 40)
        mDoubleSortSelectedDrawable = context.getDrawable(R.mipmap.ic_default_sort_selected)
        mDoubleSortSelectedDrawable?.setBounds(0, 0, 40, 40)
    }

    private fun initView() {
        // 关闭
        contentView.findViewById<ImageView>(R.id.tv_take_order_setting_exit).setOnClickListener{ v ->
            // 退出
            dismiss()
        }
        // 确定
        contentView.findViewById<TextView>(R.id.tv_take_order_setting_submit).setOnClickListener { v ->
            listener?.invoke(event!!)
            dismiss()
        }

        setModelView()

        setTakeSortView();

        setDeliverySortView();
    }

    private fun setModelView() {
        // 轮模式
        contentView.findViewById<RadioButton>(R.id.rb_take_order_setting_four).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                event?.workStatus = HomeModelEvent.MODEL_FOUR
            }
            contentView.findViewById<RadioButton>(R.id.rb_take_order_setting_four).drawRight(if(isChecked) mModelDrawable else null)
        }
        contentView.findViewById<RadioButton>(R.id.rb_take_order_setting_two).setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                event?.workStatus = HomeModelEvent.MODEL_TWO
            }
            contentView.findViewById<RadioButton>(R.id.rb_take_order_setting_two).drawRight(if(isChecked) mModelDrawable else null)
        }
        // set default model
        event?.apply {
            if(workStatus == HomeModelEvent.MODEL_FOUR) {
                contentView.findViewById<RadioButton>(R.id.rb_take_order_setting_four).isChecked = true
            } else {
                contentView.findViewById<RadioButton>(R.id.rb_take_order_setting_two).isChecked = true
            }
        }
    }

    private fun setTakeSortView() {
        // set take sort
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_default).setOnClickListener {
            setTakeDefaultSort()
        }
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_distance).setOnClickListener{
            setTakeDistanceSort();

        }
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_time).setOnClickListener{
            setTakeTimeSort();
        }
        when(event?.pickOrder) {
            HomeModelEvent.SORT_DEFAULT -> {
                setTakeDefaultSort()
            }
            HomeModelEvent.SORT_DISTANCE -> {
                setTakeDistanceSort();
            }
            HomeModelEvent.SORT_TIME -> {
                setTakeTimeSort();
            }
        }
    }

    private fun setTakeDefaultSort() {
//        if(event?.pickOrder == HomeModelEvent.SORT_DEFAULT) {
//            event?.pickOrderSort = if(event?.pickOrderSort == 0) 1 else 0;
//        } else {
//            event?.pickOrder = HomeModelEvent.SORT_DEFAULT;
//            event?.pickOrderSort = 0;
//        }
        event?.pickOrder = HomeModelEvent.SORT_DEFAULT;
        event?.pickOrderSort = 0;
        resetTakingDefaultDrawable();
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_default).isSelected = true;
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_default).drawRight(mDoubleSortSelectedDrawable);
    }

    private fun setTakeDistanceSort() {
        if(event?.pickOrder == HomeModelEvent.SORT_DISTANCE) {
            event?.pickOrderSort = if(event?.pickOrderSort == 0) 1 else 0;
        } else {
            event?.pickOrder = HomeModelEvent.SORT_DISTANCE;
            event?.pickOrderSort = 0;
        }
        resetTakingDefaultDrawable();
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_distance).isSelected = true;
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_distance).drawRight(if(event?.pickOrderSort == 0) mSortAscDrawable else mSortDescDrawable);
    }

    private fun setTakeTimeSort() {
        if(event?.pickOrder == HomeModelEvent.SORT_TIME) {
            event?.pickOrderSort = if(event?.pickOrderSort == 0) 1 else 0;
        } else {
            event?.pickOrder = HomeModelEvent.SORT_TIME;
            event?.pickOrderSort = 0;
        }
        resetTakingDefaultDrawable();
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_time).isSelected = true;
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_time).drawRight(if(event?.pickOrderSort == 0) mSortAscDrawable else mSortDescDrawable);
    }

    private fun resetTakingDefaultDrawable() {
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_default).drawRight(mDoubleSortDrawable);
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_distance).drawRight(mSortDescDrawable);
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_time).drawRight(mSortDescDrawable);

        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_default).isSelected = false;
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_distance).isSelected = false;
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_take_time).isSelected = false;
    }

    private fun setDeliverySortView() {
        // set delivery sort
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_default).setOnClickListener {
            setDeliveryDefaultSort()
        }
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_distance).setOnClickListener{
            setDeliveryDistanceSort();
        }
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_time).setOnClickListener{
            setDeliveryTimeSort();
        }
        when(event?.deliveryOrder) {
            HomeModelEvent.SORT_DEFAULT -> {
                setDeliveryDefaultSort()
            }
            HomeModelEvent.SORT_DISTANCE -> {
                setDeliveryDistanceSort();
            }
            HomeModelEvent.SORT_TIME -> {
                setDeliveryTimeSort();
            }
        }
    }


    private fun setDeliveryDefaultSort() {
//        if(event?.deliveryOrder == HomeModelEvent.SORT_DEFAULT) {
//            event?.deliveryOrderSort = if(event?.deliveryOrderSort == 0) 1 else 0;
//        } else {
//            event?.deliveryOrder = HomeModelEvent.SORT_DEFAULT;
//            event?.deliveryOrderSort = 0;
//        }
        event?.deliveryOrder = HomeModelEvent.SORT_DEFAULT;
        event?.deliveryOrderSort = 0;
        resetDeliveringDefaultDrawable();
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_default).isSelected = true;
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_default).drawRight(mDoubleSortSelectedDrawable);
    }

    private fun setDeliveryDistanceSort() {
        if(event?.deliveryOrder == HomeModelEvent.SORT_DISTANCE) {
            event?.deliveryOrderSort = if(event?.deliveryOrderSort == 0) 1 else 0;
        } else {
            event?.deliveryOrder = HomeModelEvent.SORT_DISTANCE;
            event?.deliveryOrderSort = 0;
        }
        resetDeliveringDefaultDrawable();
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_distance).isSelected = true;
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_distance).drawRight(if(event?.deliveryOrderSort == 0) mSortAscDrawable else mSortDescDrawable);
    }

    private fun setDeliveryTimeSort() {
        if(event?.deliveryOrder == HomeModelEvent.SORT_TIME) {
            event?.deliveryOrderSort = if(event?.deliveryOrderSort == 0) 1 else 0;
        } else {
            event?.deliveryOrder = HomeModelEvent.SORT_TIME;
            event?.deliveryOrderSort = 0;
        }
        resetDeliveringDefaultDrawable();
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_time).isSelected = true;
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_time).drawRight(if(event?.deliveryOrderSort == 0) mSortAscDrawable else mSortDescDrawable);
    }

    private fun resetDeliveringDefaultDrawable() {
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_default).drawRight(mDoubleSortDrawable);
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_distance).drawRight(mSortDescDrawable);
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_time).drawRight(mSortDescDrawable);

        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_default).isSelected = false;
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_distance).isSelected = false;
        contentView.findViewById<TextView>(R.id.rb_take_order_setting_delivery_time).isSelected = false;
    }

}