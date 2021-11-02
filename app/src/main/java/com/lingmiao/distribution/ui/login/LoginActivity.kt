package com.lingmiao.distribution.ui.login

import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import cn.jpush.android.api.JPushInterface
import com.lingmiao.distribution.base.UserManager
import com.lingmiao.distribution.R
import com.lingmiao.distribution.bean.PersonalDataParam
import com.lingmiao.distribution.ui.activity.*
import com.lingmiao.distribution.ui.login.bean.LoginVo
import com.lingmiao.distribution.ui.login.presenter.ILoginPresenter
import com.lingmiao.distribution.ui.login.presenter.impl.LoginPreImpl
import com.lingmiao.distribution.ui.main.MainActivity
import com.lingmiao.distribution.util.InputUtil
import com.lingmiao.distribution.util.MD5Util
import com.lingmiao.distribution.util.ToastUtil
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.util.LinkedHashSet

/**
Create Date : 2020/12/263:39 PM
Auther      : Fox
Desc        :
 **/
class LoginActivity : BaseActivity<ILoginPresenter>() , ILoginPresenter.View  {
    private var pwdIsVisible = false //默认密码隐藏

    override fun getLayoutId(): Int {
        return R.layout.activity_login;
    }

    override fun createPresenter(): ILoginPresenter {
        return LoginPreImpl(this);
    }

    override fun useBaseLayout(): Boolean {
        return false;
    }

    override fun initView() {
        l_user_name.setText(UserManager.getUserName());


        // 忘记密码
        l_forget_pass.setOnClickListener {
            startActivity(Intent(context, ModifyPasswordActivity::class.java).putExtra("type", 1))
        }
        // 注册
        l_register.setOnClickListener {
            startActivity(Intent(context, RegisterActivity::class.java))
        }
        //协议
        l_agreement.setOnClickListener {
            startActivity(Intent(context, AgreementActivity::class.java))
        }
        m_show_pass.setOnClickListener {
            pwdIsVisible = !pwdIsVisible
            if (pwdIsVisible) {
                m_show_pass.setImageResource(R.mipmap.eye_choose_on)
                l_user_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            } else {
                m_show_pass.setImageResource(R.mipmap.eye_choose_un)
                l_user_pass.setTransformationMethod(PasswordTransformationMethod.getInstance())
            }
        }
        //  登录
        l_login_btn.setOnClickListener {
            if(!checkInput(l_user_name.text.toString(), l_user_pass.text.toString())) {
                return@setOnClickListener;
            }
            var request = LoginVo(l_user_name.text.toString(), MD5Util.getMD5Str(l_user_pass.text.toString()));
            mPresenter.login(request);
        }
    }

    /**
     * 检验输入框内容
     */
    private fun checkInput(userName: String, password: String): Boolean {
        if (!InputUtil.isEmptyNew(this, userName, "请输入账号/手机号！")) {
            return false
        }
        if (!InputUtil.isPhoneNew(this, userName, "手机号格式有误！")) {
            return false
        }
        if (!InputUtil.isEmptyNew(this, password, "请输入登录密码！")) {
            return false
        }
        if (password.length < 6) {
            ToastUtil.showToastNew(context, "密码长度最低6位！")
            return false
        }
        if (!l_choose_checkbox.isChecked()) {
            ToastUtil.showToastNew(context, "请阅读并同意服务条款！")
            return false
        }
        return true
    }

    override fun onLoginSuccess(data : PersonalDataParam) {
        setJPushTag(data.id);
        //auditStatus 0未提交    1已提交    2审核通过   3审核不通过
        when(data.auditStatus) {
            2 -> {
                // 审核通过
                val intent = Intent(context, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish();
            }
            0 -> {
                // 未提交
                val intent = Intent(context, IdentityNoActivity::class.java)
                context.startActivity(intent)
                finish();
            }
            1 -> {
                //已提交
                val intent = Intent(context, IdentityExamineActivity::class.java)
                context.startActivity(intent)
                finish();
            }
            3 -> {
                // 审核不通过
                val intent: Intent =
                    Intent(context, IdentityExNoActivity::class.java).putExtra(
                        "reason",
                        data.getRefuseReason()
                    )
                context.startActivity(intent)
                finish();
            }
        }
    }


    /**
     * 设置推送TAG
     */
    private fun setJPushTag(user_id: String) {
        val tagSet: MutableSet<String> =
            LinkedHashSet()
        tagSet.add("test_$user_id") //其他环境用test_   生产环境pro_
        JPushInterface.setTags(context, 1, tagSet)
    }


    override fun onLoginError() {
        showBottomToast("")
    }

    //双击退出应用
    private var mExitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtil.showToastNew(this, "再按一次返回桌面")
                mExitTime = System.currentTimeMillis()
            } else {
                System.exit(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}