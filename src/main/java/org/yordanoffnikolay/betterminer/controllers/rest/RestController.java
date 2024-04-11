package org.yordanoffnikolay.betterminer.controllers.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yordanoffnikolay.betterminer.dtos.DateRange;
import org.yordanoffnikolay.betterminer.dtos.JsonToDateRange;
import org.yordanoffnikolay.betterminer.models.Article;
import org.yordanoffnikolay.betterminer.services.ArticleService;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/request")
public class RestController {

    private final ArticleService articleService;

    public RestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public List<Article> postRequest(@RequestBody String json){
        DateRange dateRange = null;
        try {
            dateRange = JsonToDateRange.mapJsonToDateRange(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articleService.getArticlesByDateRange(dateRange);
    }
}
