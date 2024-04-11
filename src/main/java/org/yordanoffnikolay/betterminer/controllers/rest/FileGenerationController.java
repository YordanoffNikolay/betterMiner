/** Following controller is for testing purposes only. **/

package org.yordanoffnikolay.betterminer.controllers.rest;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yordanoffnikolay.betterminer.dtos.DateRange;
import org.yordanoffnikolay.betterminer.models.Article;
import org.yordanoffnikolay.betterminer.repositories.ArticleRepository;
import org.yordanoffnikolay.betterminer.services.ArticleService;
import org.yordanoffnikolay.betterminer.services.FileService;

import java.util.List;

@RestController()
@RequestMapping("/api/file")
public class FileGenerationController {

    private final FileService fileService;
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    public FileGenerationController(FileService fileService, ArticleService articleService, ArticleRepository articleRepository) {
        this.fileService = fileService;
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    @PostMapping
    public void generateFile() {
        List<Article> articles = articleRepository.findAll();
        fileService.writeToFile(articles ,articleService.generateInnList(), new DateRange());
    }
}
