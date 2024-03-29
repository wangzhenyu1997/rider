package com.lingmiao.distribution.util;

import java.math.BigDecimal;

/**
 * @author yandaocheng
 * @Description: 数学运算及相关共用方法,以下保留小数点后几位的方式全部采用了4舍5入方法;底部附加BigDecimal.ROUND_HALF_XXX的各种用法
 * @date 2018-04-23
 */
public class MathExtend {
    // 默认除法运算精度
    private static final int DEFAULT_DIV_SCALE = 10;

    /**
     * 提供精确的加法运算。
     *
     * @param v1
     * @param v2
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的加法运算
     *
     * @param v1
     * @param v2
     * @return 两个参数数学加和，以字符串格式返回
     */
    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1
     * @param v2
     * @return 两个参数的差
     */
    public static double subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1
     * @param v2
     * @return 两个参数数学差，以字符串格式返回
     */
    public static String subtract(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1
     * @param v2
     * @return 两个参数的积
     */
    public static double multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1
     * @param v2
     * @return 两个参数的数学积，以字符串格式返回
     */
    public static String multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).toString();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_UP
     *
     * @param v1
     * @param v2
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2) {
        return divide(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_UP
     *
     * @param v1
     * @param v2
     * @param scale 表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale) {
        return divide(v1, v2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
     *
     * @param v1
     * @param v2
     * @param scale      表示需要精确到小数点以后几位
     * @param round_mode 表示用户指定的舍入模式
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, round_mode).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_UP
     *
     * @param v1
     * @param v2
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2) {
        return divide(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_UP
     *
     * @param v1
     * @param v2
     * @param scale 表示需要精确到小数点以后几位
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2, int scale) {
        return divide(v1, v2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
     *
     * @param v1
     * @param v2
     * @param scale      表示需要精确到小数点以后几位
     * @param round_mode 表示用户指定的舍入模式
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, round_mode).toString();
    }

    /**
     * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_UP
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        return round(v, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param v          需要四舍五入的数字
     * @param scale      小数点后保留几位
     * @param round_mode 指定的舍入模式
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        return b.setScale(scale, round_mode).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_UP
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果，以字符串格式返回
     */
    public static String round(String v, int scale) {
        if (v == null || v == "") {
            return "--";
        }
        return round(v, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param v          需要四舍五入的数字
     * @param scale      小数点后保留几位
     * @param round_mode 指定的舍入模式
     * @return 四舍五入后的结果，以字符串格式返回
     */
    public static String round(String v, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        return b.setScale(scale, round_mode).toString();
    }
}

//        BigDecimal.ROUND_HALF_XXX的各种用法
//
//        在银行、帐户、计费等领域，BigDecimal提供了精确的数值计算。其中8种舍入方式值得掌握。
//
//        1、ROUND_UP
//
//        舍入远离零的舍入模式。
//
//        在丢弃非零部分之前始终增加数字(始终对非零舍弃部分前面的数字加1)。
//
//        注意，此舍入模式始终不会减少计算值的大小。
//
//        2、ROUND_DOWN
//
//        接近零的舍入模式。
//
//        在丢弃某部分之前始终不增加数字(从不对舍弃部分前面的数字加1，即截短)。
//
//        注意，此舍入模式始终不会增加计算值的大小。
//
//        3、ROUND_CEILING
//
//        接近正无穷大的舍入模式。
//
//        如果 BigDecimal 为正，则舍入行为与 ROUND_UP 相同;
//
//        如果为负，则舍入行为与 ROUND_DOWN 相同。
//
//        注意，此舍入模式始终不会减少计算值。
//
//        4、ROUND_FLOOR
//
//        接近负无穷大的舍入模式。
//
//        如果 BigDecimal 为正，则舍入行为与 ROUND_DOWN 相同;
//
//        如果为负，则舍入行为与 ROUND_UP 相同。
//
//        注意，此舍入模式始终不会增加计算值。
//
//        5、ROUND_HALF_UP
//
//        向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。
//
//        如果舍弃部分 >= 0.5，则舍入行为与 ROUND_UP 相同;否则舍入行为与 ROUND_DOWN 相同。
//
//        注意，这是我们大多数人在小学时就学过的舍入模式(四舍五入)。
//
//        6、ROUND_HALF_DOWN
//
//        向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为上舍入的舍入模式。
//
//        如果舍弃部分 > 0.5，则舍入行为与 ROUND_UP 相同;否则舍入行为与 ROUND_DOWN 相同(五舍六入)。
//
//        7、ROUND_HALF_EVEN    银行家舍入法
//
//        向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。
//
//        如果舍弃部分左边的数字为奇数，则舍入行为与 ROUND_HALF_UP 相同;
//
//        如果为偶数，则舍入行为与 ROUND_HALF_DOWN 相同。
//
//        注意，在重复进行一系列计算时，此舍入模式可以将累加错误减到最小。
//
//        此舍入模式也称为“银行家舍入法”，主要在美国使用。四舍六入，五分两种情况。
//
//        如果前一位为奇数，则入位，否则舍去。
//
//        以下例子为保留小数点1位，那么这种舍入方式下的结果。
//
//        1.15>1.2 1.25>1.2
//
//        8、ROUND_UNNECESSARY
//
//        断言请求的操作具有精确的结果，因此不需要舍入。
//
//        如果对获得精确结果的操作指定此舍入模式，则抛出ArithmeticException。