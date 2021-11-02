package com.lingmiao.distribution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.lingmiao.distribution.base.UserManager;
import com.fisheagle.mkt.business.photo.PhotoHelper;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.LoginBean;
import com.lingmiao.distribution.bean.PersonalBean;
import com.lingmiao.distribution.bean.PersonalDataParam;
import com.lingmiao.distribution.bean.UploadBean;
import com.lingmiao.distribution.common.commonpop.bean.RegionVo;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityPersonalInfoBinding;
import com.lingmiao.distribution.dialog.ListDialog;
import com.lingmiao.distribution.imageloader.GlideImageLoader;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.ui.activity.presenter.PopAddressPreImpl;
import com.lingmiao.distribution.ui.common.bean.UploadDataBean;
import com.lingmiao.distribution.ui.common.pop.MediaMenuPop;
import com.lingmiao.distribution.util.GlideUtil;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.lingmiao.distribution.ui.photo.GalleryExtKt.convert2GalleryVO;

/**
 * 个人资料
 * niuxinyu
 */
public class PersonalInfoActivity extends ActivitySupport implements View.OnClickListener {
    private ActivityPersonalInfoBinding viewBinding;
    // 上传图片
    private ArrayList<String> photoData = new ArrayList<>();
    private PersonalDataParam dataParam = null;
    private List<RegionVo> regionVos;
    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityPersonalInfoBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();
        initImagePicker();
        getUserInfo();
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

