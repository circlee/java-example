# Java - Quartz 定时任务

> Quartz是一个完全由Java编写的开源作业调度框架，为在Java应用程序中进行作业调度提供了简单却强大的机制。Quartz允许开发人员根据时间间隔来调度作业。

### 基础概念

#### Job

具体需要处理的业务逻辑。

#### JobDetail

详细的任务描述，包括名称，关联的`Job`，`Job`运行时所需要的参数等。

#### Trigger

任务调度器，描述什么时候执行`Job`，多久执行一次。

- SimpleTrigger 设置重复次数，重复执行间隔时长
- CronTrigger 设置Cron表达式

#### Scheduler

调度容器，`Job`与`Trigger`都需要在容器中注册，被容器统一管理。

### Cron表达式

#### 范式

表达式               | 说明
-------------------- | --------------
*/5 * * * * ?        | 每隔5秒执行一次
0 */1 * * * ?        | 每隔1分钟执行一次
0 0 23 * * ?         | 每天23点执行一次
0 0 23 ? * *         | 每天23点执行一次
0 0 23 * * ? *       | 每天23点执行一次
0 0 23 * * ? 2016    | 2016年每天23点执行一次
0 0 1 * * ?          | 每天1点执行一次
0 0 1 1 * ?          | 每月1号1点执行一次
0 * 14 * * ?         | 每天14:00点到14:59期间，每隔1分钟执行一次
0 0-5 14 * * ?       | 每天14:00点到14:05期间，每隔1分钟执行一次
0 0 23 L * ?         | 每月最后一天23点执行一次
0 0 1 ? * L          | 每周星期六1点执行一次
0 26,29,33 * * * ?   | 在26分、29分、33分执行一次
0 0 0,13,18,21 * * ? | 每天的0点、13点、18点、21点都执行一次
0 0 0 ? * 6#3        | 每月第3个星期六

#### 表达式

字段       | 可选值              | 特殊字符
---------- | ------------------- | --------------
秒         | 0 - 59              | , - * /
分         | 0 - 59              | , - * /
时         | 0 - 23              | , - * /
日         | 1 - 31              | , - * / ? L W
月         | 1 - 12              | , - * /
周         | 1（Sun） - 7（Sat） | , - * / ? L #
年（可选） | 1970 - 2099         | , - * /

#### 特殊字符

字符 | 说明
---- | -----------
,    | 指定多个数值
-    | 指定一个范围
*    | 代表整个时间段
/    | 多长时间执行一次
?    | 不确定的值
L    | 每月最后一天（日）/每月最后一个星期六（周）
W    | 最近的工作日
#    | 每月第N个工作日

### 示例

#### QuartzManager

QuartzManager中我封装了很多方法，包括`Job`，`Trigger`的添加，删除，修改等，这里只列举了其中一个。

```java
public class QuartzManager {
    /**
     * 添加任务
     *
     * @param jobName      任务名
     * @param jobGroup     任务组
     * @param jobClass     任务实现类
     * @param jobData      任务数据
     * @param triggerName  触发器名
     * @param triggerGroup 触发器组
     * @param triggerCron  触发器Cron表示式
     * @return
     */
    public Boolean addJob(String jobName, String jobGroup, Class<? extends Job> jobClass, Map<String, Object> jobData, 
                            String triggerName, String triggerGroup, String triggerCron) {
        try {
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
            if (jobData != null && jobData.size() > 0)
                jobDetail.getJobDataMap().putAll(jobData);

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroup);
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(triggerCron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }
    ...
}
```

#### MyJob

```java
public class MyJob implements Job {
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String x = jobDataMap.getString("x");
        String y = jobDataMap.getString("y");
        System.out.println(String.format("%s x=%s, y=%s", df.format(new Date()), x, y));
    }
}
```

#### QuartzTest

```java
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
}
```

*PS：本文使用的是quartz-2.2.3*