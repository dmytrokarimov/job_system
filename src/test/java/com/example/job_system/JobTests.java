package com.example.job_system;

import java.util.Queue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.job_system.config.TaskPool;
import com.example.job_system.dto.JobModel;
import com.example.job_system.service.JobService;

class JobTests extends BaseTest {

    @Autowired
    private JobService jobService;

    @Autowired
    private TaskPool taskPool;

    @Test
    void checkSingleJobStarted() throws InterruptedException {
        JobModel model = new JobModel();
        model.setPeriod(0);
        model.setType("test");
        model.setGroupId("test");
        jobService.scheduleJob(model);

        //we have to wait awhile as jobs started in async way
        Thread.sleep(500);

        Assertions.assertEquals(1, taskPool.getRunningTasks().size());
        Assertions.assertEquals(0, taskPool.getGroupedTasks().size());
    }

    @Test
    void checkBlockedJob() throws InterruptedException {
        JobModel model = new JobModel();
        model.setPeriod(0);
        model.setType("test");
        model.setGroupId("test");
        jobService.scheduleJob(model);
        jobService.scheduleJob(model);

        //we have to wait awhile as jobs started in async way
        Thread.sleep(500);

        Assertions.assertEquals(1, taskPool.getRunningTasks().size(),
            "Must be only 1 running task");
        Assertions.assertEquals(0, taskPool.getTaskExecutorWorkQueue().size(),
            "Must be no tasks in queue");
        Assertions.assertEquals(1, taskPool.getGroupedTasks().values().stream().mapToInt(Queue::size).sum(),
            "Second task must wait until first will be done");
    }

}
