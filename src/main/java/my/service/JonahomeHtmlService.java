package my.service;

import lombok.extern.slf4j.Slf4j;
import my.model.JonahomeHtmlModel;
import my.repository.JonahomeHtmlRepository;
import my.utils.RegexUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class JonahomeHtmlService {

    @Autowired
    private JonahomeHtmlRepository jonahomeHtmlRepository;

    public void crawlAll() throws IOException {
        for (int i = 0; i < 53; i++) {
            JonahomeHtmlModel jonahomeHtmlModel = jonahomeHtmlRepository.findTopByWeek(i);
            if (jonahomeHtmlModel != null) {
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
            jonahomeHtmlModel = new JonahomeHtmlModel();
            jonahomeHtmlModel.setWeek(i);
            jonahomeHtmlModel.setContent(new String(res.bodyAsBytes(), "gbk"));
            jonahomeHtmlRepository.save(jonahomeHtmlModel);
        }
    }

    public String getContent(int week) {
        JonahomeHtmlModel jonahomeHtmlModel = jonahomeHtmlRepository.findTopByWeek(week);
        if (jonahomeHtmlModel == null) {
            return null;
        }

        Elements elements = Jsoup.parse(jonahomeHtmlModel.getContent()).select(".Section1");

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
                String s = e.select("td:contains(日)").get(0).text();
                String day = RegexUtils.getFirst("(\\d+)", s);
//                log.info("====={}", day);
                e.attr("id", "day" + day).attr("width", "80%");
            }
            sb.append(e);
        }
        return sb.toString();
    }
}
