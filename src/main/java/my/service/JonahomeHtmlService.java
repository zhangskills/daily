package my.service;

import lombok.extern.slf4j.Slf4j;
import my.model.JonahomeHtmlModel;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
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
}
