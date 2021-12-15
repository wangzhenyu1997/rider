package com.lingmiao.distribution.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;

import com.google.gson.Gson;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicListBean;
import com.lingmiao.distribution.bean.BasicListParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityAboutUsBinding;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AboutUsActivity
 *
 * @author yandaocheng <br/>
 * 关于我们
 * 2020-07-15
 * 修改者，修改日期，修改内容
 */
public class AboutUsActivity extends ActivitySupport {

    private ActivityAboutUsBinding viewBinding;
    private List<BasicListParam> mListData = new ArrayList<>();//列表数据
    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityAboutUsBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();
        getData();
        getTel();
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
        }, true, "关于我们", 0, "");
    }

    /**
     * 获取数据
     */
    private void getData() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("type", "1");
        OkHttpUtils.postAync(Constant.AppQueryContentList, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if(response.getData()!=null&&response.getData().size()!=0){
                        BasicListParam contentOnj=response.getData().get(0);
                        viewBinding.abuContext.setText(contentOnj.getContent());
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }


    /**
     * 获取服务热线
     */
    private void getTel() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("type", "5");
        OkHttpUtils.postAync(Constant.AppQueryContentList, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    mListData.addAll(response.getData());
                    String text="";
                    for(int i=0;i<response.getData().size();i++){
                        text+=response.getData().get(i).getContent()+"<br>";
                    }
                    viewBinding.abuTel.setText(Html.fromHtml(text));
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

}