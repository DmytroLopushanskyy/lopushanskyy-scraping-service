package scrapper;

public class Main {
    public static void main(String[] args) {
        URLScraper scraper = new URLScraper();
        System.out.println(scraper.retrieve("http://google.com"));

        Scraper cashScraper = new CacheScraper(scraper);
    }
}
