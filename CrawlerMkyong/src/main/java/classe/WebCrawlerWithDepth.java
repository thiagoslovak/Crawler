package classe;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

/* Um WebCrawler é um programa que navega na Web e encontra páginas novas ou atualizadas para in-dexação. */

public class WebCrawlerWithDepth {

    private static final int MAX_DEPTH = 2;
    private HashSet<String> links;

    public WebCrawlerWithDepth() {
        links = new HashSet<>();
    }

    public void getPageLinks(String URL, int depth) {

        if ((!links.contains(URL) && (depth < MAX_DEPTH))) {
            System.out.println(">> Depth: " + depth + " [" + URL + "]");

            try {
                links.add(URL);

                Document document = Jsoup.connect(URL).get();
                Elements linksOnPage = document.select("a[href]");

                depth++;
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"), depth);
                }
            } catch(IOException e) {
                System.err.println("Para '" + URL + "': " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new WebCrawlerWithDepth().getPageLinks("https://www.magazineluiza.com.br/", 0);
    }
}
