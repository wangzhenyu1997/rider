package com.lingmiao.distribution.bean;

import java.io.Serializable;

/**
 * Create Date : 2020/12/203:20 PM
 * Auther      : Fox
 * Desc        :
 **/
public class HomeModelEvent implements Serializable {

    /**
     * 四轮
     */
    public static final int MODEL_FOUR = 1;
    /**
     * 两轮
     */
    public static final int MODEL_TWO = 2;
    /**
     * 默认
     */
    public static final int DEFAULT_MODEL = MODEL_FOUR;
    /**
     * 综合
     */
    public static final int SORT_DEFAULT = 1;
    /**
     * 距离
     */
    public static final int SORT_DISTANCE = 2;
    /**
     * 时间
     */
    public static final int SORT_TIME = 3;
    /**
     * 取默认
     */
    public static final int DEFAULT_SORT_OF_TAKE = SORT_DEFAULT;
    /**
     * 送默认
     */
    public static final int DEFAULT_SORT_DELIVERY = SORT_DEFAULT;

    private int id;
    private int workStatus;
    private int pickOrder;
    private int pickOrderSort = 0;
    private int deliveryOrder;
    private int deliveryOrderSort = 0;

    public static final int SORT_ASC = 1;

    public static final int SORT_DESC = 2;

    public HomeModelEvent() {
        this(DEFAULT_MODEL, DEFAULT_SORT_OF_TAKE, DEFAULT_SORT_DELIVERY);
    }

    public HomeModelEvent(int workStatus, int take, int delivery) {
        this.workStatus = workStatus;
        this.pickOrder = take;
        this.deliveryOrder = delivery;
    }

    public int getPickOrderSort() {
        return pickOrderSort;
    }

    public void setPickOrderSort(int pickOrderSort) {
        this.pickOrderSort = pickOrderSort;
    }

    public int getDeliveryOrderSort() {
        return deliveryOrderSort;
    }

    public void setDeliveryOrderSort(int deliveryOrderSort) {
        this.deliveryOrderSort = deliveryOrderSort;
    }

    public boolean isDefaultModel() {
        return DEFAULT_MODEL == workStatus;
    }

    public boolean isFourMode() {
        return MODEL_FOUR == workStatus;
    }

    public boolean isTwoMode() {
        return MODEL_TWO == workStatus;
    }

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public int getPickOrder() {
        return pickOrder;
    }

    public void setPickOrder(int pickOrder) {
        this.pickOrder = pickOrder;
    }

    public int getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(int deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }
}
