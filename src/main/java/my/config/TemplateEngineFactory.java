package my.config;

import lombok.Getter;

public class TemplateEngineFactory {

    @Getter
    private static FreeMarkerTemplateEngine freeMarkerTemplateEngine = new FreeMarkerTemplateEngine("/templates");

}
