package my;

import my.repository.JonahomeHtmlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    private JonahomeHtmlRepository jonahomeHtmlRepository;

    @RequestMapping(name = "/")
    public String index() {

        return "index";
    }
}
