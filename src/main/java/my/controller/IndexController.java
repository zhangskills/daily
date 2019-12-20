package my.controller;

import my.service.JonahomeHtmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Controller
public class IndexController {

    @Autowired
    private JonahomeHtmlService jonahomeHtmlService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletResponse response) throws IOException {
        int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        response.sendRedirect("/w" + week);
        return null;
    }

    @RequestMapping(value = "/w{week}", method = RequestMethod.GET)
    public String index(HttpServletRequest request, HttpServletResponse response,
                        @PathVariable("week") int week) throws IOException {
        if (week < 0 || week > 52) {
            response.sendRedirect("/");
            return null;
        }
        String dateStr = new SimpleDateFormat("yyyy-MM-dd EEEE", Locale.CHINA).format(new Date());
        request.setAttribute("dateStr", dateStr + " 第" + Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + "周");

        if (week > 0) {
            request.setAttribute("lastUrl", "/w" + (week - 1));
        }
        if (week < 52) {
            request.setAttribute("nextUrl", "/w" + (week + 1));
        }

        String content = jonahomeHtmlService.getContent(week);
        request.setAttribute("content", content);

        return "index";
    }
}
