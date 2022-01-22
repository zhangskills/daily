package my.route;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.jooby.Jooby;
import io.jooby.ModelAndView;
import my.service.JonahomeHtmlService;

public class DailyByWeekRoute extends Jooby {

    private JonahomeHtmlService jonahomeHtmlService = new JonahomeHtmlService();

    {
        get("/", ctx -> {
            int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
            ctx.sendRedirect("/w/" + week);
            return null;
        });

        get("/w/{week}", ctx -> {
            Map<String, Object> attributes = new HashMap<>();

            int week = ctx.path("week").intValue();
            if (week < 0 || week > 52) {
                ctx.sendRedirect("/");
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

            return new ModelAndView("daily.ftl", attributes);
        });
    }
}
