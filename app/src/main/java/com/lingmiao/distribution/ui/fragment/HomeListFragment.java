package com.lingmiao.distribution.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lingmiao.distribution.base.UserManager;
import com.github.nrecyclerview.interfaces.LoadingListener;
import com.google.gson.Gson;
import com.lingmiao.distribution.adapter.HomeBatchOrderAdapter;
import com.lingmiao.distribution.adapter.HomeListAdapter;
import com.lingmiao.distribution.app.FragmentSupport;
import com.lingmiao.distribution.bean.HomeBean;
import com.lingmiao.distribution.bean.HomeModelEvent;
import com.lingmiao.distribution.bean.HomeParam;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.FragmentHomeListBinding;
import com.lingmiao.distribution.dialog.HomeConfirmDialog;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.ui.activity.BatchDetailActivity;
import com.lingmiao.distribution.ui.activity.ComplaintActivity;
import com.lingmiao.distribution.ui.activity.EvaluateCustomerActivity;
import com.lingmiao.distribution.ui.activity.FailedToSignInActivity;
import com.lingmiao.distribution.ui.activity.FailedToSignInTwoActivity;
import com.lingmiao.distribution.ui.activity.HomeActivity;
import com.lingmiao.distribution.ui.activity.HomeDetailActivity;
import com.lingmiao.distribution.ui.activity.MapActivity;
import com.lingmiao.distribution.ui.activity.RejectionActivity;
import com.lingmiao.distribution.ui.activity.ReportExceptionActivity;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HomeListFragment
 *
 * @author yandaocheng <br/>
 * 配送订单
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class HomeListFragment extends FragmentSupport {

    private FragmentHomeListBinding viewBinding;

    private Boolean firstShow = false;    //是否已初始化
    private HomeListAdapter mAdapter;
    private List<HomeParam> mData = new ArrayList<>();

    private int mPage = 1;
    private HomeListAdapter.OnHomeListener listener;

    private HomeBatchOrderAdapter mBatchAdapter;

    private HomeModelEvent mListModel;

    public static Fragment getInstance(int index, int startPosition) {
        Fragment fragment = new HomeListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        index = getArguments().getInt("index");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentHomeListBinding.inflate(LayoutInflater.from(getActivity()));
        EventBus.getDefault().register(this);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstShow = true;

        mListModel = UserManager.Companion.getTakingModel();
        initView();
        if (getArguments().getInt("index") == 0) {
            initData();
        }
    }

    // 当页面显示时加载数据（避免预加载，viewpger切换时加载数据）
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && firstShow) {
            initData();
        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        listener = new HomeListAdapter.OnHomeListener() {
            @Override
            public void sureOrder(String id) { //确认接单
                HomeConfirmDialog dialog = new HomeConfirmDialog(getActivity(), value -> {
                    if (value) {
                        agreeAccept(id);
                    }
                }, "接单提示", "请确认是否接单配送？", "取消", "确认接单");
                dialog.show();
            }

            @Override
            public void refuseOrder(String id) { //拒绝接单
                startActivity(new Intent(getActivity(), RejectionActivity.class).putExtra("id", id));
            }

            @Override
            public void upAbnormal(String id) { //上报异常
                startActivity(new Intent(getActivity(), ReportExceptionActivity.class).putExtra("id", id));
            }

            @Override
            public void pickGood(String id) { //到达取货点
                HomeConfirmDialog dialog = new HomeConfirmDialog(getActivity(), value -> {
                    if (value) {
                        arriveShop(id);
                    }
                }, "到达提示", "请确认是否已到达取货点？", "取消", "确认到达");
                dialog.show();
            }

            @Override
            public void pickFail(String id) { //取货失败
                startActivity(new Intent(getContext(), FailedToSignInActivity.class).putExtra("id", id));
            }

            @Override
            public void pickSuccess(String id) { //取货成功
                HomeConfirmDialog dialog = new HomeConfirmDialog(getActivity(), value -> {
                    if (value) {
                        pickUp(id);
                    }
                }, "取货提示", "请确认是否已取货成功？", "取消", "确认成功");
                dialog.show();
            }

            @Override
            public void upAbnormalTwo(String id) { //上报异常Two
                startActivity(new Intent(getActivity(), ReportExceptionActivity.class).putExtra("id", id));
            }

            @Override
            public void receivingGoods(String id) { //到达收货点
                HomeConfirmDialog dialog = new HomeConfirmDialog(getActivity(), value -> {
                    if (value) {
                        arriveStation(id);
                    }
                }, "送达提示", "请确认是否已送达收货点？", "取消", "确认送达");
                dialog.show();
            }

            @Override
            public void signFail(String id) { //签收失败
                startActivity(new Intent(getContext(), FailedToSignInTwoActivity.class).putExtra("id", id));
            }

            @Override
            public void signSuccess(String id) { //签收成功
                HomeConfirmDialog dialog = new HomeConfirmDialog(getActivity(), value -> {
                    if (value) {
                        signedSuccess(id);
                    }
                }, "签收提示", "请确认是否已签收成功？", "取消", "确认成功");
                dialog.show();
            }

            @Override
            public void complaintCustomer(String orderId) { //投诉客户
                startActivity(new Intent(getContext(), ComplaintActivity.class).putExtra("id", orderId));
            }

            @Override
            public void evaluateCustomer(String orderId) { //评价客户
                startActivity(new Intent(getActivity(), EvaluateCustomerActivity.class).putExtra("id", orderId));
            }

            @Override
            public void itemClick(int position) { //订单详情&&前往取货&&前往送货
                if (mData.get(position).getDispatchStatus() == 3 || mData.get(position).getDispatchStatus() == 5) {  //3前往取货   5.前往送货
                    startActivity(new Intent(getActivity(), MapActivity.class).putExtra("data", mData.get(position)));
                } else {//跳转订单详细
                    startActivity(new Intent(getActivity(), HomeDetailActivity.class).putExtra("id", mData.get(position).getId()).putExtra("index", getArguments().getInt("index")));
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new HomeListAdapter(mData, getArguments().getInt("index"), listener, getActivity());


        mBatchAdapter = new HomeBatchOrderAdapter(mData, new HomeBatchOrderAdapter.OnListener() {

            @Override
            public void itemClick(int postition) {
                startActivity(new Intent(getActivity(), BatchDetailActivity.class).putExtra("id",mData.get(postition).getId()));
            }

            @Override
            public void takePositive(int position) {
                takeAllOrder(mData.get(position));
            }

            @Override
            public void takeNegative(int position) {

            }

            @Override
            public void clickPhone(String phone) {
                PublicUtil.callPhone(phone, getActivity());
            }
        });

        viewBinding.mRecyclerView.setLoadingListener(new LoadingListener() {
            @Override
            public void onLoadMore() {
                mPage += 1;
                getData();
            }

            @Override
            public void onRefresh() {
                super.onRefresh();
                initData();
            }
        });
        setAdapter();
    }

    private void takeAllOrder(HomeParam homeParam) {
        Map<String, String> mMap = new HashMap<>();

        OkHttpUtils.postAync(Constant.takeOrder, new Gson().toJson(mMap), new HttpCallback<HomeBean>(getActivity(), getProgressDialog()) {
            @Override
            public void onSuccess(HomeBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    initData();
                } else {

                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);

            }
        });
    }

    private void setAdapter() {
        if(mListModel.isDefaultModel()) {
            viewBinding.mRecyclerView.setAdapter(mAdapter);
        } else {
            viewBinding.mRecyclerView.setAdapter(mBatchAdapter);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mData.clear();
        mAdapter.notifyDataSetChanged();
        mBatchAdapter.notifyDataSetChanged();;
        mPage = 1;
        viewBinding.mRecyclerView.setNoMore(false);
        getData();
    }

    /**
     * 筛选
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void search(PublicBean event) {
//        firstShow = true;
        if (((HomeActivity) getActivity()).getIndex() == getArguments().getInt("index") && (event.getCode() == 10 || event.getCode() == 20 || event.getCode() == 21)) {
            initData();
        }
    }

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void shiftModel(HomeModelEvent e) {
        if(e != null) {
            this.mListModel = e;
            setAdapter();
//            if(mListModel.isDefaultModel()) {
//                viewBinding.mRecyclerView.setAdapter(mAdapter);
//            } else {
//                viewBinding.mRecyclerView.setAdapter(mBatchAdapter);
////                List<BatchOrderBean> list = new ArrayList<>();
////                list.add(new BatchOrderBean());
////                list.add(new BatchOrderBean());
////                list.add(new BatchOrderBean());
////                list.add(new BatchOrderBean());
////                list.add(new BatchOrderBean());
////                mBatchAdapter.setData(list);
//            }
            initData();
        }
    }

    private void getData() {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("pageNum", String.valueOf(mPage));
        mMap.put("pageSize", "10");
        Map<String, Object> mMapState = new HashMap<>();
        List<Integer> mState = new ArrayList<>();
        assert getArguments() != null;
        if (getArguments().getInt("index") == 0) {
//            mState.add(0);
            mState.add(2);
        } else if (getArguments().getInt("index") == 1) {
            mState.add(3);
            mState.add(4);
        } else {
            mState.add(5);
            mState.add(6);
//            mState.add(7);
//            mState.add(8);
//            mState.add(9);
        }
        mMapState.put("dispatchStatusArray", mState);
        mMapState.put("longitude", Constant.LOCATIONLONGITUDE);
        mMapState.put("latitude", Constant.LOCATIONLATITUDE);
        mMapState.put("workStatus", mListModel.getWorkStatus());
        mMapState.put("deliverySort", mListModel.getDeliveryOrder());
        mMapState.put("pickOrder", mListModel.getPickOrder());
        mMapState.put("sort", 1);
        mMap.put("body", mMapState);
        OkHttpUtils.postAync(Constant.AppDispathList, new Gson().toJson(mMap), new HttpCallback<HomeBean>(getActivity(), getProgressDialog()) {
            @Override
            public void onSuccess(HomeBean response) {
                super.onSuccess(response);
                viewBinding.mRecyclerView.refreshComplete();//刷新成功
                viewBinding.mRecyclerView.loadMoreComplete();//加载成功
                if (response.getData() != null && response.getData().getTotalCount() == 0) {
                    viewBinding.mRecyclerView.setFootViewText("", "暂无数据");
                }else{
                    viewBinding.mRecyclerView.setFootViewText("", "没有更多了");
                }
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().getRecords() != null && response.getData().getRecords().size() != 0) {
                    mData.addAll(response.getData().getRecords());
                    if (mData.size() == response.getData().getTotalCount()) {
                        viewBinding.mRecyclerView.setNoMore(true);//没有更多了
                    }
                    mAdapter.notifyDataSetChanged();
                    mBatchAdapter.notifyDataSetChanged();
                } else {
                    viewBinding.mRecyclerView.setNoMore(true);//没有更多了
                }
//                firstShow = false;
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                firstShow = true;
                viewBinding.mRecyclerView.refreshComplete();//刷新成功
                viewBinding.mRecyclerView.loadMoreComplete();//加载成功
            }
        });
    }

    /**
     * 确认接单
     */
    private void agreeAccept(String id) {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("id", id);
        OkHttpUtils.postAync(Constant.AppAgreeAccept, new Gson().toJson(mMap), new HttpCallback<LoginBean>(getActivity(), getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(getActivity(), response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    initData();
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
        OkHttpUtils.postAync(Constant.AppArriveShop, new Gson().toJson(mMap), new HttpCallback<LoginBean>(getActivity(), getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(getActivity(), response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    initData();
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
        OkHttpUtils.postAync(Constant.AppArriveStation, new Gson().toJson(mMap), new HttpCallback<LoginBean>(getActivity(), getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(getActivity(), response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    initData();
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
        OkHttpUtils.postAync(Constant.AppPickUp, new Gson().toJson(mMap), new HttpCallback<LoginBean>(getActivity(), getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(getActivity(), response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    initData();
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
        OkHttpUtils.postAync(Constant.AppSignedSuccess, new Gson().toJson(mMap), new HttpCallback<LoginBean>(getActivity(), getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(getActivity(), response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    initData();
                    EventBus.getDefault().post(new PublicBean(12));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
