package org.yordanoffnikolay.betterminer.services;

import org.yordanoffnikolay.betterminer.dtos.DateRange;
import org.yordanoffnikolay.betterminer.models.Article;

import java.util.List;

public interface FileService {
    void writeToFile(List<Article> articles, List<String> innList, DateRange dateRange);
}
