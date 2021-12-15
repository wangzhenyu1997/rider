package com.lingmiao.distribution.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.lingmiao.distribution.databinding.DialogUpdateConfirmBinding;

/**
 * HomeConfirmDialog
 *
 * @author yandaocheng <br/>
 * 弹窗温馨提示
 * 2020-07-12
 * 修改者，修改日期，修改内容
 */
public class UpdateConfirmDialog extends Dialog {

    private DialogUpdateConfirmClick mLisetner;

    private DialogUpdateConfirmBinding viewBinding;
    private String mTittle = "";
    private String mContent;
    private String mCancle;
    private String mSure;

    public UpdateConfirmDialog(Context context, DialogUpdateConfirmClick lisetner, String tittle, String content, String cancle, String sure) {
        super(context);
        this.mTittle = tittle;
        this.mContent = content;
        this.mCancle = cancle;
        this.mSure = sure;
        this.mLisetner = lisetner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        viewBinding = DialogUpdateConfirmBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        initView();
    }

    private void initView() {
        setCancelable(false);
        viewBinding.mTittle.setText(mTittle);
        viewBinding.mContent.setText(mContent);
//        viewBinding.mCancle.setText(mCancle);
        viewBinding.mSure.setText(mSure);
        //确定
        viewBinding.mSure.setOnClickListener(v -> {
            mLisetner.ConfirmClick(true);
            dismiss();
        });
        //取消
//        viewBinding.mCancle.setOnClickListener(v -> {
//            mLisetner.ConfirmClick(false);
//            dismiss();
//        });
    }

    public interface DialogUpdateConfirmClick {
        void ConfirmClick(Boolean value);
    }
}
