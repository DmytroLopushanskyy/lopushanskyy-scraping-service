package scrapper;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class CacheScraper implements Scraper {
    private final Scraper scraper;
    private final String dbPath = "jdbc:sqlite:sample.db";
    private final Statement statement;

    @SneakyThrows
    public CacheScraper(Scraper scraper) {
        this.scraper = scraper;

        Connection connection = DriverManager.getConnection(dbPath);
        statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        statement.executeUpdate("drop table if exists person");
        statement.executeUpdate("create table websites (id integer, name string)");
        statement.executeUpdate("insert into websites values(1, 'https://github.com/DmytroLopushanskyy')");
        statement.executeUpdate("insert into websites values(2, 'https://cms.ucu.edu.ua/')");
    }

    @SneakyThrows
    public boolean isInCache(String url) {
        ResultSet rs = statement.executeQuery(String.format("select * from urls where url=", url));
        // is in cash or not
        return true;
    }

    @SneakyThrows
    public String retrieveFromCache(String url) {
        ResultSet rs = statement.executeQuery(String.format("select * from urls where url=", url));
        try {
            File f = new File(url.split("://")[1].split("/")[0] + ".html");
            Scanner reader = new Scanner(f);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                System.out.println(data);
            }
            System.out.println("Hurray! retrieved");
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public String addToCache(String url, String  text) {
        statement.executeUpdate(String.format("insert into urls(url) values()", url));
        ResultSet rs = statement.executeQuery(String.format("select * from urls where url=", url));
        try {
            FileWriter writer = new FileWriter(url.split("://")[1].split("/")[0] + ".html");
            writer.write(text);
            System.out.println("added to cache");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    public String retrieve(String url) {
        if (isInCache(url)) {
            return retrieveFromCache(url);
        } else {
            String text = scraper.retrieve(url);
            addToCache(url, text);
            return text;
        }
    }
}
