package com.example.job_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.example.job_system.dto.JobModel;
import com.example.job_system.service.JobService;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JobStatsTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobService jobService;

    @Test
    void checkInitialStats() throws Exception {
        this.mockMvc.perform(get("/api/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.runningJobs", is(0)))
            .andExpect(jsonPath("$.totalBlockedJobs", is(0)))
            .andExpect(jsonPath("$.sizeOfQueue", is(0)))
        ;
    }

    @Test
    void checkRunningStats() throws Exception {
        JobModel model = new JobModel();
        model.setPeriod(0);
        model.setType("test");
        model.setGroupId("test");
        jobService.scheduleJob(model);

        //we have to wait awhile as jobs started in async way
        Thread.sleep(500);

        this.mockMvc.perform(get("/api/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.runningJobs", is(1)))
            .andExpect(jsonPath("$.totalBlockedJobs", is(0)))
            .andExpect(jsonPath("$.sizeOfQueue", is(0)))
        ;
    }

    @Test
    void checkRunningDifferentJobsStats() throws Exception {
        JobModel model = new JobModel();
        model.setPeriod(0);
        model.setType("test");
        model.setGroupId("test");
        jobService.scheduleJob(model);
        model = new JobModel();
        model.setPeriod(0);
        model.setType("test");
        model.setGroupId("test2");
        jobService.scheduleJob(model);

        //we have to wait awhile as jobs started in async way
        Thread.sleep(500);

        this.mockMvc.perform(get("/api/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.runningJobs", is(1)))
            .andExpect(jsonPath("$.totalBlockedJobs", is(0)))
            .andExpect(jsonPath("$.sizeOfQueue", is(1)))
        ;
    }

}
