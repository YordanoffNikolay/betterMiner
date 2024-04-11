/** Following controller is for testing purposes only. **/

package org.yordanoffnikolay.betterminer.controllers.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yordanoffnikolay.betterminer.models.Article;
import org.yordanoffnikolay.betterminer.repositories.ArticleRepository;
import org.yordanoffnikolay.betterminer.services.ArticleService;

import java.util.List;


@RestController
@RequestMapping("/api/fix")
public class FixController {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    public FixController(ArticleService articleService, ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

//    @PostMapping
//    public void fix() {
//        articleService.fixMismatches();
//    }

    @PostMapping
    public void check() {
        List<Article> articles = articleRepository.findAll();
        articleService.checkForUnwantedWords(articles);
    }

}
