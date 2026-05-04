package com.tus.coupon.worker;

import com.tus.coupon.activity.CouponTaskActivitiesImpl;
import com.tus.coupon.config.CouponTaskConstants;
import com.tus.coupon.config.TemporalBootstrap;
import com.tus.coupon.workflow.CouponTaskScheduleWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Polls {@link CouponTaskConstants#TASK_QUEUE}. Run after Docker Temporal is up.
 */
public final class CouponTaskWorker {

    private static final Logger log = LoggerFactory.getLogger(CouponTaskWorker.class);

    public static void main(String[] args) throws InterruptedException {
        WorkflowServiceStubs stubs = TemporalBootstrap.serviceStubs();
        WorkflowClient client = TemporalBootstrap.workflowClient(stubs);

        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker(CouponTaskConstants.TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(CouponTaskScheduleWorkflowImpl.class);
        worker.registerActivitiesImplementations(new CouponTaskActivitiesImpl());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down worker factory");
            factory.shutdown();
            stubs.shutdown();
        }));

        factory.start();
        log.info(
                "Coupon task worker started. taskQueue={} temporalTarget={}",
                com.tus.coupon.config.CouponTaskConstants.TASK_QUEUE,
                firstNonBlank(System.getenv("TEMPORAL_TARGET"), "127.0.0.1:7233"));

        Thread.sleep(Long.MAX_VALUE);
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) {
            return a;
        }
        return b;
    }
}
