package my.task;

import lombok.extern.slf4j.Slf4j;
import my.service.JiJinService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.IOException;

@Slf4j
public class JijinJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        try {
            JiJinService.send();
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
