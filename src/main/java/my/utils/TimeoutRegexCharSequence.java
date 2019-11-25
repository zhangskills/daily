package my.utils;

public class TimeoutRegexCharSequence implements CharSequence {

    private final CharSequence inner;
    private final int timeoutMillis;
    private final long timeoutTime;
    private final String content;
    private final String regex;

    public TimeoutRegexCharSequence(CharSequence inner, int timeoutMillis, String content, String regex) {
        super();
        this.inner = inner;
        this.timeoutMillis = timeoutMillis;
        this.content = content;
        this.regex = regex;
        timeoutTime = System.currentTimeMillis() + timeoutMillis;
    }

    public char charAt(int index) {
        if (System.currentTimeMillis() > timeoutTime) {
            throw new RuntimeException("正则匹配超时 timeout:" + timeoutMillis + " regex:" + regex);
        }
        return inner.charAt(index);
    }

    public int length() {
        return inner.length();
    }

    public CharSequence subSequence(int start, int end) {
        return new TimeoutRegexCharSequence(inner.subSequence(start, end), timeoutMillis, content, regex);
    }

    @Override
    public String toString() {
        return inner.toString();
    }
}