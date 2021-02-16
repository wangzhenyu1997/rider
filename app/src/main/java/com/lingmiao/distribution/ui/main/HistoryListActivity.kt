package com.lingmiao.distribution.ui.main

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.distribution.R
import com.lingmiao.distribution.app.MyApp
import com.lingmiao.distribution.ui.main.adapter.HistoryDispatchAdapter
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.presenter.IHistoryListPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.HistoryListPreImpl
import com.lingmiao.distribution.widget.EmptyView
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import kotlinx.android.synthetic.main.main_activity_history_dispatch.*

/**
Create Date : 2021/1/45:06 PM
Auther      : Fox
Desc        :
 **/
class HistoryListActivity : BaseLoadMoreActivity<DispatchOrderRecordBean, IHistoryListPresenter>(), IHistoryListPresenter.View{

    companion object {
        const val CODE_DETAIL = 200;
    }

    override fun initOthers() {
        super.initOthers()
        toolbarView?.apply {
            setTitleContent(getString(R.string.main_history_dispatch_title))
        }
    }

    override fun useBaseLayout(): Boolean {
        return false;
    }

    override fun createPresenter(): IHistoryListPresenter {
        return HistoryListPreImpl(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_history_dispatch;
    }

    override fun initAdapter(): BaseQuickAdapter<DispatchOrderRecordBean, BaseViewHolder> {
        return HistoryDispatchAdapter().apply{
            setOnItemChildClickListener { adapter, view, position ->
                var item = getItem(position);
                when(view.id) {
                    R.id.iv_batch_no_copy -> {
                        val cm: ClipboardManager = MyApp.getInstance()
                            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.setPrimaryClip(
                            ClipData.newPlainText(
                                MyApp.getInstance().getPackageName(),
                                item?.dispatchNo
                            )
                        )
                        showToast("复制成功")
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->
                var item = getItem(position);
                DispatchDetailActivity.history(context!!, item?.id!!, CODE_DETAIL);
            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.common_bg)
            }
        };
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(page, mAdapter.data);
    }

    override fun onLoadMoreSuccess(list: List<DispatchOrderRecordBean>?, hasMore: Boolean) {
        if (mLoadMoreDelegate?.getPage()?.isRefreshing() == true) {
            mAdapter.replaceData(list ?: arrayListOf())
        } else {
            mAdapter.addData(list ?: arrayListOf())
        }
        mLoadMoreDelegate?.refresh();
        mLoadMoreDelegate?.loadFinish(hasMore, !list.isNullOrEmpty())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CODE_DETAIL && resultCode == Activity.RESULT_OK) {
            mLoadMoreDelegate?.refresh()
        }
    }

}