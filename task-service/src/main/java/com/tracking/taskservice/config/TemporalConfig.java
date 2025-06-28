package com.tracking.taskservice.config;

import com.tracking.taskservice.activity.TaskActivity;
import com.tracking.taskservice.activity.impl.TaskActivityImpl;
import com.tracking.taskservice.helper.TemporalHelper;
import com.tracking.taskservice.workflow.impl.TaskWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TemporalConfig {
    @Value("${temporal.connection.target}")
    private String temporalTarget;

    @Value("${temporal.namespace}")
    private String temporalNamespace;

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newInstance(
                WorkflowServiceStubsOptions.newBuilder().setTarget(temporalTarget).build());
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs serviceStubs) {
        return WorkflowClient.newInstance(serviceStubs, WorkflowClientOptions.newBuilder().setNamespace(temporalNamespace).build());
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient client) {
        return WorkerFactory.newInstance(client);
    }

    @Bean
    public Worker worker(WorkerFactory workerFactory, TaskActivity taskActivity) {
        Worker worker = workerFactory.newWorker(TemporalHelper.TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(TaskWorkflowImpl.class);
        worker.registerActivitiesImplementations(taskActivity);
        return worker;
    }

    @Bean
    public ApplicationRunner workerStarters(WorkerFactory workerFactory) {
        return args -> {
            workerFactory.start();
        };
    }
}



