package my.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class TranslateUtils {

    // https://translate.google.cn/translate_a/single?client=gtx&dt=t&dj=1&ie=UTF-8&sl=auto&tl=zh-CN&q=
    public static final String en2cnByGoogle(String en) throws IOException {
        Connection connect = Jsoup.connect("https://translate.google.cn/translate_a/single")
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                .data("client", "gtx")
                .data("dt", "t")
                .data("dj", "1")
                .data("ie", "UTF-8")
                .data("sl", "auto")
                .data("tl", "zh-CN")
                .data("q", en)
                .ignoreContentType(true);
        Connection.Response res = connect.execute();
        JsonNode jsonNode = new ObjectMapper().readTree(res.body());
        List<String> lines = new ArrayList<>();
        JsonNode sentences = jsonNode.get("sentences");
        for (JsonNode e : sentences) {
            lines.add(e.get("trans").asText());
        }
        return String.join(" ", lines);
    }
}
