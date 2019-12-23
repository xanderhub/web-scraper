package com.now.webscraper.controller;

import com.now.webscraper.service.ScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScrapperController {
    @Autowired
    private ScrapperService service;

    @GetMapping(path = "/articles", produces = "application/json")
    public ResponseEntity getArticlesByPage(
            @RequestParam(value = "page", required = false) final Integer page,
            @RequestParam(value = "search", required = false) final String searchTerm) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getArticles(page, searchTerm, false));
    }

    @GetMapping(path = "/findOnPages", produces = "application/json")
    public ResponseEntity getArticlesFromAllPages(
            @RequestParam(value = "maxPage") final Integer maxPage,
            @RequestParam(value = "search", required = false) final String searchTerm) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getArticles(maxPage, searchTerm, true));
    }
}
