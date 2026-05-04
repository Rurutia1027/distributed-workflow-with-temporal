package com.tus.coupon.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Stand-in for DB + RocketMQ. Thread-safe for parallel activities across workflow.
 */
public class CouponTaskActivitiesImpl implements CouponTaskActivities {
    private static final Logger log = LoggerFactory.getLogger(CouponTaskActivitiesImpl.class);

    // Simulated task rows: PENDING --> IN_PROGRESS on successful claim.
    private final Map<Long, String> taskStatus = new ConcurrentHashMap<>();

    // Seed a task as PENDING (used by starter so claim matches real flow)
    public void seedPending(long taskId) {
        taskStatus.put(taskId, "PENDING");
    }

    @Override
    public void claimCouponTask(long taskId, long shopNumber) {
        taskStatus.compute(taskId, (id, status) -> {
            if (status == null) {
                log.warn("[activity:claim] taskId={} shopNumber={} — no row, treating as claim ok for demo", id, shopNumber);
                return "IN_PROGRESS";
            }
            if ("IN_PROGRESS".equals(status)) {
                log.info("[activity:claim] taskId={} already IN_PROGRESS (idempotent)", id);
                return status;
            }
            if ("PENDING".equals(status)) {
                log.info("[activity:claim] taskId={} shopNumber={} PENDING -> IN_PROGRESS", id, shopNumber);
                return "IN_PROGRESS";
            }
            log.warn("[activity:claim] taskId={} unexpected status={}, forcing IN_PROGRESS for demo", id, status);
            return "IN_PROGRESS";
        });
    }

    @Override
    public void publishCouponTaskExecute(long taskId, long shopNumber) {
        log.info(
                "[activity:publish] CouponTaskExecuteEvent couponTaskId={} shopNumber={} (RocketMQ stub — wire CouponTaskActualExecuteProducer here)",
                taskId,
                shopNumber);
    }
}
