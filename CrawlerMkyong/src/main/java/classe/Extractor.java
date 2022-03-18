package classe;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Extractor {

    private HashSet<String> links;
    private List<List<String>> articles;

    public Extractor() {
        links = new HashSet<>();
        articles  = new ArrayList<>();
    }

    //Encontre todos os URLs que começam com "http://www.mkyong.com/page/" e adicione-os ao HashSet
    public void getPageLinks(String URL) {

        if (!links.contains(URL)) {

            try {
                Document document = Jsoup.connect(URL).get();
                Elements otherLinks = document.select("a[href^=\"http://www.mkyong.com/page/\"]");

                for (Element page : otherLinks) {
                    if (links.add(URL)) {
                        System.out.println(URL);
                    }
                    getPageLinks(page.attr("abs:href"));
                }
            } catch(IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    //Conecta-se a cada link salvo no artigo e encontra todos os artigos na página
    public void getArticles() {

        links.forEach(x -> {
            Document document;

            try {
                document = Jsoup.connect(x).get();

                Elements articleLinks = document.select("h2 a[href^=\"http://www.mkyong.com/\"]");

                for (Element article : articleLinks) {
                    //Recupera apenas os títulos dos artigos que contêm Java 8
                    if (article.text().matches("^.*?(Java 8|java 8|JAVA 8).*$")) {
                        ArrayList<String> temporary = new ArrayList<>();
                        temporary.add(article.text()); //Título do artigo
                        temporary.add(article.attr("abs:href")); //A URL do artigo
                        articles.add(temporary);
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
    }

    public void writeToFile(String filename) {
        FileWriter writer;

        try {
            writer = new FileWriter(filename);
            articles.forEach(a -> {
                try {
                    String temp = "- Title: " + a.get(0) + " (link: " + a.get(1) + ")\n";
                    //exibe no console
                    System.out.println(temp);

                    //salvar em arquivo
                    writer.write(temp);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] arg) {
        Extractor bwc = new Extractor();
        bwc.getPageLinks("http://www.mkyong.com");
        bwc.getArticles();
        bwc.writeToFile("Java 8 Articles");
    }
}
