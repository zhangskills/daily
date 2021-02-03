package my.controller;

import my.config.TemplateEngineFactory;
import my.service.JonahomeHtmlService;

import java.text.SimpleDateFormat;
import java.util.*;

import static spark.Spark.get;
import static spark.Spark.modelAndView;

public class IndexController {

    private JonahomeHtmlService jonahomeHtmlService = new JonahomeHtmlService();

    public IndexController() {
        get("/", (req, res) -> {
            int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
            res.redirect("/w/" + week);
            return null;
        });

        get("/w/:week", (req, res) -> {
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

            String content = jonahomeHtmlService.getContent(week);
            attributes.put("content", content);
            attributes.put("week", week);

            return modelAndView(attributes, "index.ftl");
        }, TemplateEngineFactory.getFreeMarkerTemplateEngine());
    }
}
