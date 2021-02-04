package my.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;

public abstract class FreeMarkerTemplateEngine {

    private static Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

    static {
        final ClassTemplateLoader loader = new ClassTemplateLoader(FreeMarkerTemplateEngine.class, "/templates");
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public static String render(String viewName, Object model) {
        try {
            StringWriter stringWriter = new StringWriter();

            Template template = cfg.getTemplate(viewName);
            template.process(model, stringWriter);

            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            throw new IllegalArgumentException(e);
        }
    }

}