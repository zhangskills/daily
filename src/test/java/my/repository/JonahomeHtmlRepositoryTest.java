package my.repository;

import lombok.extern.slf4j.Slf4j;
import my.model.JonahomeHtmlModel;
import my.model.query.QJonahomeHtmlModel;
import my.utils.RegexUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

@Slf4j
public class JonahomeHtmlRepositoryTest {

    @Test
    public void findTopByWeek() {
        JonahomeHtmlModel jonahomeHtmlModel = new QJonahomeHtmlModel().week.eq(1).findOne();
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
}