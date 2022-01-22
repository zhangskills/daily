package my.route;

import java.util.HashMap;
import java.util.Map;

import io.jooby.Jooby;
import io.jooby.ModelAndView;
import my.model.DailyModel;
import my.service.DailyService;

public class DailyByTodayRoute extends Jooby {
    {
        get("/today", ctx -> {
            Map<String, Object> attributes = new HashMap<>();
            DailyModel dailyModel = DailyService.getDailyModelByToday();
            attributes.put("model", dailyModel);
            return new ModelAndView("today.ftl", attributes);
        });
    }

}
