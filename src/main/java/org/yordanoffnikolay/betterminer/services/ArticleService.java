package org.yordanoffnikolay.betterminer.services;

import org.yordanoffnikolay.betterminer.dtos.DateRange;
import org.yordanoffnikolay.betterminer.models.Article;

import java.util.List;

public interface ArticleService {
    List<Article> getArticlesByDateRange(DateRange dateRange);

    List<String> generateInnList();

    void fixMismatches();

    void checkForUnwantedWords(List<Article> articles);
}
