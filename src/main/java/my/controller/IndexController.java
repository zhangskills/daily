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
import java.util.Calendar;

@Controller
public class IndexController {

    @Autowired
    private JonahomeHtmlService jonahomeHtmlService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletResponse response) throws IOException {
        int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        response.sendRedirect("/" + week);
        return null;
    }

    @RequestMapping(value = "/{week}", method = RequestMethod.GET)
    public String index(HttpServletRequest request, HttpServletResponse response,
                        @PathVariable("week") int week) throws IOException {
        if (week > 0) {
            request.setAttribute("lastUrl", "/" + (week - 1));
        }
        if (week < 52) {
            request.setAttribute("nextUrl", "/" + (week + 1));
        }

        String content = jonahomeHtmlService.getContent(week);
        request.setAttribute("content", content);

        return "index";
    }
}
