package com.tus.coupon.starter;

import com.tus.coupon.config.CouponTaskConstants;
import com.tus.coupon.config.TemporalBootstrap;
import com.tus.coupon.model.CouponTaskScheduleInput;
import com.tus.coupon.workflow.CouponTaskScheduleWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Starts a workflow run -- same process would run in merchant-admin after task insert
 * (future integration)
 */
public final class CouponTaskStarter {
    private static final Logger LOG = LoggerFactory.getLogger(CouponTaskStarter.class);

    public static void main(String[] args) {
        Map<String, String> opts = parseArgs(args);
        long taskId = Long.parseLong(opts.getOrDefault("task-id", "10001"));
        long shopNumber = Long.parseLong(opts.getOrDefault("shop-number", "248475"));
        long delaySeconds = Integer.parseInt(opts.getOrDefault("delay-seconds", "0"));

        long sendTime = Instant.now().plusSeconds(delaySeconds).toEpochMilli();
        CouponTaskScheduleInput input = new CouponTaskScheduleInput(taskId, shopNumber,
                sendTime);

        WorkflowServiceStubs stubs = TemporalBootstrap.serviceStubs();
        WorkflowClient client = TemporalBootstrap.workflowClient(stubs);
        try {
            CouponTaskScheduleWorkflow workflow =
                    client.newWorkflowStub(
                            CouponTaskScheduleWorkflow.class,
                            WorkflowOptions.newBuilder()
                                    .setWorkflowId(CouponTaskConstants.workflowId(taskId))
                                    .setTaskQueue(CouponTaskConstants.TASK_QUEUE)
                                    .build());
            LOG.info("Starting workflow workflowId={} input={}",
                    CouponTaskConstants.workflowId(taskId), input);
            workflow.run(input);
            LOG.info("Workflow completed workflowId={}", CouponTaskConstants.workflowId(taskId));
        } finally {
            stubs.shutdown();
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if ("--task-id".equals(a) && i + 1 < args.length) {
                map.put("task-id", args[++i]);
            } else if ("--shop-number".equals(a) && i + 1 < args.length) {
                map.put("shop-number", args[++i]);
            } else if ("--delay-seconds".equals(a) && i + 1 < args.length) {
                map.put("delay-seconds", args[++i]);
            }
        }
        return map;
    }
}
