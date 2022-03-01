package my.service;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import my.config.JoobyConfig;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class DailyServiceTest {

    @BeforeClass
    public static void beforeClass() {
        new JoobyConfig();
    }

    @Test
    public void name() {
    }

    @Test
    public void send() throws IOException, WxErrorException {
        DailyService.send();
    }

    @Test
    public void week() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 0, 1);
        calendar.add(Calendar.DATE, -8);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        log.info("{}--第{}周，第{}天", calendar.getTime(), week, day);
    }

    @Test
    public void send2() throws IOException, WxErrorException {
        DailyService.send(11, 3);
    }

    @Test
    public void markdown2html() {
        List<Extension> extensions = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(extensions)
                .build();
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(extensions)
                .build();

        String markdown = "" +
                "" +
                "| Header  | Another Header |\n" +
                "|---------|----------------|\n" +
                "| field 1 | value one      |" +
                "";
        String html = renderer.render(parser.parse(markdown));
        log.info(html);
    }
}