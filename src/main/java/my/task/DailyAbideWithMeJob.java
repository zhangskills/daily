package my.task;

import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import my.service.DailyService;

@Slf4j
public class DailyAbideWithMeJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        try {
            DailyService.send();
        } catch (IOException | WxErrorException e) {
            log.error("", e);
        }
    }
}
