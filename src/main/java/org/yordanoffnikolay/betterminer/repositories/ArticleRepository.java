package org.yordanoffnikolay.betterminer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yordanoffnikolay.betterminer.models.Article;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findArticlesByInnName(String name);

    List<Article> findArticlesByBadWordsFalse();

    List<Article> findArticlesBySnippet(String text);
}
