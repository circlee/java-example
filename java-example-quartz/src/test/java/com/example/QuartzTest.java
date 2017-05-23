package com.example;

import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QuartzTest {

    @Test
    public void test() throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        QuartzManager quartzManager = new QuartzManager(scheduler);

        Map<String, Object> jobData = new HashMap<>();
        jobData.put("x", "1");
        jobData.put("y", "1");
        String triggerCron = "0/5 * * * * ?";
        quartzManager.addJob("myJob", "test", MyJob.class, jobData, triggerCron);
        quartzManager.startScheduler();

        Thread.sleep(1500);

        for (int i = 0; Thread.activeCount() > 0; i ++) {
            if (i % 3 == 0) {
                jobData = new HashMap<>();
                jobData.put("x", i + "");
                quartzManager.modifyJob("myJob", "test", jobData);

            }
            Thread.sleep(1000);
        }
    }

    @Test
    public void test2() throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        QuartzManager quartzManager = new QuartzManager(scheduler);

        // quartzManager.addJob("myJob", "test", MyJob.class, Collections.singletonMap("x", "1"));
        quartzManager.addJob("myJob", "test", MyJob.class, Collections.singletonMap("x", "2"), "0/3 * * * * ?");
        quartzManager.startScheduler();
        Thread.sleep(6000);
        quartzManager.addJob("myJob", "test", MyJob.class, Collections.singletonMap("x", "3"));



        while (Thread.activeCount() > 0)
            Thread.yield();
    }

}
