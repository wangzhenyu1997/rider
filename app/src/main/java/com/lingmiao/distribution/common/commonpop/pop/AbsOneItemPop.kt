package com.lingmiao.shop.business.commonpop.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.util.hideYTranslateAnim
import com.lingmiao.distribution.util.showYTranslateAnim
import com.lingmiao.shop.business.commonpop.bean.ItemData
import razerdp.basepopup.BaseLazyPopupWindow

/**
 * Author : Elson
 * Date   : 2020/7/15
 * Desc   : 商品类型
 */
class AbsOneItemPop<T : ItemData>(context: Context, var value: String?, var data: List<T>?, var title: String?) :
    BaseLazyPopupWindow(context) {

    var listener: ((List<T>) -> Unit)? = null

    private var checkedPosition: Int = -1;

    var isMultiChecked : Boolean = false;

    fun setOnClickListener(listener: ((List<T>) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.common_pop_single_list)
    }

    fun getIndex(): Int {
        if (value?.isNotBlank() == true) {
            data?.forEachIndexed { index, item ->
                if (value == item.getIValue()) {
                    return index;
                }
            }
        }
        return -1;
    }

    override fun onViewCreated(contentView: View) {
        checkedPosition = getIndex();
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL

        // title
        contentView.findViewById<TextView>(R.id.singleListTitleTv).text = title
        // list
        val singleListRv = contentView.findViewById<RecyclerView>(R.id.singleListRv)
        singleListRv.layoutManager = LinearLayoutManager(context)
        val adapter = object :
            BaseQuickAdapter<T, BaseViewHolder>(R.layout.common_adapter_single_list) {
            override fun convert(helper: BaseViewHolder, item: T?) {
                helper.setText(R.id.singleNameTv, item?.getIName());
//                helper.setText(R.id.singleHintTv,
//                    String.format("%s%s%s",
//                        if(item?.typeDesc?.length?:0 > 0) "(" else "" ,
//                        item?.typeDesc,
//                        if(item?.typeDesc?.length?:0 > 0) ")" else ""
//                    )
//                );
                helper.setGone(R.id.cbItemCheck, isMultiChecked)
                helper.setChecked(R.id.cbItemCheck, item?.isItemChecked()?:false)
                helper.addOnClickListener(R.id.cbItemCheck);
                helper.setTextColor(
                    R.id.singleNameTv,
                    ColorUtils.getColor(
                        if (checkedPosition == helper.adapterPosition) R.color.colorPrimary else R.color.color333
                    )
                );
            }

        }
        singleListRv.adapter = adapter
        adapter.setNewData(data)
        adapter.setOnItemChildClickListener { _, view, position ->
            run {
                if(view.id == R.id.cbItemCheck) {
                    if(isMultiChecked) {
                        val item = adapter?.data?.get(position);
                        item?.shiftChecked(item?.isItemChecked());
                        //dismiss()
                        return@run;
                    } else {
                        checkedPosition = position;
                        if (checkedPosition > -1 && checkedPosition < adapter?.data?.size) {
                            listener?.invoke(listOf(adapter?.data?.get(checkedPosition)))
                            dismiss()
                        }
                    }
                }

//                adapter.notifyDataSetChanged();
            }
        }
        adapter.setOnItemClickListener { _, _, position ->
            run {
                if(isMultiChecked) {
                    val item = adapter?.data?.get(position);
                    item?.shiftChecked(item?.isItemChecked());
                    //dismiss()
                    return@run;
                } else {
                    checkedPosition = position;
                    if (checkedPosition > -1 && checkedPosition < adapter?.data?.size) {
                        listener?.invoke(listOf(adapter?.data?.get(checkedPosition)))
                        dismiss()
                    }
                }
//                adapter.notifyDataSetChanged();
            }
        }
        // confirm
        contentView.findViewById<TextView>(R.id.singleListConfirmTv).apply {
            visibility = if(isMultiChecked)View.VISIBLE else View.GONE;
        }.setOnClickListener {
            if(isMultiChecked) {
                val list = adapter?.data?.filter { it?.isItemChecked() == true };
                listener?.invoke(list)
            } else {
                if (checkedPosition > -1 && checkedPosition < adapter?.data?.size) {
                    listener?.invoke(listOf(adapter?.data?.get(checkedPosition)))
                }
            }
            dismiss()
        }

        // close
        contentView.findViewById<ImageView>(R.id.singleListCloseTv)
            .setOnClickListener { dismiss() }

    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }

}