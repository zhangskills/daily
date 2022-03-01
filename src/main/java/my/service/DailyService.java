package my.service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import my.config.JoobyConfig;
import my.model.DailyModel;

@Slf4j
public abstract class DailyService {

    private static List<Extension> extensions = Collections.singletonList(TablesExtension.create());
    private static Parser parser = Parser.builder().extensions(extensions).build();
    private static HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
    private static Pattern imgSrcPattern = Pattern.compile("img src=\"(.*?)\"");

    public static void send() throws IOException, WxErrorException {
        DailyModel dailyModel = getDailyModelByToday();
        if (dailyModel != null) {
            // log.info(html);
            MyWxCpService wxCpService = JoobyConfig.getBeanByClass(MyWxCpService.class);
            dailyModel.getTextContentsForWorkWx().forEach(content -> {
                try {
                    wxCpService.sendByDaily(content);
                } catch (WxErrorException e) {
                    log.error("", e);
                }
            });
        }
    }

    public static void send(int week, int day) throws IOException, WxErrorException {
        DailyModel dailyModel = getDailyModel(week, day);
        if (dailyModel != null) {
            // log.info(html);
            MyWxCpService wxCpService = JoobyConfig.getBeanByClass(MyWxCpService.class);
            dailyModel.getTextContentsForWorkWx().forEach(content -> {
                try {
                    wxCpService.sendByDaily(content);
                } catch (WxErrorException e) {
                    log.error("", e);
                }
            });
        }
    }

    private static String image2base64(InputStream imageInputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imageInputStream);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            return "data:image/png;base64," + encoder.encodeToString(bytes);
        } catch (Exception e) {
            return "";
        }
    }

    private static InputStream getFileInputStream(String fileName) {
        return DailyService.class.getResourceAsStream(fileName);
    }

    public static DailyModel getDailyModelByToday() throws IOException {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return getDailyModel(week, day);
    }

    public static DailyModel getDailyModel(int week, int day) throws IOException {
        if (day == 0) {
            log.info("星期天不发送");
            return null;
        }

        String fileName = "/days/" + week + "." + day + ".md";

        String style = "<style type=\"text/css\">" +
                "table {width:100%;color:#333;border-width:1px;border-color:#666;border-collapse:collapse;}" +
                "th,td { border-width:1px;border-style:solid;border-color:#666;text-align:center;}" +
                "th{ background-color:#dedede;}" +
                "td { background-color:#fff;}" +
                "table.text-left td {text-align:left;}" +
                "</style>";
        StringBuilder text = new StringBuilder();
        StringBuilder title = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getFileInputStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("> 日期：") || line.startsWith("> 经文：")) {
                    title.append(line.substring(2)).append(" ");
                } else {
                    text.append(line).append("\n");
                }
            }
        }
        DailyModel dailyModel = new DailyModel();
        dailyModel.setWeek(week);
        dailyModel.setDay(day);
        dailyModel.setTitle(title.toString());

        String markdown = text.toString();

        String html = renderer.render(parser.parse(markdown)) + style;

        Matcher m = imgSrcPattern.matcher(html);
        while (m.find()) {
            String group = m.group(1);
            html = html.replace(group, image2base64(getFileInputStream("/days/" + group)));
        }
        dailyModel.setHtml(html.replaceAll("<img", "<img width=100%"));
        return dailyModel;
    }

}
