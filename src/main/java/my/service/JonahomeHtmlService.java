package my.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.jooby.internal.$shaded.io.IOUtils;
import lombok.extern.slf4j.Slf4j;
import my.model.JonahomeHtmlModel;
import my.model.query.QJonahomeHtmlModel;
import my.utils.RegexUtils;

@Slf4j
public class JonahomeHtmlService {

    public void crawlAll() throws IOException {
        for (int i = 0; i < 53; i++) {
            if (new QJonahomeHtmlModel().week.eq(i).exists()) {
                continue;
            }
            String num = String.format("%02d", i);
            String url = "http://www.jonahome.net/files02/PI_SGrowth-Day-Walk/gb/" + num + ".htm";
            log.info("---抓取 page:{}", url);
            Connection.Response res = Jsoup.connect(url)
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                    .execute();
            JonahomeHtmlModel jonahomeHtmlModel = new JonahomeHtmlModel();
            jonahomeHtmlModel.setWeek(i);
            jonahomeHtmlModel.setContent(new String(res.bodyAsBytes(), "gbk"));
            jonahomeHtmlModel.save();
        }
    }

    public String getContentByHtml(int week) throws IOException {
        String html = IOUtils.toString(JonahomeHtmlService.class.getResourceAsStream("/jonahome/" + week + ".html"),
                StandardCharsets.UTF_8);
        return formatContent(html);
    }

    public String getContentByDB(int week) {
        JonahomeHtmlModel jonahomeHtmlModel = new QJonahomeHtmlModel().week.eq(week).findOne();
        if (jonahomeHtmlModel == null) {
            return null;
        }
        return formatContent(jonahomeHtmlModel.getContent());
    }

    private String formatContent(String html) {
        Elements elements = Jsoup.parse(html).select(".Section1");

        // 去掉过宽的width
        elements.select("[width]").forEach(e -> {
            String widthStr = e.attr("width");
            if (widthStr.matches("\\d+")) {
                int width = Integer.parseInt(widthStr);
                if (width > 300) {
                    e.removeAttr("width");
                }
            }
        });

        StringBuilder sb = new StringBuilder();
        for (Element e : elements.get(0).children()) {
            if (!e.select("table[border=0]").isEmpty()) {
                e.select("td:contains(第)").get(0).attr("width", "20%");
                String s = e.select("td:contains(日)").get(0).text();
                String day = RegexUtils.getFirst("(\\d+)", s);
                // log.info("====={}", day);
                e.attr("id", "day" + day)
                        .attr("width", "80%")
                        .attr("style", "margin: 10px auto 0 auto;");
            }
            sb.append(e);
        }
        return sb.toString();
    }
}
