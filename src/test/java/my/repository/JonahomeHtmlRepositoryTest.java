package my.repository;

import lombok.extern.slf4j.Slf4j;
import my.Application;
import my.model.JonahomeHtmlModel;
import my.service.JonahomeHtmlService;
import my.service.JonahomeHtmlService2;
import my.utils.RegexUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
@Slf4j
public class JonahomeHtmlRepositoryTest {

    @Autowired
    private JonahomeHtmlRepository jonahomeHtmlRepository;
    @Autowired
    private JonahomeHtmlService jonahomeHtmlService;

    @Test
    public void findTopByWeek() {
        JonahomeHtmlModel jonahomeHtmlModel = jonahomeHtmlRepository.findTopByWeek(1);
//        System.out.println(jonahomeHtmlModel.getContent());

        Elements elements = Jsoup.parse(jonahomeHtmlModel.getContent()).select(".Section1");

        for (Element e : elements.get(0).children()) {
            if (!e.select("table[border=0]").isEmpty()) {
                String s = e.select("td:contains(日)").get(0).text();
                String day = RegexUtils.getFirst("(\\d+)", s);
                System.out.println("=====" + day);
                e.attr("id", "day" + day);
            }
            System.out.println(e + "");
        }

//        System.out.println(elements.select("table[border=0]"));
    }

    /**
     * from http://www.jonahome.net/files02/PI_DailyWalk/gb/
     */
    @Test
    public void saveGitBook() throws IOException {

        for (int i = 101; i < 366; i++) {
            JonahomeHtmlService2.crawl(i);
        }


    }
}