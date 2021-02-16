package com.lingmiao.distribution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.fisheagle.mkt.business.photo.PhotoHelper;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.adapter.UploadImageAdapter;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicBean;
import com.lingmiao.distribution.bean.BasicListBean;
import com.lingmiao.distribution.bean.BasicListParam;
import com.lingmiao.distribution.bean.Filepath;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.bean.UploadImageBean;
import com.lingmiao.distribution.bean.UploadImageParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityReportExceptionBinding;
import com.lingmiao.distribution.dialog.DialogShowImageFragment;
import com.lingmiao.distribution.dialog.ListDialog;
import com.lingmiao.distribution.imageloader.GlideImageLoader;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.ui.common.bean.UploadDataBean;
import com.lingmiao.distribution.ui.common.pop.MediaMenuPop;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.PublicUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.lingmiao.distribution.ui.photo.GalleryExtKt.convert2GalleryVO;

/**
 * 上报异常
 * niuxinyu
 */
public class ReportExceptionActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityReportExceptionBinding viewBinding;
    private UploadImageAdapter mUpLoadeAdapter;
    private ArrayList<Filepath> mFilePathList = new ArrayList<>();

    private ArrayList<String> photoData = new ArrayList<>();
    private ImagePicker imagePicker;
    private TimePickerView mTimeChoose;         //时间选择器
    private String mId;  //主键ids

    private String mExceptionTypeId = "";
    private List<String> mExceptionData = new ArrayList<>();//异常数据
    private ListDialog dialog;

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityReportExceptionBinding.inflate(LayoutInflater.from(this));
        mId = getIntent().getStringExtra("id");
        setContentView(viewBinding.getRoot());
        initView();
        getExceptionType();
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
        }, true, "上报异常", 0, "");
        initLunarPicker();
        initImagePicker();
        mUpLoadeAdapter = new UploadImageAdapter(this, mFilePathList, 3, callback);
        viewBinding.acUploadGrid.setAdapter(mUpLoadeAdapter);
        viewBinding.abcSubmit.setOnClickListener(this);
        viewBinding.acExceptionType.setOnClickListener(this);
        viewBinding.acExceptionTime.setOnClickListener(this);
    }

    /**
     * 查询异常类型
     */
    private void getExceptionType() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("type", "exception_type");
        OkHttpUtils.postAync(Constant.AppGetListByType, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if (response.getData() != null && response.getData().size() != 0) {
                        for (BasicListParam param : response.getData()) {
                            mExceptionData.add(param.getValue() + "@" + param.getLabel());
                        }
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 初始化时间选择器
     */
    private void initLunarPicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2019, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2029, 11, 31);
        //时间选择器 ，自定义布局
        mTimeChoose = new TimePickerBuilder(context, (date, v) -> {//选中事件回调
            if (v.getId() == R.id.ac_exception_time) {
                viewBinding.acExceptionTime.setText(PublicUtil.getTime(date, "yyyy-MM-dd HH:mm:ss"));
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLayoutRes(R.layout.pickerview_custom_lunar, v -> {
                    final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                    ImageView ivCancel = v.findViewById(R.id.iv_cancel);
                    tvSubmit.setOnClickListener(v1 -> {
                        mTimeChoose.returnData();
                        mTimeChoose.dismiss();
                    });
                    ivCancel.setOnClickListener(v12 -> mTimeChoose.dismiss());
                })
                .isDialog(true)
                .build();
    }

    /**
     * 初始化图片选择器
     */
    private void initImagePicker() {
        photoData.add("1@拍照");
        photoData.add("2@相册");
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());      //设置图片加载器
        imagePicker.setMultiMode(true);                          //设置单选模式
        imagePicker.setSelectLimit(3);                           //选中数量限制
    }

    /**
     * 相册选择弹窗
     */
    private void SelectDialog(final int code) {
        ListDialog dialog = new ListDialog(this, (position, id, text, chooseIndex) -> {//1:拍照   2：相册
            if (id.equals("1")) {
                startActivityForResult(new Intent(context, ImageGridActivity.class).putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true), code);
            } else if (id.equals("2")) {
                startActivityForResult(new Intent(context, ImageGridActivity.class), code);
            }
        }, photoData, 0);
        dialog.show();
    }

    private void addDataList(List<UploadDataBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        compressAndUpload(list);
    }

    private void showGallery() {
        int popType = MediaMenuPop.TYPE_SELECT_PHOTO | MediaMenuPop.TYPE_PLAY_PHOTO;
        MediaMenuPop pop = new MediaMenuPop(context, popType);
        pop.setOnClickListener((value)->{
            if(value == MediaMenuPop.TYPE_SELECT_PHOTO) {
                PhotoHelper.openAlbum(ReportExceptionActivity.this, UploadDataBean.MAX_ONCE_PICKER_COUNT, null,(it)->{
                    addDataList(convert2GalleryVO(it));
                    return null;
                });
            } else if(value == MediaMenuPop.TYPE_PLAY_PHOTO) {
                PhotoHelper.openCamera(ReportExceptionActivity.this, null,(it) -> {
                    addDataList(convert2GalleryVO(it));
                    return null;
                });
            }
            return null;
        });
        pop.showPopupWindow();
    }
    /**
     * 上传图片添加监听
     */
    private UploadImageAdapter.SelectPicCallBack callback = new UploadImageAdapter.SelectPicCallBack() {
        @Override
        public void addImg(int position) {
            if (position == mFilePathList.size()) {
                showGallery();
//                SelectDialog(200);
            } else {
                ArrayList<String> mShowData = new ArrayList<>();
                for (Filepath param : mFilePathList) {
                    mShowData.add(param.getFilePath());
                }
                DialogShowImageFragment imageFragment = new DialogShowImageFragment();
                imageFragment.setData(mShowData, position);
                imageFragment.show(getSupportFragmentManager(), null);
            }
        }

        // 删除图片
        @Override
        public void deleteImg(int position) {
            mFilePathList.remove(position);
            mUpLoadeAdapter.notifyDataSetChanged();
            imagePicker.setSelectLimit(3-mFilePathList.size());                           //选中数量限制
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.abc_submit://点击提交
                if (!InputUtil.isEmpty(context, mExceptionTypeId, "请选择异常类型！")) return;
                if (!InputUtil.isEmpty(context, viewBinding.acExceptionTime.getText().toString(), "请选择异常时间！"))
                    return;
                if (!InputUtil.isEmpty(context, viewBinding.mDiscription.getText().toString(), "请输入异常描述！"))
                    return;
                if (mFilePathList.size() == 0) {
                    ToastUtil.showToast(context, "请上传照片！");
                    return;
                }
                submit();
                break;
            case R.id.ac_exception_type:        //异常类型
                if (mExceptionData.size() == 0) {
                    ToastUtil.showToast(context, "暂无异常类型！");
                    return;
                }
                if (dialog == null)
                    dialog = new ListDialog(context, (position, id, text, chooseIndex) -> {
                        mExceptionTypeId = id;
                        viewBinding.acExceptionType.setText(text);
                    }, mExceptionData, 0);
                dialog.show();
                break;
            case R.id.ac_exception_time:        //异常时间
                mTimeChoose.show(viewBinding.acExceptionTime);
                break;
            default:
                break;
        }
    }


    /**
     * 提交数据
     */
    private void submit() {
        StringBuilder mUrls = new StringBuilder();
        for (Filepath param : mFilePathList) {
            if (mUrls.toString().equals("")) {
                mUrls = new StringBuilder(param.getFilePath());
            } else {
                mUrls.append(",").append(param.getFilePath());
            }
        }
        Map<String, String> mMap = new HashMap<>();
        mMap.put("content", viewBinding.mDiscription.getText().toString());
        mMap.put("dispatchId", mId);
        mMap.put("exceptionType", mExceptionTypeId);
        mMap.put("exceptionTime", viewBinding.acExceptionTime.getText().toString());
        mMap.put("urls", mUrls.toString());
        OkHttpUtils.postAync(Constant.AppUploadException, new Gson().toJson(mMap), new HttpCallback<BasicBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicBean response) {
                super.onSuccess(response);
                ToastUtil.showToast(context, response.getMessage());
                if (response.getCode().equals(Constant.SUCCESS)) {
                    EventBus.getDefault().post(new PublicBean(10));
                    EventBus.getDefault().post(new PublicBean(11));
                    EventBus.getDefault().post(new PublicBean(12));
                    finish();
                }
            }
        });
    }

    /**
     * 图片上传服务端素材库
     */
    private void uploadSubmit(List<File> file) {
        Map<String, String> mMap = new HashMap<>();
        OkHttpUtils.postAsynFiles(Constant.AppUploadMoreImage, "uploadImages", file, mMap, new HttpCallback<UploadImageBean>(this, getProgressDialog()) {
            @Override
            public void onSuccess(UploadImageBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    for (UploadImageParam param : response.getData()) {
                        Filepath path = new Filepath(param.getUrl(), param.getId());
                        mFilePathList.add(path);
                    }
                    mUpLoadeAdapter.notifyDataSetChanged();
                    imagePicker.setSelectLimit(3-mFilePathList.size());                           //选中数量限制
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
    }

    /**
     * 选择图片后回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && requestCode == 200) {//添加图片返回
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null) {
                final int[] mFailNums = {0};
                List<File> mFile = new ArrayList<>();
                for (ImageItem param : images) {
                    File imageFile = new File(param.path);
                    Luban.with(context).load(imageFile).ignoreBy(100).setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            mFile.add(file);
                            if ((images.size() - mFailNums[0] == mFile.size()) && mFile.size() != 0) {
                                uploadSubmit(mFile);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mFailNums[0]++;
                            ToastUtil.showToast(context, "图片压缩失败，请重试！");
                        }
                    }).launch();
                }
            }
        }
    }

    private void compressAndUpload(List<UploadDataBean> list) {
        List<File> mFile = new ArrayList<>();
        final int[] mFailNums = {0};
        for(UploadDataBean item : list) {
            File imageFile = new File(item.getPath());
            Luban.with(context).load(imageFile).ignoreBy(100).setCompressListener(new OnCompressListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(File file) {
                    mFile.add(file);
                    if ((list.size() - mFailNums[0] == mFile.size()) && mFile.size() != 0) {
                        uploadSubmit(mFile);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    mFailNums[0]++;
                    ToastUtil.showToast(context, "图片压缩失败，请重试！");
                }
            }).launch();
        }
    }
}
