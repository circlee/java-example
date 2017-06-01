package com.example;

import com.example.job.MyJob;
import com.example.quartz.QuartzManager;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Collections;

public class QuartzTest {

    @Test
    public void test() throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        QuartzManager quartzManager = new QuartzManager(scheduler);

        // 每1000毫秒执行一次，重复执行3次，共执行4次
        quartzManager.addJob("myJob", "test", MyJob.class, Collections.singletonMap("x", "1"), 1000L, 3);
        quartzManager.startScheduler();

        while (Thread.activeCount() > 0)
            Thread.yield();
    }

    @Test
    public void testModify() throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        QuartzManager quartzManager = new QuartzManager(scheduler);

        quartzManager.addJob("myJob", "test", MyJob.class, Collections.singletonMap("x", "1"), "0/5 * * * * ?");
        quartzManager.startScheduler();

        Thread.sleep(1500);

        for (int i = 0; Thread.activeCount() > 0; i ++) {
            if (i % 3 == 0) {
                quartzManager.modifyJob("myJob", "test", Collections.singletonMap("x", i + ""));
            }
            Thread.sleep(1000);
        }
    }

}
