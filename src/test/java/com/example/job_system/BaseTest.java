package com.example.job_system;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.job_system.config.TaskPool;
import com.example.job_system.service.task.ScheduleTaskCreator;
import com.example.job_system.service.task.Task;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JobSystemApplication.class)
@AutoConfigureMockMvc
public class BaseTest {

    @Autowired
    private TaskPool taskPool;

    @BeforeEach
    void waitAllJobFinish() throws InterruptedException, ExecutionException {
        for (ScheduleTaskCreator creator : taskPool.getScheduledTasks()) {
            creator.cancel();
        }

        taskPool.getScheduledTasks().clear();
        taskPool.getTaskExecutorWorkQueue().clear();
        taskPool.getGroupedTasks().clear();

        for (Task runningTask : taskPool.getRunningTasks()) {
            runningTask.getFuture().get();
        }
    }
}