            @Override
            public void onRightTx() {//提交资料
                if(regionVos == null) {
                    ToastUtil.showToast(context, "请选择配送区域");
                    return;
                }
                submit(viewBinding.piNick.getText().toString());
            }
        }, true, "个人资料", 0, "保存");
        viewBinding.piHeadImg.setOnClickListener(this);
        viewBinding.piCardBackImg.setOnClickListener(this);
        viewBinding.tvAre.setOnClickListener(this);
    }

    /**
     * 获取个人资料数据
     */
    private void getUserInfo() {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAync(Constant.AppUserInfoDetail, new Gson().toJson(mMap), new HttpCallback<PersonalBean>() {
            @Override
            public void onSuccess(PersonalBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null && response.getData().getRider() != null) {
                    PersonalDataParam data = response.getData().getRider();
                    dataParam = data;
                    UserManager.Companion.setUserInfo(data);
                    viewBinding.piCardCode.setText(data.getIdCard());
                    viewBinding.piName.setText(data.getName());
                    viewBinding.piNick.setText(data.getNickName());
                    viewBinding.piPhone.setText(data.getMobile());
                    viewBinding.tvAre.setText(data.getDistrictAra());
                    GlideUtil.load(context, data.getHeadImgUrl(), viewBinding.piHeadImg, GlideUtil.getHeadImgOption());
                    GlideUtil.load(context, data.getIdCardBackUrl(), viewBinding.piCardBackImg, GlideUtil.getRoundedOption(20));
                    GlideUtil.load(context, data.getIdCardFrontUrl(), viewBinding.piCardFrontImg, GlideUtil.getRoundedOption(20));
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 提交修改信息
     * 仅头像 昵称可修改
     */
    private void submit(String nickName) {
        if (!checkInput(nickName)) {
            return;
        }
        if(!checkInput(province, "请选择配送区域信息") || !checkInput(city, "请选择配送区域信息") || !checkInput(district, "请选择配送区域信息")) {
            return;
        }
        String id = UserManager.Companion.getPushID();
        Map<String, String> mMap = new HashMap<>();
        mMap.put("headImgUrl", dataParam != null ? dataParam.getHeadImgUrl() : "");
        mMap.put("id", id);
        mMap.put("nickName", nickName);
        mMap.put("provinceName", province);
        mMap.put("cityName", city);
        mMap.put("districtName", district);
        OkHttpUtils.postAync(Constant.AppUpdateRider, new Gson().toJson(mMap), new HttpCallback<LoginBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(LoginBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals("0")) {
                    setResult(200);
                    finish();
                }
            }
        });
    }

    /**
     * 检验输入框内容
     */
    private boolean checkInput(String nick) {
        return checkInput(nick, "请输入昵称！");
    }

    private boolean checkInput(String nick, String hint) {
        if (!InputUtil.isEmpty(this, nick, hint)) {
            return false;
        }
        return true;
    }


    String province, city, district;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pi_head_img://点击头像
//                chooseImage(10010);
                showGallery(10010);
                break;
            case R.id.pi_card_back_img:
            case R.id.tv_are:
                address.showAddressPop(context, (str, list) -> {
                    this.regionVos = list;
                    viewBinding.tvAre.setText(str);
                    if(list != null && list.size() >= 3) {
                        province = list.get(0).getIName();
                        city = list.get(1).getIHint();
                        district = list.get(2).getIHint();
                    }
                    return null;
                });
                break;
            default:
                break;

        }
    }

    PopAddressPreImpl address = new PopAddressPreImpl();

    /**
     * 初始化图片选择器
     */
    private void initImagePicker() {
        photoData.add("1@拍照");
        photoData.add("2@相册");
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());     //设置图片加载器
        imagePicker.setMultiMode(false);                         //设置多选模式
        imagePicker.setShowCamera(false);                       //相册里隐藏拍照按钮
    }

    /**
     * 选择照片方法
     */
    private void chooseImage(int code) { //10010正面  10011反面
        ListDialog dialog = new ListDialog(context, (position, id, text, chooseIndex) -> {
            if (id.equals("1")) {//1:拍照   2：相册
                startActivityForResult(new Intent(context, ImageGridActivity.class).putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true), code);
            } else if (id.equals("2")) {
                startActivityForResult(new Intent(context, ImageGridActivity.class), code);
            }
        }, photoData, 0, true);
        dialog.show();
    }

    private void showGallery(int code) {
        int popType = MediaMenuPop.TYPE_SELECT_PHOTO | MediaMenuPop.TYPE_PLAY_PHOTO;
        MediaMenuPop pop = new MediaMenuPop(context, popType);
        pop.setOnClickListener((value)->{
            if(value == MediaMenuPop.TYPE_SELECT_PHOTO) {
                PhotoHelper.openAlbum(PersonalInfoActivity.this, UploadDataBean.MAX_ONCE_PICKER_COUNT, null,(it)->{
                    addDataList(convert2GalleryVO(it), code);
                    return null;
                });
            } else if(value == MediaMenuPop.TYPE_PLAY_PHOTO) {
                PhotoHelper.openCamera(PersonalInfoActivity.this, null,(it) -> {
                    addDataList(convert2GalleryVO(it), code);
                    return null;
                });
            }
            return null;
        });
        pop.showPopupWindow();
    }


    private void addDataList(List<UploadDataBean> list, int code) {
        if (list == null || list.size() == 0) {
            return;
        }
        compressAndUpload(list.get(0), code);
    }


    private void compressAndUpload(UploadDataBean item, int request) {
        File mFile = new File(item.getPath());
        File imageFile = new File(item.getPath());
        Luban.with(context).load(imageFile).ignoreBy(100).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(File file) {
                uploadSubmit(mFile, request);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast(context, "图片压缩失败，请重试！");
            }
        }).launch();
    }

    /**
     * 图片上传服务端素材库
     */
    private void uploadSubmit(File path, int requestCode) {//10010正面  10011反面
        List<File> mFile = new ArrayList<>();
        mFile.add(path);
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAsynFiles(Constant.AppUploadFile, "uploadFile", mFile, mMap, new HttpCallback<UploadBean>(this, getProgressDialog()) {
            @Override
            public void onSuccess(UploadBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    GlideUtil.load(context, response.getData().getUrl(), viewBinding.piHeadImg, GlideUtil.getHeadImgOption());
                    if (dataParam != null) {
                        dataParam.setHeadImgUrl(response.getData().getUrl());
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && (requestCode == 10010 || requestCode == 10011)) {//图片选择回调
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null) {
                File imageFile = new File(images.get(0).path);
                Luban.with(context).load(imageFile).ignoreBy(100).setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        uploadSubmit(file, requestCode);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(context, "图片压缩失败，请重试！");
                    }
                }).launch();
            }
        }
    }


}