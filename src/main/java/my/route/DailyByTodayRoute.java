package my.route;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.jooby.Jooby;
import io.jooby.ModelAndView;
import my.model.DailyModel;
import my.service.DailyService;

public class DailyByTodayRoute extends Jooby {
    {
        get("/today", ctx -> {
            Calendar calendar = Calendar.getInstance();
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            ctx.sendRedirect("/d/" + week + "/" + day);
            return null;
        });

        get("/d/{week}/{day}", ctx -> {
            int week = ctx.path("week").intValue();
            if (week < 1 || week > 52) {
                ctx.sendRedirect("/today");
                return null;
            }
            int day = ctx.path("day").intValue();
            if (day < 1 || day > 6) {
                ctx.sendRedirect("/d/" + week + "/1");
                return null;
            }

            Map<String, Object> attributes = new HashMap<>();
            DailyModel dailyModel = DailyService.getDailyModel(week, day);
            attributes.put("model", dailyModel);
            return new ModelAndView("today.ftl", attributes);
        });
    }

}
