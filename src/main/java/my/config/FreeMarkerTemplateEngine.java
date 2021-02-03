package my.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import spark.ModelAndView;
import spark.TemplateEngine;

import java.io.IOException;
import java.io.StringWriter;

public class FreeMarkerTemplateEngine extends TemplateEngine {

    private static final String TEMPLATE_PATH = "/templates";

    private Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

    private static FreeMarkerTemplateEngine instance = new FreeMarkerTemplateEngine();

    public static FreeMarkerTemplateEngine getInstance() {
        return instance;
    }

    private FreeMarkerTemplateEngine() {
        final ClassTemplateLoader loader = new ClassTemplateLoader(FreeMarkerTemplateEngine.class, TEMPLATE_PATH);
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    @Override
    public String render(ModelAndView modelAndView) {
        try {
            StringWriter stringWriter = new StringWriter();

            Template template = cfg.getTemplate(modelAndView.getViewName());
            template.process(modelAndView.getModel(), stringWriter);

            return stringWriter.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch (TemplateException e) {
            throw new IllegalArgumentException(e);
        }
    }

}