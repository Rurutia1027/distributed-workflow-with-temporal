package com.tus.coupon.config;

/**
 * Shared queue / id conventions for local debugging.
 */
public final class CouponTaskConstants {
    public static final String TASK_QUEUE = "coupon-task-queue";

    private CouponTaskConstants() {}

    public static String workflowId(long couponTaskId) {
        return "coupon-task-" + couponTaskId;
    }
}
