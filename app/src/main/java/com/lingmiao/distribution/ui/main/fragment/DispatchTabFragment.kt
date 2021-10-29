package com.lingmiao.distribution.ui.main.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.fisheagle.mkt.base.UserManager
import com.google.gson.Gson
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.HomeModelEvent
import com.lingmiao.distribution.bean.PublicBean
import com.lingmiao.distribution.bean.PushBean
import com.lingmiao.distribution.config.Constant
import com.lingmiao.distribution.dialog.HomeConfirmDialog
import com.lingmiao.distribution.dialog.HomeConfirmDialog.DialogHomeConfirmClick
import com.lingmiao.distribution.ui.activity.RejectionActivity
import com.lingmiao.distribution.ui.main.SearchActivity
import com.lingmiao.distribution.ui.main.adapter.DispatchPageAdapter
import com.lingmiao.distribution.ui.main.bean.DispatchConstants
import com.lingmiao.distribution.ui.main.bean.DispatchNumberBean
import com.lingmiao.distribution.ui.main.bean.DispatchOrderRecordBean
import com.lingmiao.distribution.ui.main.event.*
import com.lingmiao.distribution.ui.main.pop.HomePushDialog
import com.lingmiao.distribution.ui.main.pop.TakingOrderSettingPop
import com.lingmiao.distribution.ui.main.presenter.IDispatchTabPresenter
import com.lingmiao.distribution.ui.main.presenter.impl.DispatchTabPreImpl
import com.lingmiao.distribution.util.VoiceUtils
import com.lingmiao.distribution.widget.ITabWithNumberView
import com.james.common.base.BaseFragment
import com.lingmiao.distribution.ui.main.pop.TakeOrderDialog
import kotlinx.android.synthetic.main.main_fragment_dispatch_tab.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
Create Date : 2020/12/284:30 PM
Auther      : Fox
Desc        :
 **/
class DispatchTabFragment : BaseFragment<IDispatchTabPresenter>(), IDispatchTabPresenter.View {

    private var tvTabList: ArrayList<ITabWithNumberView> = ArrayList();

    private var mTabTitles = arrayOf("待抢单", "待接单", "待取货", "待送达")

    private var mFragments = mutableListOf<Fragment>();

    private var mediaPlayer: MediaPlayer? = null;

    private var mPushDialog: HomePushDialog? = null;

    private var mTakeOrderDialog: TakeOrderDialog? = null

    companion object {

        fun newInstance(): DispatchTabFragment {
            return DispatchTabFragment()
        }

    }

    override fun getLayoutId() = R.layout.main_fragment_dispatch_tab;


    override fun useEventBus() = true


    override fun createPresenter(): IDispatchTabPresenter? {
        return DispatchTabPreImpl(this)
    }

    override fun initViewsAndData(rootView: View) {
        initTabLayout()

        initTakingOrderSetting()

        loadSetting()

        setWorkStatus()
    }

    private fun loadTabNumber() {
        mPresenter?.loadTabNumber(Constant.Home_Model_Event)
    }

    private fun loadSetting() {
        mPresenter?.getSetting({
            it?.apply {
                it.workStatus = HomeModelEvent.MODEL_TWO
                UserManager.setTakingModel(it)
                Constant.Home_Model_Event = it
            }
        }, {

        })
    }

    fun initTakingOrderSetting() {
        tv_home_take_setting.setOnClickListener {
            val pop = TakingOrderSettingPop(context!!, mPresenter?.getModelData()!!)
            pop.setOnClickListener {
                mPresenter?.updateSetting(it)
            }
            pop.showPopupWindow()
        }
        homeRefreshBtn.setOnClickListener {
            refreshListData()
        }
        btn_home_work.setOnClickListener {
            val dialog = HomeConfirmDialog(
                context,
                DialogHomeConfirmClick { value: Boolean ->
                    if (value) {
                        if (Constant.WORKSTATES == Constant.WORK_STATES_OF_REST) {
                            mPresenter?.updateWorkStatus(Constant.WORK_STATES_OF_WORK)
                        } else {
                            mPresenter?.updateWorkStatus(Constant.WORK_STATES_OF_REST)
                        }
                    }
                },
                "温馨提示",
                if (Constant.WORKSTATES == Constant.WORK_STATES_OF_REST) "上班后将会接受派单，确定要上班吗？" else "下班后将不能再接到派单，确定要下班吗？",
                "取消",
                "确定"
            )
            dialog.show()
        }
    }

