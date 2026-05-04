package com.tus.coupon.workflow;

import com.tus.coupon.activity.CouponTaskActivities;
import com.tus.coupon.model.CouponTaskScheduleInput;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public final class CouponTaskScheduleWorkflowImpl implements CouponTaskScheduleWorkflow {
    private final CouponTaskActivities activities = Workflow.newActivityStub(
            CouponTaskActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(30))
                    .setRetryOptions(
                            RetryOptions.newBuilder()
                                    .setInitialInterval(Duration.ofMillis(200))
                                    .setMaximumInterval(Duration.ofSeconds(5))
                                    .setBackoffCoefficient(2.0)
                                    .setMaximumAttempts(5)
                                    .build())
                    .build());

    @Override
    public void run(CouponTaskScheduleInput input) {
        long now = Workflow.currentTimeMillis();
        long delayMs = input.getSendTimeEpochMillis() - now;
        if (delayMs > 0) {
            Workflow.sleep(Duration.ofMillis(delayMs));
        }

        activities.claimCouponTask(input.getTaskId(), input.getShopNumber());
        activities.publishCouponTaskExecute(input.getTaskId(), input.getShopNumber());
    }
}
