package classe;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

/* Um WebCrawler é um programa que navega na Web e encontra páginas novas ou atualizadas para in-dexação. */

public class BasicWebCrawler {

    private HashSet<String> links;

    public BasicWebCrawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String URL) {
        //4. Verifique se você já rastreou os URLs
        //(não estamos verificando intencionalmente se há conteúdo duplicado neste exemplo)
        if (!links.contains(URL)) {

            try {

                //4. (i) Se não adicioná-lo ao índice
                if (links.add(URL)) {
                    System.out.println(URL);
                }

                //2. Buscar o código HTML
                Document document = Jsoup.connect(URL).get();


                //3. Analise o HTML para extrair links para outros URLs
                Elements linksOnPage = document.select("a[href]");

                //5. Para cada URL extraído... volte para a Etapa 4.
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"));
                }

            } catch (IOException e) {
                System.err.println("Para '" + URL + "': " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        //1. Escolha um URL da fronteira
        new BasicWebCrawler().getPageLinks("https://www.magazineluiza.com.br/");
    }
}
