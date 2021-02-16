package com.lingmiao.distribution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.fisheagle.mkt.base.UserManager;
import com.fisheagle.mkt.business.photo.PhotoHelper;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicBean;
import com.lingmiao.distribution.bean.OcrBean;
import com.lingmiao.distribution.bean.UploadBean;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityIdentityAtBinding;
import com.lingmiao.distribution.dialog.ListDialog;
import com.lingmiao.distribution.imageloader.GlideImageLoader;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.ui.common.bean.UploadDataBean;
import com.lingmiao.distribution.ui.common.pop.MediaMenuPop;
import com.lingmiao.distribution.util.GlideUtil;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.PreferUtil;
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
 * IdentityAtActivity
 *
 * @author yandaocheng <br/>
 * 身份认证
 * 2020-07-11
 * 修改者，修改日期，修改内容
 */

public class IdentityAtActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityIdentityAtBinding viewBinding;
    // 上传图片
    private ArrayList<String> photoData = new ArrayList<>();

    private String idCardBackUrl = "";//正面身份证图片路径
    private String idCardFrontUrl = "";//反面身份证图片路径

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityIdentityAtBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initView();
        initImagePicker();
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
        }, true, "身份认证", 0, null);
        viewBinding.mCardPositionRe.setOnClickListener(this); //身份证正面点击
        viewBinding.mCardOppositeRe.setOnClickListener(this); //身份证反面点击
        viewBinding.mSubmit.setOnClickListener(this);
    }

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
//        imagePicker.setSelectLimit(mMaxLength);                         //选中数量限制
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
                PhotoHelper.openAlbum(IdentityAtActivity.this, UploadDataBean.MAX_ONCE_PICKER_COUNT, null,(it)->{
                    addDataList(convert2GalleryVO(it), code);
                    return null;
                });
            } else if(value == MediaMenuPop.TYPE_PLAY_PHOTO) {
                PhotoHelper.openCamera(IdentityAtActivity.this, null,(it) -> {
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
                    if (requestCode == 10010) {//正面
                        idCardBackUrl = response.getData().getUrl();
                        GlideUtil.load(context,response.getData().getUrl(), viewBinding.mCardPositionImage, GlideUtil.getOption());
                        viewBinding.mCardPositionImage.setVisibility(View.VISIBLE);
                        getOCR(response.getData().getUrl());
                    } else {
                        idCardFrontUrl = response.getData().getUrl();
                        GlideUtil.load(context,response.getData().getUrl(), viewBinding.mCardOppositeImage, GlideUtil.getOption());
                        viewBinding.mCardOppositeImage.setVisibility(View.VISIBLE);
                    }
                }else{
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * ocr识别
     *
     * @param filePath
     */
    private void getOCR(String filePath) {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("discernType", "1");
        mMap.put("imageUrl", filePath);
        OkHttpUtils.postAync(Constant.AppOcrDiscern, new Gson().toJson(mMap), new HttpCallback<OcrBean>() {
            @Override
            public void onSuccess(OcrBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS) && response.getData() != null) {
                    viewBinding.mName.setText(response.getData().getName());
                    viewBinding.mNumber.setText(response.getData().getIdNum());
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 提交认证
     */
    private void mSubmit() {
        String name = viewBinding.mName.getText().toString();
        String cardId = viewBinding.mNumber.getText().toString();
        String id = UserManager.Companion.getPushID();
        if (!checkInput(name, cardId)) return;
        Map<String, String> mMap = new HashMap<>();
        mMap.put("id", id);
        mMap.put("idCard", cardId);
        mMap.put("idCardBackUrl", idCardBackUrl);
        mMap.put("idCardFrontUrl", idCardFrontUrl);
        mMap.put("name", name);
        OkHttpUtils.postAync(Constant.AppSubmitIdCardAudit, new Gson().toJson(mMap), new HttpCallback<BasicBean>() {
            @Override
            public void onSuccess(BasicBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    startActivity(new Intent(context, IdentityExamineActivity.class));
                    finish();
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 检验输入框内容
     */
    private boolean checkInput(String name, String cardId) {
        if (!InputUtil.isEmpty(this, idCardBackUrl, "请上传身份证信息！")) {
            return false;
        }
        if (!InputUtil.isEmpty(this, idCardFrontUrl, "请上传身份证信息！")) {
            return false;
        }
        if (!InputUtil.isEmpty(this, name, "请输入姓名！")) {
            return false;
        }
        if (!InputUtil.isEmpty(this, cardId, "请输入身份证号码！")) {
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_submit:             //提交
                mSubmit();
                break;
            case R.id.m_card_position_re:   //身份证正面
//                chooseImage(10010);
                showGallery(10010);
                break;
            case R.id.m_card_opposite_re:   //反面
//                chooseImage(10011);
                showGallery(10011);
                break;
            default:
                break;
        }
    }
}

