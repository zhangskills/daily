package my.config;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.sql.DataSource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import io.ebean.config.DatabaseConfig;
import io.jooby.Jooby;
import io.jooby.ebean.EbeanModule;
import io.jooby.freemarker.FreemarkerModule;
import io.jooby.hikari.HikariModule;
import io.jooby.json.JacksonModule;
import lombok.extern.slf4j.Slf4j;
import my.service.MyWxCpService;
import no.api.freemarker.java8.Java8ObjectWrapper;

@Slf4j
public class JoobyConfig extends Jooby {

    public static Map<Class<?>, Object> classBeanFactory = new HashMap<>();

    {
        // 设置数据库
        install(new HikariModule());

        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setDataSource(require(DataSource.class));
        dbConfig.addPackage("my.model");

        // dbConfig.setDdlGenerate(true);
        // dbConfig.setDdlRun(true);
        // dbConfig.setDdlCreateOnly(true);

        install(new EbeanModule(dbConfig));

        // 安装插件
        // Freemarker
        installFreemarker();
        // Jackson
        installJackson();

        Config config = getEnvironment().getConfig();

        String corpId = config.getString("workWx.xiny.corpId");
        String corpSecret = config.getString("workWx.xiny.corpSecret");
        Integer agentId = config.getInt("workWx.xiny.agentId");
        classBeanFactory.put(MyWxCpService.class, new MyWxCpService(corpId, corpSecret, agentId));

        // PushPlusService.setToken(config.getString("pushplus.token"));
    }

    public static <T> T getBeanByClass(Class<T> clazz) {
        return (T) classBeanFactory.get(clazz);
    }

    private void installJackson() {
        ObjectMapper objectMapper = new ObjectMapper()
                .setLocale(Locale.CHINA)
                .setTimeZone(TimeZone.getTimeZone("GMT+8"))
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        install(new JacksonModule(objectMapper));
    }

    private void installFreemarker() {
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        ClassTemplateLoader loader = new ClassTemplateLoader(JoobyConfig.class, "/templates");
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setObjectWrapper(new Java8ObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        install(new FreemarkerModule(cfg));
    }
}