    private fun initTabLayout() {
        // 待接单
        tvTabVie.setOnClickListener {
            viewPager.currentItem =
                DispatchTabEvent.getTabIndexByStatus(DispatchConstants.DISPATCH_STATUS_VIE);
            EventBus.getDefault()
                .post(RefreshDispatchStatusEvent(DispatchConstants.DISPATCH_STATUS_VIE));
        }
        // 待接单
        tvTabAgreeing.setOnClickListener {
            viewPager.currentItem =
                DispatchTabEvent.getTabIndexByStatus(DispatchConstants.DISPATCH_STATUS_AGREEING);
            EventBus.getDefault()
                .post(RefreshDispatchStatusEvent(DispatchConstants.DISPATCH_STATUS_AGREEING));
        }
        // 待接货
        tvTabTaking.setOnClickListener {
            viewPager.currentItem =
                DispatchTabEvent.getTabIndexByStatus(DispatchConstants.DISPATCH_STATUS_TAKING);
            EventBus.getDefault()
                .post(RefreshDispatchStatusEvent(DispatchConstants.DISPATCH_STATUS_TAKING));
        }
        // 待送达
        tvTabDelivering.setOnClickListener {
            viewPager.currentItem =
                DispatchTabEvent.getTabIndexByStatus(DispatchConstants.DISPATCH_STATUS_DELIVERING);
            EventBus.getDefault()
                .post(RefreshDispatchStatusEvent(DispatchConstants.DISPATCH_STATUS_DELIVERING));
        }
        tvTabList.add(tvTabVie);
        tvTabList.add(tvTabAgreeing);
        tvTabList.add(tvTabTaking);
        tvTabList.add(tvTabDelivering);

        mFragments.add(OrderListFragment.vie(mPresenter?.getModelData()!!));
        mFragments.add(DispatchListFragment.agreeing(mPresenter?.getModelData()!!));
        mFragments.add(DispatchListFragment.taking(mPresenter?.getModelData()!!));
        mFragments.add(DispatchListFragment.delivering(mPresenter?.getModelData()!!));

        val fragmentAdapter = DispatchPageAdapter(childFragmentManager, mFragments, mTabTitles);
        viewPager.adapter = fragmentAdapter;
        viewPager.addOnPageChangeListener(mPageChangeListener);
        viewPager.offscreenPageLimit = 1;
    }

    val mPageChangeListener = object : ViewPager.OnPageChangeListener {
        /**
         * ViewPager.OnPageChangeListener
         */
        override fun onPageScrollStateChanged(state: Int) {

        }

        /**
         * ViewPager.OnPageChangeListener
         */
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        /**
         * ViewPager.OnPageChangeListener
         */
        override fun onPageSelected(position: Int) {
            updateTab(position);
        }
    }

    private fun setTabStatus(tabIndex: Int) {
        tvTabVie.setTabSelected(false)
        tvTabAgreeing.setTabSelected(false)
        tvTabTaking.setTabSelected(false)
        tvTabDelivering.setTabSelected(false)
        viewPager.currentItem = tabIndex;
        tvTabList[tabIndex].setTabSelected(true)
    }

    private fun refreshListData() {
        EventBus.getDefault().post(RefreshDispatchStatusEvent(viewPager.currentItem))
    }

    override fun loadTabNumberSuccess(data: DispatchNumberBean?) {
        tvTabAgreeing.setTabNumber(data?.receivedNum!!);
        tvTabTaking.setTabNumber(data?.pickupNum!!);
        tvTabDelivering.setTabNumber(data?.arriveNum!!);
    }

    override fun updateWorkStatusSuccess() {
        Constant.WORKSTATES =
            if (Constant.WORKSTATES == Constant.WORK_STATES_OF_REST) Constant.WORK_STATES_OF_WORK else Constant.WORK_STATES_OF_REST;
        setWorkStatus();
    }

    override fun loadedDispatchData(data: DispatchOrderRecordBean) {
        if (data?.id == null || data?.id?.isEmpty() == true) {
            return;
        }
        if (data?.id?.isNotBlank() == true) {
            if (viewPager.currentItem != 0) {
                viewPager.currentItem = 0;
            }
            if (data?.isTrucker()) {
                VoiceUtils.playVoiceOfFourModel()
                EventBus.getDefault().post(
                    DispatchNewOrderEvent(
                        DispatchConstants.DISPATCH_STATUS_AGREEING,
                        data?.id!!
                    )
                );
            } else {
                VoiceUtils.playVoiceOfTwoModel()
                if (mPushDialog?.isShowing == true) {
                    mPushDialog?.dismiss()
                }
                mPushDialog = HomePushDialog(
                    context,
                    click,
                    data
                )
                mPushDialog?.show()
            }
        }
    }

