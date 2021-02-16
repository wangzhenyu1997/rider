package com.lingmiao.distribution.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lingmiao.distribution.R;

/**
 * ConfirmDialog
 *
 * @author yandaocheng <br/>
 * 弹窗温馨提示
 * 2018-06-21
 * 修改者，修改日期，修改内容
 */
public class ConfirmDialog extends Dialog {
    private DialogConfirmClick mLisetner;
    private Context mContext;
    private boolean cancelShow = true;//是否显示取消按钮

    private String mTittle = "";

    public ConfirmDialog(Context context, String tittle, DialogConfirmClick lisetner) {
        super(context);
        this.mContext = context;
        this.mTittle = tittle;
        this.mLisetner = lisetner;
    }

    public ConfirmDialog(Context context, String tittle, DialogConfirmClick lisetner, boolean cancelShow) {
        super(context);
        this.mContext = context;
        this.mTittle = tittle;
        this.mLisetner = lisetner;
        this.cancelShow = cancelShow;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        initView();
    }

    private void initView() {
        TextView tittle = (TextView) findViewById(R.id.c_tittle);
        tittle.setText(mTittle);
        findViewById(R.id.c_dialog_sure).setOnClickListener(new View.OnClickListener() {//确定
            @Override
            public void onClick(View v) {
                mLisetner.ConfirmClick(true);
                dismiss();
            }
        });
        findViewById(R.id.c_dialog_cancle).setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                mLisetner.ConfirmClick(false);
                dismiss();
            }
        });
        if (!cancelShow) {
            findViewById(R.id.c_dialog_cancle).setVisibility(View.GONE);
        }
    }

    public interface DialogConfirmClick {
        void ConfirmClick(Boolean value);
    }
}
