package my;

import io.jooby.Jooby;
import lombok.extern.slf4j.Slf4j;
import my.config.JoobyConfig;
import my.route.IndexRoute;

@Slf4j
public class Application extends Jooby {

    {
        install(JoobyConfig::new);

        /** 挂载路由 */
        mount(new IndexRoute());
    }

    public static void main(String[] args) {
        Jooby.runApp(args, Application::new);
    }
}
