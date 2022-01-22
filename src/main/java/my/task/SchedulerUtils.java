package my.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
public class SchedulerUtils {

    private Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

    public SchedulerUtils() throws SchedulerException {
    }

    public void addJob(String jobName, String triggerName, String cronExpression, Class<? extends Job> jobClass)
            throws SchedulerException {
        JobDetail job = newJob(jobClass)
                .withIdentity(jobName, "group1")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity(triggerName, "group1")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        scheduler.scheduleJob(job, trigger);
        log.warn("--添加定时任务 {} cron:{}", jobClass, cronExpression);
    }

    public void run() throws SchedulerException {
        addJob("j1", "t1", "0 0 7 * * ?", DailyAbideWithMeJob.class);
//        addJob("j2", "t2", "0 0 14 * * ?", JijinJob.class);

        scheduler.start();
        log.warn("quartz 启动");
    }

    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
        log.warn("quartz 关闭");
    }

}

