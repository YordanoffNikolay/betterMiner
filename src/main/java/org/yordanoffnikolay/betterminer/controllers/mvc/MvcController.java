package org.yordanoffnikolay.betterminer.controllers.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yordanoffnikolay.betterminer.dtos.DateRange;
import org.yordanoffnikolay.betterminer.services.ArticleService;

@Controller
@RequestMapping()
public class MvcController {

    private final ArticleService articleService;

    @Autowired
    public MvcController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String index(Model model) {
        DateRange dateRange = new DateRange();
        model.addAttribute("dateRange", dateRange);
        return "index";
    }

    @PostMapping
    public String getArticlesByDateRange(DateRange dateRange, Model model) {
        model.addAttribute("articles", articleService.getArticlesByDateRange(dateRange));
        model.addAttribute("dateRange", dateRange);
        return "results";
    }
}
