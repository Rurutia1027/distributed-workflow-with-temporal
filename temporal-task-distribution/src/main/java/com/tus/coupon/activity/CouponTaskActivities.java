package com.tus.coupon.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

/**
 * Side effects: DB claim + MQ publish (prototype uses in-memory + logs).
 */

@ActivityInterface
public interface CouponTaskActivities {
    @ActivityMethod
    void claimCouponTask(long taskId, long shopNumber);

    @ActivityMethod
    void publishCouponTaskExecute(long taskId, long shopNumber);
}
