package com.lingmiao.distribution.ui.main.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import com.lingmiao.distribution.R
import com.james.common.utils.exts.show
import com.james.common.utils.hideYTranslateAnim
import com.james.common.utils.hideYTranslateAnimOfBottomToTop
import com.james.common.utils.showYTranslateAnim
import com.james.common.utils.showYTranslateAnimOfTopToBottom
import razerdp.basepopup.BaseLazyPopupWindow


/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : 商品列表编辑
 */
class StatusMenuPop(context: Context, var flags: Int = TYPE_AGREEING) :
    BaseLazyPopupWindow(context) {

    companion object {
        const val TYPE_AGREEING = 0x001 //待接单
        const val TYPE_TAKING = 0x002   //待取货
        const val TYPE_DELIVERY = 0x004 //待送达
    }

    init {
        // 透明背景
        setBackgroundColor(0)
    }

    private var agreeingTv: TextView? = null
    private var takingTv: TextView? = null
    private var deliveryTv: TextView? = null

    private var listener: ((Int) -> Unit)? = null

    fun setOnClickListener(listener: ((Int) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.main_pop_menu)
    }

    override fun onViewCreated(contentView: View) {
        // 显示的位置
        setPopupGravity(Gravity.BOTTOM or Gravity.CENTER_VERTICAL)
        // 待接单
        agreeingTv = contentView.findViewById<TextView>(R.id.goodsEditTv).apply {
            show(flags and TYPE_AGREEING != 0)
            setOnClickListener {
                listener?.invoke(TYPE_AGREEING)
                dismiss()
            }
        }
        // 待取货
        takingTv = contentView.findViewById<TextView>(R.id.goodsDisableTv).apply {
            show(flags and TYPE_TAKING != 0)
            setOnClickListener {
                listener?.invoke(TYPE_TAKING)
                dismiss()
            }
        }
        // 待送达
        deliveryTv = contentView.findViewById<TextView>(R.id.goodsQuantityTv).apply {
            show(flags and TYPE_DELIVERY != 0)
            setOnClickListener {
                listener?.invoke(TYPE_DELIVERY)
                dismiss()
            }
        }
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnimOfTopToBottom(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideYTranslateAnimOfBottomToTop(300)
    }
}