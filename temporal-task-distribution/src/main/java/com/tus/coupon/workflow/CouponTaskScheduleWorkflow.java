package com.tus.coupon.workflow;

import com.tus.coupon.model.CouponTaskScheduleInput;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/**
 * One workflow per coupon task: sleep until send time, then claim + dispatch (activities).
 */

@WorkflowInterface
public interface CouponTaskScheduleWorkflow {
    @WorkflowMethod
    void run(CouponTaskScheduleInput input);
}
