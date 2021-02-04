package my;

import lombok.extern.slf4j.Slf4j;
import my.route.IndexRoute;

import static spark.Spark.*;

@Slf4j
public class Application {

    public static void main(String[] args) {
        threadPool(5, 1, 30_000);
        port(8080);

        get("/", IndexRoute.indexRoute);
        get("/w/:week", IndexRoute.weekRoute);
    }

}
