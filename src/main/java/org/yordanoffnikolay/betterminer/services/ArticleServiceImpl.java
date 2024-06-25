package org.yordanoffnikolay.betterminer.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.yordanoffnikolay.betterminer.dtos.DateRange;
import org.yordanoffnikolay.betterminer.models.Article;
import org.yordanoffnikolay.betterminer.models.BadWords;
import org.yordanoffnikolay.betterminer.models.Inn;
import org.yordanoffnikolay.betterminer.models.Query;
import org.yordanoffnikolay.betterminer.repositories.ArticleRepository;
import org.yordanoffnikolay.betterminer.repositories.BadWordsRepository;
import org.yordanoffnikolay.betterminer.repositories.InnRepository;
import org.yordanoffnikolay.betterminer.repositories.QueryRepository;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final InnRepository innRepository;
    private final ArticleRepository articleRepository;
    private final BadWordsRepository badWordsRepository;
    private final QueryRepository queryRepository;
    private final FileService fileService;

    public ArticleServiceImpl(
            InnRepository innRepository, ArticleRepository articleRepository, BadWordsRepository badWordsRepository,
            QueryRepository queryRepository, FileService fileService) {
        this.innRepository = innRepository;
        this.articleRepository = articleRepository;
        this.badWordsRepository = badWordsRepository;
        this.queryRepository = queryRepository;
        this.fileService = fileService;
    }

    @Override
    public List<Article> getArticlesByDateRange(DateRange dateRange) {
        wipeOutExistingData();
        List<Article> articles = new LinkedList<>();
        List<String> innList = generateInnList();
//        List<String> innList = new LinkedList<>();
//        innList.add("Candesartan hydrochlorothiazide");
        List<Query> pubmedQueries = generateQueries(innList, dateRange);
        extractArticleDetails(pubmedQueries, articles, innList);
        fixMismatches();
        checkForUnwantedWords(articles);
        fileService.writeToFile(articles, innList, dateRange);
        return articleRepository.findArticlesByBadWordsFalse();
    }

    @Override
    public void fixMismatches() {
        List<Article> articlesToFix = articleRepository.findArticlesBySnippet("No abstract available");
        for (int i = 0; i < articlesToFix.size(); i++) {
            Article a = articlesToFix.get(i);
            if (!a.getAbstractText().equals("No abstract available")) {
                a.setSnippet(a.getAbstractText());
                articleRepository.save(a);
            }
        }
    }

    private void wipeOutExistingData() {
        articleRepository.deleteAll();
        queryRepository.deleteAll();
    }

    public void checkForUnwantedWords(List<Article> articles) {
        List<BadWords> badWords = badWordsRepository.findAll();
        for (Article article : articles) {
            if (article.getTitle().contains("case")) continue;
            for (BadWords badWord : badWords) {
                String regex = "\\b" + badWord.getWord() + "s?" + "\\b";
                String text = article.getTitle();
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    article.setBadWords(true);
                    articleRepository.save(article);
                    break;
                }
                String text2 = article.getAbstractText();
                String regex2 = "\\b" + badWord.getWord() + "s?" + "\\b";
                Pattern pattern2 = Pattern.compile(regex2, Pattern.CASE_INSENSITIVE);
                Matcher matcher2 = pattern2.matcher(text2);
                if (matcher2.find()) {
                    article.setBadWords(true);
                    articleRepository.save(article);
                    break;
                }
            }
        }
    }

    private void extractArticleDetails(List<Query> pubmedQueries, List<Article> articles, List<String> innList) {
        for (int i = 0; i < innList.size(); i++) {
            String inn = innList.get(i);
            String query = pubmedQueries.get(i).getClientUrl();
            try {
                Document document = Jsoup.connect(query).get();
                if (responseContainsSingleArticle(document, inn)) continue;
                responseContainsMultipleArticles(articles, document, inn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        addAbstractTextToArticle();
    }

    private void addAbstractTextToArticle() {
        for (Article a : articleRepository.findAll()) {
            if (a.getAbstractText() != null) {
                continue;
            }
            String url = a.getUrl();
            try {
                Document document = Jsoup.connect(url).get();
                Elements checkIfEmpty = document.getElementsByClass("empty-abstract");
                if (!checkIfEmpty.isEmpty()) {
                    a.setAbstractText(checkIfEmpty.text());
                    articleRepository.save(a);
                    continue;
                }
                Elements elements = document.getElementsByClass("abstract-content selected");
                for (Element element : elements) {
//                    String text = Objects.requireNonNull(element.getElementById("eng-abstract")).text();
                    String text = Objects.requireNonNull(element.getElementsByClass("abstract-content selected")).text();
                    a.setAbstractText(text);
                }
                articleRepository.save(a);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void responseContainsMultipleArticles(List<Article> articles, Document document, String inn) {
        Elements elements = document.getElementsByClass("full-docsum");
        for (Element element : elements) {
            Article article = new Article();
            article.setTitle(element.getElementsByClass("docsum-title").text());
            article.setSnippet(element.getElementsByClass("full-view-snippet").text());
            if (article.getSnippet().isEmpty()) {
                article.setSnippet("No abstract available");
            }
            article.setAuthor(element.getElementsByClass("docsum-citation full-citation").text());
            article.setUrl("https://pubmed.ncbi.nlm.nih.gov/"
                    + element.getElementsByClass("docsum-pmid").text() + "/");
            article.setInn(innRepository.findByName(inn));
            articles.add(article);
        }
        articleRepository.saveAll(articles);
    }

    private boolean responseContainsSingleArticle(Document document, String inn) {
        if (!document.getElementsByClass("single-result-redirect-message").isEmpty()) {
            Article article = new Article();
            String title = document.getElementsByClass("heading-title").get(0).text();
            article.setTitle(title);
            StringBuilder sb = new StringBuilder();
            sb.append(document.getElementsByClass("inline-authors").get(0).text()).append(" ");
            sb.append(System.lineSeparator());
            sb.append(document.getElementsByClass("identifiers").get(0).text());
            article.setAuthor(sb.toString());
            String text = document.getElementsByClass("abstract-content selected").text();
            article.setSnippet(text);
            article.setAbstractText(text);
            article.setInn(innRepository.findByName(inn));
            article.setUrl("https://pubmed.ncbi.nlm.nih.gov/" + document.getElementsByClass("current-id").get(0).text() + "/");
            articleRepository.save(article);
            return true;
        }
        return false;
    }


    private List<Query> generateQueries(List<String> innList, DateRange dateRange) {
        List<Query> pubmedQueries;
        for (int i = 0; i < innList.size(); i++) {
            Query query = new Query();
            StringBuilder sb = new StringBuilder();
            sb.append("https://pubmed.ncbi.nlm.nih.gov/?term=%28");
//            String replaceWhitespaceWithPlus = innList.get(i).replaceAll(" ", "%2B");
            String replaceWhitespaceWithPlus = innList.get(i).replaceAll(" ", "+");
            sb.append(replaceWhitespaceWithPlus.toLowerCase());
            sb.append("%29+AND+%28%28%22");
            sb.append(dateRange.getStartYear());
            sb.append("%2F");
            sb.append(dateRange.getStartMonth());
            sb.append("%2F");
            sb.append(dateRange.getStartDay());
            sb.append("%22%5BDate+-+Publication%5D+%3A+%22");
            sb.append(dateRange.getEndYear());
            sb.append("%2F");
            sb.append(dateRange.getEndMonth());
            sb.append("%2F");
            sb.append(dateRange.getEndDay());
            sb.append("%22%5BDate+-+Publication%5D%29%29&size=200");
            query.setClientUrl(sb.toString());
            sb.delete(0, sb.length());

            sb.append("(").append(innList.get(i).toLowerCase()).append(")").append(" AND ((\"").append(dateRange.getStartYear())
                    .append("/").append(dateRange.getStartMonth()).append("/").append(dateRange.getStartDay())
                    .append("\"[Date - Publication] : \"").append(dateRange.getEndYear()).append("/")
                    .append(dateRange.getEndMonth()).append("/").append(dateRange.getEndDay())
                    .append("\"[Date - Publication]))");
            query.setServerQuery(sb.toString());
            query.setInn(innRepository.findByName(innList.get(i)));
            queryRepository.save(query);
        }
        pubmedQueries = queryRepository.findAll();
        return pubmedQueries;
    }

    @Override
    public List<String> generateInnList() {
        List<String> innList = new LinkedList<>();
        innRepository.findAll();
        for (Inn inn : innRepository.findAll()) {
            innList.add(inn.getName());
        }
        return innList;
    }
}
