package my.route;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jooby.Jooby;
import io.jooby.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import my.Application;
import my.model.SjpptModel;
import my.utils.RegexUtils;

@Slf4j
public class SjpptRoute extends Jooby {

    private final List<String> dirList = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    private final String volumeName;
    private final String imageBasePath;

    public SjpptRoute(String volumeName, String imageBasePath) {
        this.volumeName = volumeName;
        this.imageBasePath = imageBasePath;
    }

    private List<SjpptModel> getSjpptModelList() {
        try {
            return objectMapper.readValue(SjpptRoute.class.getResource("/data/" + volumeName),
                    new TypeReference<>() {
                    });
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    private List<String> getPathList(String path) {
        return Stream.of(Objects.requireNonNull(new File(imageBasePath + path).list()))
                .sorted((a, b) -> {
                    try {
                        String first = RegexUtils.getFirst("(\\d+).jpg", a);
                        String second = RegexUtils.getFirst("(\\d+).jpg", b);
                        return Integer.compare(Integer.parseInt(first), Integer.parseInt(second));
                    } catch (NumberFormatException e) {
                        log.error("排序出错 {} {}", a, b);
                        return 0;
                    }
                }).collect(Collectors.toList());
    }

    {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Application.class.getResourceAsStream("/data/dir.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                dirList.add(line);
            }
        } catch (IOException e) {
            log.error("---加载dir.txt出错", e);
        }

        get("/sjppt", ctx -> {
            Map<String, Object> attributes = new HashMap<>();
            List<SjpptModel> list = getSjpptModelList();

            attributes.put("list", list);

            return new ModelAndView("sjppt/index.ftl", attributes);
        });

        get("/sjppt/detail", ctx -> {
            Map<String, Object> attributes = new HashMap<>();
            String fileName = ctx.query("fileName").valueOrNull();
            String path = "";
            for (String dir : dirList) {
                if (dir.contains(fileName)) {
                    path = dir;
                    break;
                }
            }

            attributes.put("path", path);
            attributes.put("list", getPathList(path));

            return new ModelAndView("sjppt/detail.ftl", attributes);
        });
    }
}
