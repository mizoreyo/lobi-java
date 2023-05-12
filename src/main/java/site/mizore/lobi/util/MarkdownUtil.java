package site.mizore.lobi.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class MarkdownUtil {

    /**
     * 获取摘要
     *
     * @param markdownText
     * @return
     */
    public String getSummary(String markdownText) {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(parser.parse(markdownText));
        // remove html tags
        String plainText = Jsoup.parse(html).text();
        if (plainText.length() > 150) {
            return plainText.substring(0, 150) + "...";
        }
        return plainText;
    }

}
