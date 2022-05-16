package com.example.job_system.service.task;

import java.util.Date;
import java.util.concurrent.Future;

import org.springframework.transaction.annotation.Transactional;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public abstract class Task implements Runnable {

    private final String jobId;

    private final String type;

    private final String groupId;

    private final ScheduleTaskCreator creator;

    private Future<?> future;

    /**
     * Main task logic should be implemented in this method
     */
    protected abstract void doTask() throws Exception;

    /**
     * Method for Cancel functionality
     */
    public abstract void abort();

    private TaskResult call() {
        try {
            doTask();
            return new TaskResult(true, new Date(), null);
        } catch (Exception e) {
            return new TaskResult(false, new Date(), e);
        }
    }

    @Override
    @Transactional
    public void run() {
        TaskResult taskResult;
        try {
            taskResult = call();
        } catch (RuntimeException e) {
            taskResult = new TaskResult(false, new Date(), e);
        }
        creator.processJobResult(this, taskResult);
    }
}
