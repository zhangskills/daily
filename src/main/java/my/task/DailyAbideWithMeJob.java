package my.task;

import lombok.extern.slf4j.Slf4j;
import my.service.DailyService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.IOException;

@Slf4j
public class DailyAbideWithMeJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        try {
            DailyService.send();
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
