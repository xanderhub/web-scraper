package com.now.webscraper.service;

import com.now.webscraper.entity.ScraperTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

@Service
public class ScrapperService {

    @Value("${app.target-site}")
    private String baseUrl;

    @Value("${app.num-of-threads}")
    private Integer numOfThreads;
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

    private CountDownLatch latch;

    public ScrapperService() {
    }


    public Map<String, String> getArticles(Integer pageNumber, String searchTerm, Boolean getFromAllPages) {
        Map<String, String> result = new ConcurrentHashMap<>();

        setLatch(getFromAllPages ? pageNumber : 1);

        if(getFromAllPages) {
            for (int i = 1; i <= pageNumber; i++) {
                executor.submit(new ScraperTask(baseUrl + "/" + i
                        ,searchTerm
                        ,result
                        ,latch));
            }

        }

        else {
            executor.submit(new ScraperTask(baseUrl + "/" + pageNumber, searchTerm, result, latch));
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void setLatch(Integer numOfPages) {
        this.latch = new CountDownLatch(numOfPages);
    }
}
