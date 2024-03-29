package com.lingmiao.distribution.net

import android.text.TextUtils
import com.blankj.utilcode.util.*
import com.fisheagle.mkt.base.ApiService
import com.lingmiao.distribution.base.IConstant
import com.lingmiao.distribution.base.UserManager
import com.google.gson.Gson
import com.lingmiao.distribution.BuildConfig
import com.lingmiao.distribution.config.Constant
import com.james.common.BaseApplication
import com.james.common.net.BaseResponse
import com.james.common.net.RetrofitUtil
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.EOFException
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Author : Elson
 * Date   : 2020/7/7
 * Desc   :
 *          存在的问题：
 *          1, 网络异常没有捕捉
 *          2, 401/403 token过期没处理
 */
object Fetch {

    private val UTF8 = Charset.forName("UTF-8")

    private val apiService: ApiService by lazy {
        RetrofitUtil.getInstance().retrofit.create(ApiService::class.java)
    }

    fun apiService(): ApiService {
        return apiService
    }

    fun <T> createService(clazz: Class<T>): T {
        return RetrofitUtil.getInstance().retrofit.create(clazz)
    }

    fun initNetConfig() {
        RetrofitUtil.setBaseUrl(IConstant.getServerUrl())
        RetrofitUtil.setInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val host = chain.request().url().host()
                val builder = chain.request().newBuilder()
                    .addHeader("version", BuildConfig.VERSION_NAME)

                builder.addHeader("uuid", DeviceUtils.getAndroidID())

                var token = Constant.TOKEN;

                LogUtils.dTag("FETCH", "token here =${token}")

                if(token?.isBlank() == true) {
                    val loginInfo = UserManager.getLoginInfo();
                    token = loginInfo?.token ?: "";
                    LogUtils.dTag("FETCH", "token there =${token}")
                }
                builder.addHeader("token", token);
                if (UserManager.isLogin() && token?.isNotBlank() == true) {
                    builder.addHeader("token", token)
//                  带pro的正式服地址,已登录的则需要进行参数加密
                    if(IConstant.official){
                        val nonce = UUID.randomUUID().toString().replace("-", "").substring(0, 6)
                        val timestamp = (System.currentTimeMillis() / 1000).toString()
                        val sign = EncryptUtils.encryptMD5ToString(nonce + timestamp + Constant.TOKEN).toLowerCase(Locale.ROOT)
                        val httpUrl = chain.request().url().newBuilder()
//                            .addQueryParameter("uid", uid)
                            .addQueryParameter("nonce",nonce)
                            .addQueryParameter("timestamp",timestamp)
                            .addQueryParameter("sign",sign)
                            .build()
                        builder.url(httpUrl)
                    }
                }
                val request = builder.build()

                val startNs = System.nanoTime()
                val response: Response
                response = try {
                    chain.proceed(request)
                } catch (e: Exception) {
                    throw e
                }
                val tookMs =
                    TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

                val responseBody = response.body()
                val contentLength = responseBody!!.contentLength()
                val bodySize =
                    if (contentLength != -1L) "$contentLength-byte" else "unknown-length"


                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.

                val buffer = source.buffer()

                var charset = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                if (!isPlaintext(buffer)) {
                    return response
                }

                if (contentLength != 0L && response.code() != 200) {
                    val result = buffer.clone().readString(charset)
                    try {
                        val gson = Gson()
                        val baseResponse =
                            gson.fromJson(result, BaseResponse::class.java)
                        if (baseResponse != null) {
                            if (!TextUtils.isEmpty(baseResponse.message)) ToastUtils.showShort(
                                baseResponse.message
                            )
                            if("100000" == baseResponse.code) {
                                goToLoginActivity()
                            } else if ("403" == baseResponse.code) {
//                                val loginInfo = UserManager.getLoginInfo()
//                                var tempToken = "eyJhbGciOiJIUzUxMiJ9.eyJ1aWQiOjEwOSwic3ViIjoiQlVZRVIiLCJyb2xlcyI6WyJCVVlFUiJdLCJleHAiOjE1OTU5NTIzNDUsInV1aWQiOm51bGwsInVzZXJuYW1lIjoiYTEzMzkyNDI5NDU5In0.VWJSW6cuhxxK22WifEyTkQxB1FNUMICCT4JG-FK3Wr-V87CqBQCGgAXXX9rZVLKCAMhLk_EmHbeIsl2pfDFyNg"
//                                if (loginInfo?.token == null) {
//                                    goToLoginActivity()
//                                } else {
//                                    val response =
//                                        LoginRepository.refreshToken(loginInfo?.token!!)
//                                            .execute()
//                                    if (response.isSuccessful) {
//                                        loginInfo?.token = response.body()?.token
//                                        UserManager.setLoginInfo(loginInfo)
//                                        Constant.TOKEN = loginInfo?.token;
//                                        LogUtils.d("refresh token success")
//                                    } else {
//                                        goToLoginActivity()
//                                    }
//                                }
                                goToLoginActivity();
                            }
                        }
                    } catch (e: RuntimeException) {

                    }
                }


                return response
            }

        })
    }

    private fun goToLoginActivity() {
        if (AppUtils.isAppForeground()) {
            BaseApplication.getInstance().loginOutByToken()
            ActivityUtils.finishAllActivities()
            ActivityUtils.startActivity(com.lingmiao.distribution.ui.login.LoginActivity::class.java)
        }
    }

    fun isPlaintext(buffer: Buffer): Boolean {
        return try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(
                        codePoint
                    )
                ) {
                    return false
                }
            }
            true
        } catch (e: EOFException) {
            false // Truncated UTF-8 sequence.
        }
    }
}