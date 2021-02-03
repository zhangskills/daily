package my.service;

import lombok.extern.slf4j.Slf4j;
import my.utils.RegexUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import spark.utils.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class JonahomeHtmlService2 {

    public static final void crawl(int page) throws IOException {
        String url = "http://www.jonahome.net/files02/PI_DailyWalk/gb/" + page + ".htm";
        log.info("---抓取 page:{}", url);
        Document doc = Jsoup.connect(url)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                .get();

        Elements elements = doc.select(".Section1").get(0).children();

        Element e0 = elements.select("p:contains(第)").get(0);
        String week = RegexUtils.getFirst("(\\d+)", e0.text());
        String title = e0.select("b").text();

        String e1Str = e0.nextElementSibling().html();
        String day = RegexUtils.getFirst("(\\d+)", e1Str);
        String position = RegexUtils.getFirst("日[&nbsp; ]+(.+?)\\s*&", e1Str);

        String fileName = week + "." + day + ".md";

        try (BufferedWriter fw0 = new BufferedWriter(new FileWriter("SUMMARY.md", true));
             BufferedWriter fw = new BufferedWriter(new FileWriter("gitbook/" + fileName));
        ) {
            fw0.append("* [第").append(week).append("周 第").append(day).append("天](").append(fileName).append(")");
            fw0.newLine();

            fw.append("# ").append(title);
            fw.newLine();
            fw.newLine();
            fw.append("> 时间：第").append(week).append("周 第").append(day).append("天");
            fw.newLine();
            fw.newLine();
            fw.append("> 经文：").append(position);
            fw.newLine();
            fw.newLine();

            for (int i = 2; i < elements.size(); i++) {
                Element e = elements.get(i);
                String tagName = e.tag().getName();
                if (tagName.equals("p")) {
                    if (e.select("b").isEmpty()) {
                        String str = e.text();
                        fw.append(str);
                    } else {
                        for (Node n : e.childNodes()) {
                            if (n.nodeName().equals("b")) {
                                fw.append("** ").append(String.valueOf(n.childNode(0))).append(" **");
                            } else {
                                fw.append(e.text());
                            }
                            fw.newLine();
                            fw.newLine();
                        }
                    }
                } else if (tagName.equals("table") && StringUtils.isEmpty(e.attr("border"))) {
                    String str = "** " + e.text() + " **";
                    fw.append(str);
                } else {
                    String str = e + "";
                    fw.append(str);
                }
                fw.newLine();
                fw.newLine();
            }
        }
    }
}
