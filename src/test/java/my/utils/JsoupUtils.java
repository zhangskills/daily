package my.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public abstract class JsoupUtils {

    public static Connection getConnection(String url) {
        return Jsoup.connect(url)
                .timeout(60_000)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("User-Agent", "Mozilla/5ddd (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36");
    }
}
