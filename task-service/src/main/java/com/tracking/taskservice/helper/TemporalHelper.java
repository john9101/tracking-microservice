package com.tracking.taskservice.helper;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;

import java.time.Duration;

public class TemporalHelper {
    public static final String TASK_QUEUE = "TrackingTaskQueue";

    private static RetryOptions retryOptions() {
        return RetryOptions.newBuilder()
                .setInitialInterval(Duration.ofSeconds(1))
                .setMaximumInterval(Duration.ofSeconds(100))
                .setBackoffCoefficient(2)
                .setMaximumAttempts(5)
                .build();
    }

    public static ActivityOptions activityOptions() {
        return ActivityOptions.newBuilder()
                .setTaskQueue(TASK_QUEUE)
                .setRetryOptions(retryOptions())
                .setStartToCloseTimeout(Duration.ofMinutes(5))
                .build();
    }
}
