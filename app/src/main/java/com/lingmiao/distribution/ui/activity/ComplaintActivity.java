package com.lingmiao.distribution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.fisheagle.mkt.business.photo.PhotoHelper;
import com.google.gson.Gson;
import com.lingmiao.distribution.R;
import com.lingmiao.distribution.adapter.UploadImageAdapter;
import com.lingmiao.distribution.adapter.recycleviewAdapter.CommonAdapter;
import com.lingmiao.distribution.adapter.recycleviewAdapter.ViewHolder;
import com.lingmiao.distribution.app.ActivitySupport;
import com.lingmiao.distribution.bean.BasicBean;
import com.lingmiao.distribution.bean.BasicListBean;
import com.lingmiao.distribution.bean.BasicListParam;
import com.lingmiao.distribution.bean.Filepath;
import com.lingmiao.distribution.bean.PublicBean;
import com.lingmiao.distribution.bean.UploadImageBean;
import com.lingmiao.distribution.bean.UploadImageParam;
import com.lingmiao.distribution.config.Constant;
import com.lingmiao.distribution.databinding.ActivityComplaintBinding;
import com.lingmiao.distribution.dialog.DialogShowImageFragment;
import com.lingmiao.distribution.dialog.ListDialog;
import com.lingmiao.distribution.imageloader.GlideImageLoader;
import com.lingmiao.distribution.okhttp.HttpCallback;
import com.lingmiao.distribution.okhttp.OkHttpUtils;
import com.lingmiao.distribution.ui.common.bean.UploadDataBean;
import com.lingmiao.distribution.ui.common.pop.MediaMenuPop;
import com.lingmiao.distribution.util.InputUtil;
import com.lingmiao.distribution.util.ToastUtil;
import com.lingmiao.distribution.view.LayoutTopView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.lingmiao.distribution.ui.photo.GalleryExtKt.convert2GalleryVO;

/**
 * 我要投诉
 * niuxinyu
 */
public class ComplaintActivity extends ActivitySupport implements View.OnClickListener {

    private ActivityComplaintBinding viewBinding;
    private UploadImageAdapter mUpLoadeAdapter;
    private ArrayList<Filepath> mFilePathList = new ArrayList<>();
    private ArrayList<String> photoData = new ArrayList<>();
    private ImagePicker imagePicker;

    private CommonAdapter<BasicListParam> mAdapter;
    private ArrayList<BasicListParam> mListData = new ArrayList<>();//列表数据
    private String mId;     //主键

    @Override
    protected void setCreateView(@Nullable Bundle savedInstanceState) {
        viewBinding = ActivityComplaintBinding.inflate(LayoutInflater.from(this));
        mId = getIntent().getStringExtra("id");
        setContentView(viewBinding.getRoot());
        initView();
        getReason();
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
        }, true, "我要投诉", 0, "");
        viewBinding.abcSubmit.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.mRefuseRecyclerview.setLayoutManager(linearLayoutManager);
        mAdapter = new CommonAdapter<BasicListParam>(this, R.layout.item_rejection_ac, mListData) {
            @Override
            protected void convert(ViewHolder holder, final BasicListParam s, int position) {
                if (position == mListData.size() - 1) {
                    holder.setVisible(R.id.bottom_view, false);
                } else {
                    holder.setVisible(R.id.bottom_view, true);
                }
                holder.setText(R.id.m_content, s.getLabel());
                holder.setChecked(R.id.m_content, s.isSelectState());
                holder.setOnClickListener(R.id.m_content, view -> {
                    if (s.isSelectState()) {
                        mListData.get(position).setSelectState(false);
                    } else {
                        for (BasicListParam param : mListData) {
                            param.setSelectState(false);
                        }
                        mListData.get(position).setSelectState(true);
                    }
                    mAdapter.notifyDataSetChanged();
                });
            }
        };
        viewBinding.mRefuseRecyclerview.setAdapter(mAdapter);

        initImagePicker();
        mUpLoadeAdapter = new UploadImageAdapter(this, mFilePathList, 3, callback);
        viewBinding.acUploadGrid.setAdapter(mUpLoadeAdapter);
    }

    /**
     * 查询可选拒绝原因
     */
    private void getReason() {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("type", "complaint_type");
        OkHttpUtils.postAync(Constant.AppGetListByType, new Gson().toJson(mMap), new HttpCallback<BasicListBean>(context, getProgressDialog()) {
            @Override
            public void onSuccess(BasicListBean response) {
                super.onSuccess(response);
                if (response.getCode().equals(Constant.SUCCESS)) {
                    if (response.getData() != null && response.getData().size() != 0) {
                        mListData.addAll(response.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtil.showToast(context, response.getMessage());
                }
            }
        });
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


    private void showGallery() {
        int popType = MediaMenuPop.TYPE_SELECT_PHOTO | MediaMenuPop.TYPE_PLAY_PHOTO;
        MediaMenuPop pop = new MediaMenuPop(context, popType);
        pop.setOnClickListener((value)->{
            if(value == MediaMenuPop.TYPE_SELECT_PHOTO) {
                PhotoHelper.openAlbum(ComplaintActivity.this, UploadDataBean.MAX_ONCE_PICKER_COUNT, null,(it)->{
                    addDataList(convert2GalleryVO(it));
                    return null;
                });
            } else if(value == MediaMenuPop.TYPE_PLAY_PHOTO) {
                PhotoHelper.openCamera(ComplaintActivity.this, null,(it) -> {
                    addDataList(convert2GalleryVO(it));
                    return null;
                });
            }
            return null;
        });
        pop.showPopupWindow();
    }


    private void addDataList(List<UploadDataBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        compressAndUpload(list);
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


    /**
     * 上传图片添加监听
     */
    private UploadImageAdapter.SelectPicCallBack callback = new UploadImageAdapter.SelectPicCallBack() {
        @Override
        public void addImg(int position) {
            if (position == mFilePathList.size()) {
//                SelectDialog(200);
                showGallery();
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
            imagePicker.setSelectLimit(3 - mFilePathList.size());
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.abc_submit) {//点击提交
            submit();
        }
    }

    /**
     * 提交
     */
    private void submit() {
        String mRefuseAcceptType = "";
        for (BasicListParam param : mListData) {
            if (param.isSelectState()) {
                mRefuseAcceptType = param.getValue();
            }
        }
        if (mRefuseAcceptType.isEmpty()) {
            ToastUtil.showToast(context, "请选择投诉类型！");
            return;
        }
        if (!InputUtil.isEmpty(context, viewBinding.mContent.getText().toString(), "请输入投诉描述！"))
            return;
//        if (mFilePathList.size() == 0) {
//            ToastUtil.showToast(context, "请上传照片！");
//            return;
//        }
        StringBuilder mUrls = new StringBuilder();
        for (Filepath param : mFilePathList) {
            if (mUrls.toString().equals("")) {
                mUrls = new StringBuilder(param.getFilePath());
            } else {
                mUrls.append(",").append(param.getFilePath());
            }
        }
        Map<String, String> mMap = new HashMap<>();
        mMap.put("orderId", mId);
        mMap.put("type", mRefuseAcceptType);
        mMap.put("explainReason", viewBinding.mContent.getText().toString());
        mMap.put("urls", mUrls.toString());
        OkHttpUtils.postAync(Constant.AppAddComplaint, new Gson().toJson(mMap), new HttpCallback<BasicBean>(context, getProgressDialog()) {
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
                    imagePicker.setSelectLimit(3 - mFilePathList.size());
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
}
