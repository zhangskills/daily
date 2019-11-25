package my.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class RegexUtils {

    private static Map<String, Pattern> patternMap = new ConcurrentHashMap<>();

    private static int timeoutMillis = 30_000;

    public static void setTimeoutMillis(int timeoutMillis) {
        RegexUtils.timeoutMillis = timeoutMillis;
    }

    private static Matcher createMatcherWithTimeout(String content, String regex) {
        String key = DigestUtils.md5DigestAsHex(regex.getBytes());
        Pattern p = patternMap.get(key);
        if (p == null) {
            p = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            patternMap.put(key, p);
        }
        CharSequence charSequence = new TimeoutRegexCharSequence(content, timeoutMillis, content, regex);
        return p.matcher(charSequence);
    }


    public static boolean matches(String regex, String content) {
        if (StringUtils.isEmpty(regex) || StringUtils.isEmpty(content)) {
            return false;
        }
        if (content.contains(regex)) {
            return true;
        }
        Matcher m = createMatcherWithTimeout(content, regex);
        return m.find();
    }

    public static String getFirst(String regex, String content) {
        if (StringUtils.isEmpty(regex) || StringUtils.isEmpty(content)) {
            return "";
        }
        Matcher m = createMatcherWithTimeout(content, regex);
        if (m.groupCount() < 1) {
            throw new IllegalArgumentException("regex必须一个以上的分组 regex:" + regex);
        }
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public static List<String> getFirstLine(String regex, String content) {
        List<String> line = new ArrayList<>();
        if (StringUtils.isEmpty(regex) || StringUtils.isEmpty(content)) {
            return line;
        }
        Matcher m = createMatcherWithTimeout(content, regex);
        if (m.find()) {
            int len = m.groupCount();
            if (len < 1) {
                throw new IllegalArgumentException("regex必须一个以上的分组 regex:" + regex);
            }
            for (int i = 0; i < len; i++) {
                line.add(m.group(i + 1));
            }
        }
        return line;
    }

    public static List<List<String>> getAllLine(String regex, String replacement, String content) {
        List<List<String>> list = new ArrayList<>();
        List<List<String>> result = getAllLine(regex, content);
        if (StringUtils.isEmpty(replacement)) {
            return result;
        }
        if (!CollectionUtils.isEmpty(result)) {
            result.forEach(row -> {
                String tmp = replacement;
                for (int i = 0; i < row.size(); i++) {
                    tmp = tmp.replaceAll("\\$" + (i + 1), row.get(i));
                }
                List<String> subList = new ArrayList<>();
                subList.add(tmp);
                list.add(subList);
            });
            return list;
        } else {
            return result;
        }
    }

    public static List<List<String>> getAllLine(String regex, String content) {
        List<List<String>> list = new ArrayList<>();
        if (StringUtils.isEmpty(regex) || StringUtils.isEmpty(content)) {
            return list;
        }
        Matcher m = createMatcherWithTimeout(content, regex);
        int len = m.groupCount();
        if (len < 1) {
            throw new IllegalArgumentException("regex必须一个以上的分组 regex:" + regex);
        }
        while (m.find()) {
            List<String> line = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                line.add(m.group(i + 1));
            }
            list.add(line);
        }
        return list;
    }

    public static String getSite(String url) {
        if (StringUtils.isEmpty(url)) {
            log.warn("网站为空,url:{}", url);
            return "";
        }
        return RegexUtils.getFirst("\\w+://(.*?)(/|:|$)", url);
    }

    // 通过规则一次性获取所有Url列表, 主要针对Detail使用
    public static List<String> getUrlList(String url) {
        List<String> urlList = new ArrayList<>();
        List<String> result = RegexUtils.getFirstLine("\\[(\\d+)-(\\d+)\\]", url);
        if (!CollectionUtils.isEmpty(result)) {
            int currentPage = Integer.parseInt(result.get(0));
            int lastPage = Integer.parseInt(result.get(1));
            for (int i = currentPage; i <= lastPage; i++) {
                String detailUrl = url.replaceFirst("\\[\\d+-\\d+\\]", i + "");
                urlList.add(detailUrl);
            }
        } else {
            urlList.add(url);
        }
        return urlList;
    }

    public static String getUrl(String urlRegex, String replacement, String content, String baseUrl) {
        String url = getReplaceStr(urlRegex, replacement, content);
        return getUrl(baseUrl, url);
    }

    public static String getUrl(String baseUrl, String url) {
        if (!StringUtils.isEmpty(url) && !matches("^\\w+://", url)) {
            try {
                URL nextUrl = new URL(new URL(baseUrl), url);
                url = nextUrl.toString();
            } catch (MalformedURLException e) {
                log.error("获取网址出错,url:{},page:{},err:", baseUrl, url, e + "");
            }
        }
        return url;
    }

    public static String getReplaceStr(String replaceRegex, String replacement, String content) {
        if (!StringUtils.isEmpty(content) && !StringUtils.isEmpty(replaceRegex)) {
            if (StringUtils.isEmpty(replacement)) {
                return getFirst(replaceRegex, content);
            } else {
                List<String> list = getFirstLine(replaceRegex, content);
                if (CollectionUtils.isEmpty(list)) {
                    return null;
                }
                String result = replacement;
                for (int i = 0; i < list.size(); i++) {
                    result = result.replaceAll("\\$" + (i + 1), list.get(i));
                }
                return result;
            }
        }
        return null;
    }

    // 过滤emoji表情
    public static String replaceEmoji(String content, String replacement) {
        if (content == null) {
            return null;
        }
        return content.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", replacement);
    }

    public static String clearTagAndSpaceContent(String content, String placeholder) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        content = content.replaceAll("<\\s*script[^>]+?/\\s*>", " ").replaceAll("<\\s*script[\\s\\S]*?<\\s*/\\s*script\\s*>", " ")
                .replaceAll("<\\s*style[^>]+?/\\s*>", " ").replaceAll("<\\s*style[\\s\\S]*?<\\s*/\\s*style\\s*>", " ")
                .replaceAll("<[^>]+?>", " ").replaceAll("&nbsp;", " ").replaceAll("\\s+", placeholder);
        return content;
    }

    public static String getSiteByUrl(String url) {
        return getFirst("https?://(.*?)/", url);
    }

    public static String getDomainBySite(String site) {
        if (StringUtils.isEmpty(site)) {
            return site;
        }
        String regex = "([a-z0-9--]{1,200}\\.)(ac\\.cn|bj\\.cn|sh\\.cn|tj\\.cn|cq\\.cn|he\\.cn|sn\\.cn|sx\\.cn|nm\\.cn" +
                "|ln\\.cn|jl\\.cn|hl\\.cn|js\\.cn|zj\\.cn|ah\\.cn|fj\\.cn|jx\\.cn|sd\\.cn|ha\\.cn|hb\\.cn|hn\\.cn" +
                "|gd\\.cn|gx\\.cn|hi\\.cn|sc\\.cn|gz\\.cn|yn\\.cn|gs\\.cn|qh\\.cn|nx\\.cn|xj\\.cn|tw\\.cn|hk\\.cn" +
                "|mo\\.cn|xz\\.cn|com\\.cn|net\\.cn|org\\.cn|gov\\.cn|com\\.hk|gov\\.tw" +
                "|我爱你|在线|中国|网址|网店|中文网|公司|网络|集团" +
                "|com|cn|cc|org|net|xin|xyz|vip|shop|top|club|wang|fun|info|online|tech|store|url|ltd|ink|biz|group" +
                "|link|work|pro|mobi|ren|kim|name|tv|red|cool|team|live|pub|company|zone|today|video|art|chat|gold" +
                "|guru|show|life|love|email|fund|city|plus|design|social|center|world|auto|rip|ceo|sale|hk|io|gg" +
                "|tm|gs|us|uk|la|mx|lu|fm|sh|io|me|tw|in)$";

        List<String> line = RegexUtils.getFirstLine(regex, site);
        if (CollectionUtils.isEmpty(line) || line.size() != 2) {
            log.warn("提取域名失败 {}", site);
            return null;
        }
        return line.get(0) + line.get(1);
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}