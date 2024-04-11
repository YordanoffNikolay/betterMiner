package org.yordanoffnikolay.betterminer.models;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name="author", columnDefinition = "TEXT")
    private String author;

    @Column(name = "abstract", columnDefinition = "TEXT DEFAULT 'No abstract available'")
    private String abstractText;

    @Column(name="url")
    private String url;

    @Column(name="snippet", columnDefinition = "TEXT DEFAULT 'No abstract available'")
    private String snippet;

    @Column(name="bad_words")
    private boolean badWords;

    @ManyToOne
    @JoinColumn(name = "inn_id")
    private Inn inn;

    public Article() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isBadWords() {
        return badWords;
    }

    public void setBadWords(boolean badWords) {
        this.badWords = badWords;
    }

    public Inn getInn() {
        return inn;
    }

    public void setInn(Inn inn) {
        this.inn = inn;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id && badWords == article.badWords && Objects.equals(title, article.title)
                && Objects.equals(abstractText, article.abstractText) && Objects.equals(url, article.url)
                && Objects.equals(inn, article.inn) && Objects.equals(snippet, article.snippet)
                && Objects.equals(author, article.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, abstractText, url, badWords, inn, snippet, author);
    }
}
