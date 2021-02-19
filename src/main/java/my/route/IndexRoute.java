package my.route;

import my.config.FreeMarkerTemplateEngine;
import my.service.JonahomeHtmlService;
import spark.Route;

import java.text.SimpleDateFormat;
import java.util.*;

public class IndexRoute {

    private static JonahomeHtmlService jonahomeHtmlService = new JonahomeHtmlService();

    public static Route indexRoute = (req, res) -> {
        int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        res.redirect("/w/" + week);
        return null;
    };

    public static Route weekRoute = (req, res) -> {
        Map<String, Object> attributes = new HashMap<>();

        int week = Integer.parseInt(req.params("week"));
        if (week < 0 || week > 52) {
            res.redirect("/");
            return null;
        }
        String dateStr = new SimpleDateFormat("yyyy-MM-dd EEEE", Locale.CHINA).format(new Date());
        attributes.put("dateStr", dateStr + " 第" + Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + "周");

        if (week > 0) {
            attributes.put("lastUrl", "/w/" + (week - 1));
        }
        if (week < 52) {
            attributes.put("nextUrl", "/w/" + (week + 1));
        }

        String content = jonahomeHtmlService.getContentByHtml(week);
        attributes.put("content", content);
        attributes.put("week", week);

        return FreeMarkerTemplateEngine.render("index.ftl", attributes);
    };


}
