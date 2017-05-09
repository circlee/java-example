package com.example;

import org.quartz.*;

import java.util.Map;

public class QuartzManager {
    private Scheduler scheduler;

    public QuartzManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 添加任务
     *
     * @param jobName     任务名
     * @param jobGroup    任务组
     * @param jobClass    任务实现类
     * @param jobData     任务数据
     * @param triggerCron 触发器定时表示式
     */
    public void addJob(String jobName, String jobGroup, Class<? extends Job> jobClass, Map<String, Object> jobData, String triggerCron) {
        String triggerName = "TRIGGER-" + jobName;
        String triggerGroup = "TRIGGER-GROUP-" + jobGroup;
        addJob(jobName, jobGroup, jobClass, jobData, triggerName, triggerGroup, triggerCron);
    }

    /**
     * 添加任务
     *
     * @param jobName      任务名
     * @param jobGroup     任务组
     * @param jobClass     任务实现类
     * @param jobData      任务数据
     * @param triggerName  触发器名
     * @param triggerGroup 触发器组
     * @param triggerCron  触发器定时表示式
     */
    public void addJob(String jobName, String jobGroup, Class<? extends Job> jobClass, Map<String, Object> jobData, String triggerName, String triggerGroup, String triggerCron) {
        try {
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
            if (jobData != null && jobData.size() > 0)
                jobDetail.getJobDataMap().putAll(jobData);

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroup);
            // triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(triggerCron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改触发器定时表示式
     *
     * @param jobName     任务名
     * @param jobGroup    任务组
     * @param triggerCron 触发器定时表示式
     */
    public void modifyJobCron(String jobName, String jobGroup, String triggerCron) {
        String triggerName = "TRIGGER-" + jobName;
        String triggerGroup = "TRIGGER-GROUP-" + jobGroup;
        modifyJobCron(jobName, jobGroup, triggerName, triggerGroup, triggerCron);
    }

    /**
     * 修改触发器定时表示式
     *
     * @param jobName      任务名
     * @param jobGroup     任务组
     * @param triggerName  触发器名
     * @param triggerGroup 触发器组
     * @param triggerCron  触发器定时表示式
     */
    public void modifyJobCron(String jobName, String jobGroup, String triggerName, String triggerGroup, String triggerCron) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldCron = trigger.getCronExpression();
            if (!oldCron.equalsIgnoreCase(triggerCron)) {
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroup);
                // triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(triggerCron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改任务数据
     *
     * @param jobName  任务名
     * @param jobGroup 任务组
     * @param jobData  任务数据
     */
    public void modifyJobData(String jobName, String jobGroup, Map<String, Object> jobData) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return;
            }

            jobDetail.getJobDataMap().clear();
            if (jobData != null && jobData.size() > 0) {
                jobDetail.getJobDataMap().putAll(jobData);
            }
            scheduler.addJob(jobDetail, true, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 移除任务
     *
     * @param jobName  任务名
     * @param jobGroup 任务组
     */
    public void removeJob(String jobName, String jobGroup) {
        String triggerName = "TRIGGER-" + jobName;
        String triggerGroup = "TRIGGER-GROUP-" + jobGroup;
        removeJob(jobName, jobGroup, triggerName, triggerGroup);
    }

    /**
     * 移除任务
     *
     * @param jobName      任务名
     * @param jobGroup     任务组
     * @param triggerName  触发器名
     * @param triggerGroup 触发器组
     */
    public void removeJob(String jobName, String jobGroup, String triggerName, String triggerGroup) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);

            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动任务
     *
     * @param jobName  任务名
     * @param jobGroup 任务组
     */
    public void startJob(String jobName, String jobGroup) {
        String triggerName = "TRIGGER-" + jobName;
        String triggerGroup = "TRIGGER-GROUP-" + jobGroup;
        startJob(jobName, jobGroup, triggerName, triggerGroup);
    }

    /**
     * 启动任务
     *
     * @param jobName      任务名
     * @param jobGroup     任务组
     * @param triggerName  触发器名
     * @param triggerGroup 触发器组
     */
    public void startJob(String jobName, String jobGroup, String triggerName, String triggerGroup) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭任务
     *
     * @param jobName  任务名
     * @param jobGroup 任务组
     */
    public void stopJob(String jobName, String jobGroup) {
        String triggerName = "TRIGGER-" + jobName;
        String triggerGroup = "TRIGGER-GROUP-" + jobGroup;
        stopJob(jobName, jobGroup, triggerName, triggerGroup);
    }

    /**
     * 关闭任务
     *
     * @param jobName      任务名
     * @param jobGroup     任务组
     * @param triggerName  触发器名
     * @param triggerGroup 触发器组
     */
    public void stopJob(String jobName, String jobGroup, String triggerName, String triggerGroup) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动触发器
     *
     * @param jobName  任务名
     * @param jobGroup 任务组
     */
    public void startTrigger(String jobName, String jobGroup) {
        String triggerName = "TRIGGER-" + jobName;
        String triggerGroup = "TRIGGER-GROUP-" + jobGroup;
        startTrigger(jobName, jobGroup, triggerName, triggerGroup);
    }

    /**
     * 启动触发器
     *
     * @param jobName      任务名
     * @param jobGroup     任务组
     * @param triggerName  触发器名
     * @param triggerGroup 触发器组
     */
    public void startTrigger(String jobName, String jobGroup, String triggerName, String triggerGroup) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
            scheduler.resumeTrigger(triggerKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭触发器
     *
     * @param jobName  任务名
     * @param jobGroup 任务组
     */
    public void stopTrigger(String jobName, String jobGroup) {
        String triggerName = "TRIGGER-" + jobName;
        String triggerGroup = "TRIGGER-GROUP-" + jobGroup;
        stopTrigger(jobName, jobGroup, triggerName, triggerGroup);
    }

    /**
     * 关闭触发器
     *
     * @param jobName      任务名
     * @param jobGroup     任务组
     * @param triggerName  触发器名
     * @param triggerGroup 触发器组
     */
    public void stopTrigger(String jobName, String jobGroup, String triggerName, String triggerGroup) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
            scheduler.pauseTrigger(triggerKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动调度器
     */
    public void startScheduler() {
        try {
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭调度器
     */
    public void stopScheduler() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
