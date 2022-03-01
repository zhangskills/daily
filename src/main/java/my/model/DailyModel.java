package my.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;

import lombok.Data;

@Data
public class DailyModel {

    private static Pattern p = Pattern.compile("<table>(.*?)</table>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private int week;
    private int day;
    private String title;
    private String html;

    public List<String> getTextContentsForWorkWx() {
        List<String> contents = new ArrayList<>();
        String[] msgs = p.matcher(html).replaceAll("<table>").split("<table>");
        for (String msg : msgs) {
            msg = Jsoup.clean(msg, "", Whitelist.none(), new OutputSettings().prettyPrint(false));
            contents.add(msg.replaceAll("\n+", "\n\n"));
        }
        contents.add("《" + title + "》\nhttps://daily2.withword.com//d/" + week + "/" + day);
        return contents;
    }
}
