package my.model;

import org.jsoup.Jsoup;

import lombok.Data;

@Data
public class DailyModel {
    private int week;
    private int day;
    private String title;
    private String html;

    public String getText() {
        return Jsoup.parse(html).text();
    }

    public String getUrl() {
        return "https://daily2.withword.com/d/" + week + "/" + day;
    }
}
