package org.yordanoffnikolay.betterminer.controllers.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yordanoffnikolay.betterminer.dtos.DateRange;
import org.yordanoffnikolay.betterminer.services.ArticleService;

import java.text.ParseException;

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
    public String getArticlesByDateRange(@ModelAttribute("dateRange") DateRange dateRange, Model model,
                                         BindingResult bindingResult) throws ParseException {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        try {
            model.addAttribute("articles", articleService.getArticlesByDateRange(dateRange));
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "index";
        }
        return "results";
    }
}
