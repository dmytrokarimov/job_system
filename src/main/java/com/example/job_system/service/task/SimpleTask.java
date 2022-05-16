package com.example.job_system.service.task;

import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleTask extends Task {

    public SimpleTask(String jobId, String type, String groupId, ScheduleTaskCreator creator) {
        super(jobId, type, groupId, creator);
    }

    @Override
    protected void doTask() throws Exception {
        Thread.sleep(1_000);
    }

    @Override
    public void abort() {
        log.info("Cleaning up");
    }
}
