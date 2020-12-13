package scrapper;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class URLScraper implements Scraper {
    @SneakyThrows
    public String retrieve(String url) {
        // implement retrieve with some library
        Document data = Jsoup.connect(url).get();
        return data.toString();
    }
}
