package my;

import lombok.extern.slf4j.Slf4j;
import my.controller.IndexController;

import static spark.Spark.port;
import static spark.Spark.threadPool;

@Slf4j
public class Application {

    public static void main(String[] args) {
        threadPool(5, 1, 30_000);
        port(8080);

        new IndexController();
    }

}
