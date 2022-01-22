package my.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.model.SjpptModel;
import my.utils.RegexUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SjpptInitUtilsTest {

    private String imageBasePath = "src/main/webapp/images/";

    @Test
    public void crawl() throws IOException {
//        String url = "http://www.sjppt.net/exodus-s.htm"; // 出埃及记
//        String url = "http://www.sjppt.net/levi-s.htm"; // 利未记
//        String url = "http://www.sjppt.net/num-s.htm"; // 民数记
//        String url = "http://www.sjppt.net/deut-s.htm"; // 申命记
//        String url = "http://www.sjppt.net/josh-s.htm"; // 约书亚记
//        String url = "http://www.sjppt.net/judges-s.htm"; // 士师记
        String url = "http://www.sjppt.net/samuel-s.htm"; // 撒母耳记
        Document document = JsoupUtils.getConnection(url).get();
        Elements pptImgEs = document.select("img[src=pic/icons/ppt.gif]");

        List<SjpptModel> list = new ArrayList<>();
        pptImgEs.forEach(pptImgE -> {
            Element blockE = pptImgE.parent().parent().parent();
            String title = blockE.select("b font").text().trim();
            String a = blockE.select("font a[href*=preview]").attr("href");

            SjpptModel model = new SjpptModel(title, RegexUtils.getFirst("preview/.*?/([^/]*?)-p.htm", a) + "-s");
            list.add(model);
        });

        String json = new ObjectMapper().writeValueAsString(list);
        Files.write(Paths.get("src/main/resources/data/" + RegexUtils.getFirst("/([^/]+?).htm", url) + ".json"), Collections.singleton(json));

        System.out.println(json);
    }


    private void listFile(List<String> list, String path) {
        for (File file : new File(path).listFiles()) {
            if (file.isDirectory()) {
                String absolutePath = file.getAbsolutePath();
                list.add(absolutePath.substring(absolutePath.indexOf(imageBasePath)));
                listFile(list, file.getAbsolutePath());
            }
        }
    }

    @Test
    public void initDir() throws IOException {
        List<String> list = new ArrayList<>();
        listFile(list, imageBasePath);
        Files.write(Paths.get("dir.txt"), list);
    }
}