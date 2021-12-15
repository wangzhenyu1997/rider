package com.lingmiao.distribution.util;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InputUtil
 * 2018-06-20
 * 修改者，修改日期，修改内容。
 *
 * @author yandaocheng
 * 检查输入的文本否合法
 */
public class InputUtil {

    /***
     * 仅用于检测文本是否为空
     * @param context  上下文
     * @param content  需要检测的文本
     * @param hint     文本为空时需要的提示信息
     * @return 符合返回true
     */
    public static boolean isEmpty(Context context, String content, String hint) {
        if (content == null || content.isEmpty()) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 仅用于检测文本是否为空
     * @param context  上下文
     * @param content  需要检测的文本
     * @param hint     文本为空时需要的提示信息
     * @return 符合返回true
     */
    public static boolean isEmptyNew(Context context, String content, String hint) {
        if (content == null || content.isEmpty()) {
            ToastUtil.showToastNew(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 检测手机号码是否合法
     * @param context       上下文
     * @param phone         校验文本
     * @param formatHint    手机号格式错误提示
     * @return 符合返回true
     */
    public static boolean isPhone(Context context, String phone, String formatHint) {
        if (!regularCheck("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$", phone)) {
            ToastUtil.showToast(context, formatHint);
            return false;
        }
        return true;
    }

    /***
     * 检测手机号码是否合法
     * @param context       上下文
     * @param phone         校验文本
     * @param formatHint    手机号格式错误提示
     * @return 符合返回true
     */
    public static boolean isPhoneNew(Context context, String phone, String formatHint) {
        if (!regularCheck("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$", phone)) {
            ToastUtil.showToastNew(context, formatHint);
            return false;
        }
        return true;
    }

    /***
     * 检测纳税人识别码是否合法
     * @param context       上下文
     * @param tax           校验文本
     * @param formatHint    手机号格式错误提示
     * @return 符合返回true
     */
    public static boolean isTax(Context context, String tax, String formatHint) {
        if (!regularCheck("^[0-9a-zA-Z]{15,20}$", tax)) {
            ToastUtil.showToast(context, formatHint);
            return false;
        }
        return true;
    }

    /**
     * 校验密码格式(数字字母组合6-20位,可为：密码为6-20位数字字母或两者组合！)
     *
     * @param context 上下文
     * @param pass    校验文本
     * @param hint    不符合提示
     * @return 符合返回true
     */
    public static boolean isPassWord(Context context, String pass, String hint) {
        if (!regularCheck("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$", pass)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /**
     * 校验格式(数字字母组合最大限制20位)
     *
     * @param context 上下文
     * @param pass    校验文本
     * @param hint    不符合提示
     * @return 符合返回true
     */
    public static boolean isPassPhone(Context context, String pass, String hint) {
        if (!regularCheck("[0-9A-Za-z+$]{1,20}$", pass)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 校验文本格式（以字母开头的6-20位字符，可为：请输入以字母开头的6-20位字符！）
     * @param context     上下文
     * @param content     校验文本
     * @param hint        不符合提示
     * @return 符合返回true
     */
    public static boolean startWithLetter(Context context, String content, String hint) {
        if (!regularCheck("^[a-zA-Z][a-zA-Z0-9_]{5,20}$", content)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 校验文本格式（只能填写数字、英文字母、中划线与下划线，可为：只能填写数字、英文字母、中划线与下划线!）
     * @param context    上下文
     * @param content    校验文本
     * @param hint       不符合提示
     * @return 符合返回true 不符合返回false
     */
    public static boolean onlyLetterNumber(Context context, String content, String hint) {
        if (!regularCheck("^[0-9a-zA-Z-_]+$", content)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 检测身份证号码是否合法（可为：身份证号码格式有误！）
     * @param context 上下文
     * @param cardId  校验文本
     * @param hint    校验不符合提示
     * @return 符合返回true
     */
    public static boolean isCardIdNo(Context context, String cardId, String hint) {
        if (!regularCheck("^[1-9]\\d{5}[1-9]\\d{3}(((0[13578]|1[02])(0[1-9]|[12]\\d|3[0-1]))|((0[469]|11)(0[1-9]|[12]\\d|30))|(02(0[1-9]|[12]\\d)))(\\d{4}|\\d{3}[xX])$", cardId)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 检测邮箱地址是否合法（可为：邮箱格式有误！）
     * @param context 上下文
     * @param email   校验文本
     * @param hint    校验不符合提示
     * @return 符合返回true
     */
    public static boolean isEmail(Context context, String email, String hint) {
        if (!regularCheck("^([0-9A-Za-z\\-_\\.]+)@([0-9a-z]+\\.[a-z]{2,3}(\\.[a-z]{2})?)$", email)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 检测电话号码是否合法(可为：电话格式不正确，如0551-12345678!)
     * @param context   上下文
     * @param phone     校验文本
     * @param hint      校验不符合提示
     * @return 符合返回true
     */
    public static boolean isHomePhone(Context context, String phone, String hint) {
        if (!regularCheck("^((0\\d{2,3}-\\d{7,8})|(1[3584]\\d{9}))$", phone)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 校验传真格式（可为：传真号码有误！）
     * @param context  上下文
     * @param fax      校验文本
     * @param hint     校验不符合提示
     * @return 符合返回true
     */
    public static boolean isFax(Context context, String fax, String hint) {
        if (!regularCheck("^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$", fax)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 邮政编码验证，简单验证（可为：邮编格式不正确，请重新填写！）
     * @param context 上下文
     * @param zip     校验文本
     * @param hint    校验不符合提示
     * @return 符合返回true
     */
    public static boolean isZipCode(Context context, String zip, String hint) {
        if (!regularCheck("^[0-8][0-7]\\d{4}$", zip)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 校验输入框不能输入中文（可为：不能输入中文！）
     * @param context 上下文
     * @param content     校验文本
     * @param hint    校验不符合提示
     * @return 符合返回true
     */
    public static boolean isChinese(Context context, String content, String hint) {
        if (!regularCheck("^[^\\u4e00-\\u9fa5]{0,}$", content)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 校验组织机构代码格式(可为：请输入正确的组织机构代码号！)
     * @param context         上下文
     * @param organization    校验文本
     * @param hint            校验不符合提示
     * @return 符合返回true
     */
    public static boolean isOrganization(Context context, String organization, String hint) {
        if (!regularCheck("^[a-zA-Z0-9]{8}-[a-zA-Z0-9]$", organization)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 校验网址格式(可为：请输入正确的网站地址！)
     * @param context  上下文
     * @param url      校验文本
     * @param hint     校验不符合提示
     * @return 符合返回true
     *  */
    public boolean isWebSite(Context context, String url, String hint) {
        if (!regularCheck("((http|ftp|https):\\/\\/)?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:\\/~\\+#]*[\\w\\-\\@?^=%&amp;\\/~\\+#])?", url)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 校验银行卡号（可为：请输入正确的银行卡号！）
     * @param bankNo   校验文本
     * @param hint     校验不符合提示
     * @return 符合返回true
     */
    public static boolean isBankNo(Context context, String bankNo, String hint) {
        if (!regularCheck("^([1-9]{1})(\\d{15}|\\d{18})$", bankNo)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 校验金额格式（可为：无效金额，整数8位，小数2位，示例：247.23！）
     * @param context 上下文
     * @param money   校验文本
     * @param hint    校验不符合提示
     * @return 符合返回true
     */
    public static boolean isMoney(Context context, String money, String hint) {
        if (!regularCheck("^([1-9][\\d]{0,7}|0)(\\.[\\d]{1,2})?$", money)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 校验是否为正整数（大于0的正整数）
     * @param context 上下文
     * @param num     校验文本
     * @param hint    校验不符合提示
     * @return 符合返回true
     */
    public static boolean isPositive(Context context, String num, String hint) {
        if (!regularCheck("^\\+{0,1}[1-9]\\d*", num)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 判断只能填写汉字（只能填写中文汉字！）
     * @param context 上下文
     * @param value   校验文本
     * @param hint    提示文本
     * @return 符合返回true
     */
    public static boolean chinese(Context context, String value, String hint) {
        if (!regularCheck("^[\u4E00-\u9FA5]+$", value)) {
            ToastUtil.showToast(context, hint);
            return false;
        }
        return true;
    }

    /***
     * 判断是否包含Emoji
     * @param context 上下文
     * @param source  验证文本
     * @return 符合返回true
     */
    public static boolean isEmoji(Context context, String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                ToastUtil.showToast(context, "非法字符，请勿输入表情！");
                return false;
            }
        }
        return true;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD));
//                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)); (codePoint >= 0x10000 alway false)
    }

    /***
     * 判断字符串是否包含小写字母
     * @param content 校验内容
     * @return 包含返回true
     */
    public static boolean hasSmallLetters(String content) {
        return regularCheck(".*[a-z]+.*", content);
    }

    /***
     * 判断字符串是否包含大写字母
     * @param content 校验内容
     * @return 包含返回true
     */
    public static boolean hasBigLetters(String content) {
        return regularCheck(".*[A-Z]+.*", content);
    }

    /***
     * 判断字符串是否包含数字
     * @return 包含返回true
     */
    public static boolean hasNum(String content) {
        return regularCheck(".*\\d+.*", content);
    }

    /***
     * 判断是否符合相关正则
     * @param regularCheck 校验正则
     * @param value 校验value
     * @return 返回true符合正则，否则false
     */
    public static boolean regularCheck(String regularCheck, String value) {
        Pattern p = Pattern.compile(regularCheck);
        Matcher m = p.matcher(value);
        return m.matches();
    }
}
