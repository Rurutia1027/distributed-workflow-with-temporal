package com.tus.coupon.config;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;

public final class TemporalBootstrap {
    private TemporalBootstrap() {
    }

    public static WorkflowServiceStubs serviceStubs() {
        String target = firstNonBlank(System.getenv("TEMPORAL_TARGET"), "127.0.0.1:7233");
        return WorkflowServiceStubs.newServiceStubs(
                WorkflowServiceStubsOptions.newBuilder().setTarget(target).build());
    }

    public static WorkflowClient workflowClient(WorkflowServiceStubs stubs) {
        String namespace = firstNonBlank(System.getenv("TEMPORAL_NAMESPACE"), "default");
        return WorkflowClient.newInstance(
                stubs,
                WorkflowClientOptions.newBuilder().setNamespace(namespace).build());
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) {
            return a;
        }
        return b;
    }
}
