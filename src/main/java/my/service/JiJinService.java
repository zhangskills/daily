package my.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public abstract class JiJinService {

    public static void send() throws IOException {
        List<String> list1 = new ArrayList<>();
        list1.add(getContentByType1("https://hq.sinajs.cn/list=s_sh000001"));
        list1.add(getContentByType1("https://hq.sinajs.cn/list=s_sz399001"));
        list1.add(getContentByType1("https://hq.sinajs.cn/list=s_sz399006"));
        String line1 = String.join(",", list1);

        List<String> list2 = new ArrayList<>();
        String s = getContentByType2("https://fundgz.1234567.com.cn/js/005918.js");
        if (s == null) {
            log.info("今天没有数据，暂不发送");
            return;
        }
        list2.add(s);
        list2.add(getContentByType2("https://fundgz.1234567.com.cn/js/004070.js"));
        list2.add(getContentByType2("https://fundgz.1234567.com.cn/js/000751.js"));
        list2.add(getContentByType2("https://fundgz.1234567.com.cn/js/005267.js"));
        list2.add(getContentByType2("https://fundgz.1234567.com.cn/js/004476.js"));
        String line2 = String.join(",", list2);
        String title = line1 + "\n" + line2;
        PushPlusService.sendForMy(title, "n");
    }

    private static String getContentByType1(String url) throws IOException {
        String res = Jsoup.connect(url)
                .ignoreContentType(true)
                .execute().body();
        String[] split = res.substring(res.indexOf("\"") + 1).split(",");
        return split[0] + split[3] + "%";
    }

    private static String getContentByType2(String url) throws IOException {
        String res = Jsoup.connect(url)
                .ignoreContentType(true)
                .execute().body();

        JsonNode jsonNode = new ObjectMapper().readTree(res.substring(res.indexOf("{"), res.indexOf("}") + 1));
        if (new SimpleDateFormat("yyyy-MM-dd").format(new Date()).equals(jsonNode.get("gztime").asText().split(" ")[0])) {
            return jsonNode.get("name").asText() + " " + jsonNode.get("gszzl").asText() + "%";
        }
        return null;
    }

}
