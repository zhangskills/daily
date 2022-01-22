package my;

import com.typesafe.config.Config;

import org.quartz.SchedulerException;

import io.jooby.Jooby;
import lombok.extern.slf4j.Slf4j;
import my.config.JoobyConfig;
import my.route.DailyByTodayRoute;
import my.route.DailyByWeekRoute;
import my.route.SjpptRoute;
import my.task.SchedulerUtils;

@Slf4j
public class Application extends Jooby {
    {
        install(JoobyConfig::new);

        /** 挂载路由 */
        mount(new DailyByTodayRoute());
        mount(new DailyByWeekRoute());

        Config config = getEnvironment().getConfig();
        mount(new SjpptRoute(config.getString("sjppt.volumeName"), config.getString("sjppt.imageBasePath")));

        try {
            new SchedulerUtils().run();
        } catch (SchedulerException e) {
            log.error("", e);
        }
    }

    public static void main(String[] args) {
        Jooby.runApp(args, Application::new);
    }
}
