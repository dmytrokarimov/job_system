package com.example.job_system.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ConcurrentConfig {

    @Bean
    public TaskScheduler taskScheduler(@Value("${job.concurrency.scheduler.pool-size}") int poolSize) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(poolSize);
        return taskScheduler;
    }

    @Bean
    public ExecutorService taskExecutorService(@Value("${job.concurrency.task-executor.pool-size}") int poolSize, TaskPool taskPool) {
        return new ThreadPoolExecutor(poolSize, poolSize,
            0L, TimeUnit.MILLISECONDS,
            taskPool.getTaskExecutorWorkQueue());
    }

    @Bean
    public TaskPool taskPool() {
        return new TaskPool(new LinkedBlockingQueue<>());
    }
}
