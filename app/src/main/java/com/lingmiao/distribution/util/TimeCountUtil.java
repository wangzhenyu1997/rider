package com.lingmiao.distribution.util;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * TimeCountUtil
 *
 * @author yandaocheng <br/>
 * 倒计时效果
 * 2018-06-14
 * 修改者，修改日期，修改内容
 */
public class TimeCountUtil extends CountDownTimer {
    private TextView btn;//按钮

    // 在这个构造方法里需要传入三个参数一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个是，就把这个按钮传过来就可以了
    public TimeCountUtil(long millisInFuture, long countDownInterval, TextView btn) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击
        btn.setText(millisUntilFinished / 1000 + "s");//设置倒计时时间
    }

    @Override
    public void onFinish() {
        btn.setText("重新获取");
        btn.setClickable(true);//重新获得点击
    }
}