    private fun setWorkStatus() {
        if (Constant.WORKSTATES == Constant.WORK_STATES_OF_WORK) {
//            val drawable: Drawable = activity.getDrawable()
//            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            btn_home_work.text = "收工"
            btn_home_work.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_rest, 0, 0, 0)
        } else {
            btn_home_work.text = "上班"
            btn_home_work.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_rider, 0, 0, 0)
        }
    }

    override fun updateSettingSuccess(string: String?, it: HomeModelEvent?) {
        if (it != null) {
            UserManager.setTakingModel(it);
            Constant.Home_Model_Event = it;
            refreshListData();
        }
    }

    override fun updateSettingFailed(str: String?, it: HomeModelEvent?) {
//        if(str?.isNotBlank() == true) {
//            showToast(str);
//        }
        if (it != null) {
            UserManager.setTakingModel(it);
            Constant.Home_Model_Event = it;
            refreshListData();

            loadTabNumber();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun toSearch(event: MainToSearchEvent) {
        if (activity != null) {
            SearchActivity.newInstance(activity!!, viewPager.currentItem, 100)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshDispatchTabNumber(event: DispatchTabEvent) {
        viewPager.setCurrentItem(event?.getTabIndex(), false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshDispatchTabNumber(event: DispatchSingleNumberEvent) {
        when (event?.status) {
            -1 -> {
                loadTabNumber();
            }
            DispatchConstants.DISPATCH_STATUS_AGREEING -> {
                tvTabAgreeing.setTabNumber(event?.number);
            }
            DispatchConstants.DISPATCH_STATUS_TAKING -> {
                tvTabTaking.setTabNumber(event?.number);
            }
            DispatchConstants.DISPATCH_STATUS_DELIVERING -> {
                tvTabDelivering.setTabNumber(event?.number);
            }
        }
    }

    /**
     * 收到推送消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun search(event: PublicBean) {
        if (event.message != null && event.message != "") {
            val param =
                Gson().fromJson(event.message, PushBean::class.java)
            if (param.type == 1) {
                mPresenter?.loadDispatchData(param.dispatchId);
            } else if (param.type == 3) {
                // 抢单
                if (param?.order != null) {
                    VoiceUtils.playVoiceOfNewOrder()
                    if (mTakeOrderDialog?.isShowing == true) {
                        mTakeOrderDialog?.dismiss()
                    }
                    mTakeOrderDialog = TakeOrderDialog(
                        context,
                        takeOrderClick,
                        param?.order!!
                    )
                    mTakeOrderDialog?.show()
                }
            }
        } else if (event.code == 12) {  //获取相关订单数量
            loadTabNumber();
        } else if (event.code == 20) {
            updateTab(0)
        } else if (event.code == 21) {
            updateTab(1)
        } else if (event.code == 10) {
            refreshListData();
        }
    }

    private fun updateTab(position: Int) {
        setTabStatus(position);
        refreshListData();
    }

    private val click: HomePushDialog.DialogPushConfirmClick =
        object : HomePushDialog.DialogPushConfirmClick {
            override fun sure(id: kotlin.String) {
                val dialog = HomeConfirmDialog(
                    activity,
                    DialogHomeConfirmClick { value: kotlin.Boolean? ->
                        if (value == true) {
                            mPushDialog?.dismiss()
                            agreeAccept(id)
                        }
                    }, "接单提示", "请确认是否接单配送？", "取消", "确认接单"
                )
                dialog.show()
            }

            override fun refuse(id: kotlin.String) {
                mPushDialog?.dismiss()
                startActivity(Intent(context, RejectionActivity::class.java).putExtra("id", id))
            }
        }

    private val takeOrderClick: TakeOrderDialog.DialogPushConfirmClick =
        object : TakeOrderDialog.DialogPushConfirmClick {
            override fun sure(id: kotlin.String) {
                val dialog = HomeConfirmDialog(
                    activity,
                    DialogHomeConfirmClick { value: kotlin.Boolean? ->
                        if (value == true) {
                            mTakeOrderDialog?.dismiss()
                            agreeAndAccept(id)
                        }
                    }, "抢单提示", "请确认是否接单配送？", "取消", "确认抢单"
                )
                dialog.show()
            }

            override fun refuse(id: kotlin.String) {
                mTakeOrderDialog?.dismiss()
                startActivity(Intent(context, RejectionActivity::class.java).putExtra("id", id))
            }
        }

    fun agreeAndAccept(id: String) {
        mPresenter?.takeOrder(id, {
            showToast("抢单成功!")
            refreshListData();
            loadTabNumber();
        }, {
        });
    }

    fun agreeAccept(id: String) {
        mPresenter?.sureOrder(id, {
            refreshListData();
            loadTabNumber();
        }, {
        });
    }
}