package com.now.webscraper.entity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ScraperTask implements Runnable {

    private Document document;
    private final String url;
    private final String searchTerm;
    private final Map<String, String> results;
    private final CountDownLatch latch;

        public ScraperTask(
             String url
            ,String searchTerm
            ,Map<String, String> results
            ,CountDownLatch latch) {

        this.url = url;
        this.searchTerm = searchTerm;
        this.results = results;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
                this.document = Jsoup.connect(url).get();
        } catch (IOException e) {
            latch.countDown();
            e.printStackTrace();
        }

        Elements links = document.getElementsByClass("title");

        for (Element element: links) {
            if(element.childNodes() != null && element.childNodes().get(0).childNodes() != null && !element.childNodes().get(0).childNodes().isEmpty()) {
                String header = searchTerm == null ? "" : element.childNodes().get(0).childNodes().get(0).toString();
                if(searchTerm.length() > 0 && header.contains(searchTerm))
                    results.put(element.childNodes().get(0).attributes().get("href"), header);
                else if (searchTerm.equals("")) {
                    results.put(element.childNodes().get(0).attributes().get("href"), header);
                }
            }
        }

        latch.countDown();
    }
}
