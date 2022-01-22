package my.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PushPlusService {

    private static String url = "http://pushplus.hxtrip.com/send";
    @Setter
    private static String token;

    public static String sendForDaily(String title, String content) throws IOException {
        return sendByTopic("daily", title, content);
    }

    public static String sendForMy(String title, String content) throws IOException {
        return sendByTopic("my", title, content);
    }

    private static String sendByTopic(String topic, String title, String content) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("topic", topic);
        data.put("title", title);
        data.put("content", content);

        String res = Jsoup.connect(url)
                .ignoreContentType(true)
                .data(data)
                .method(Connection.Method.POST)
                .execute().body();
        log.info("--{}", res);
        return res;
    }
}
