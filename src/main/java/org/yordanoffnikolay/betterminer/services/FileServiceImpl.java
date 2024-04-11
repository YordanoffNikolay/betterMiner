package org.yordanoffnikolay.betterminer.services;

import com.sun.tools.javac.Main;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.yordanoffnikolay.betterminer.dtos.DateRange;
import org.yordanoffnikolay.betterminer.models.Article;
import org.yordanoffnikolay.betterminer.repositories.ArticleRepository;
import org.yordanoffnikolay.betterminer.repositories.QueryRepository;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final ArticleRepository articleRepository;
    private final QueryRepository queryRepository;

    public FileServiceImpl(ArticleRepository articleRepository, QueryRepository queryRepository) {
        this.articleRepository = articleRepository;
        this.queryRepository = queryRepository;
    }

    @Override
    public void writeToFile(List<Article> articles, List<String> innList, DateRange dateRange) {
        String userDir = System.getProperty("user.home");
        String savePath = userDir + File.separator + "Desktop" + File.separator;
        try (XWPFDocument doc = new XWPFDocument()) {

            generateFileTitle(doc);
            addBlankRow(doc);
            addBlankRow(doc);

            for (String inn : innList) {
                List<Article> articlesByInn = generateSearchTitle(inn, doc);

                if (articlesByInn.isEmpty()) {
                    XWPFParagraph p11 = doc.createParagraph();
                    XWPFRun r11 = p11.createRun();
                    r11.setText(String.format("За периода %s.%s.%s-%s.%s.%s няма добавени нови публикации",
                            dateRange.getStartDay(), dateRange.getStartMonth(), dateRange.getStartYear(),
                            dateRange.getEndDay(), dateRange.getEndMonth(), dateRange.getEndYear()));
                    r11.setFontSize(12);
                    r11.setItalic(true);
                    addBlankRow(doc);
                }

                generateSearchTermsParagraph(inn, doc);
                generateSearchResultsCountParagraph(inn, doc);

                if (articlesByInn.isEmpty()) {
                    addNoResultsPicture(doc);
                    continue;
                }
                addBlankRow(doc);

                generateClippedFromUrlParagraph(inn, doc);

                for (int i = 0; i < articlesByInn.size(); i++) {
                    String currInn = articlesByInn.get(i).getInn().getName();
                    XWPFParagraph p7 = doc.createParagraph();
                    XWPFRun r7 = p7.createRun();
                    if (!articlesByInn.get(i).isBadWords()) {
                        r7.setBold(true);
                    }
                    r7.setText(String.valueOf(i + 1));

                    generateArticleTitle(doc, articlesByInn, i, currInn);
                    doc.createParagraph().createRun().setText(articlesByInn.get(i).getAuthor());

                    generateArticleSnippet(doc, articlesByInn, i, currInn);
                }
                addBlankRow(doc);
                addBlankRow(doc);
            }

            doc.createParagraph().createRun().setText("--------------------------------------------End of the document--------------------------------------------");
            try (OutputStream out = new FileOutputStream(savePath
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "-PubMed.docx")) {
                doc.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addBlankRow(XWPFDocument doc) {
        doc.createParagraph().createRun().setText(" ");
    }

    private static void generateArticleSnippet(XWPFDocument doc, List<Article> articlesByInn, int i, String currInn) {
        XWPFParagraph p10 = doc.createParagraph();
        String[] snippetArray = articlesByInn.get(i).getSnippet().split(" ");
        for (String word : snippetArray) {
            XWPFRun r10 = p10.createRun();
            if (word.equalsIgnoreCase(currInn)) {
                r10.setBold(true);
                r10.setFontSize(13);
                r10.setText(word + " ");
            } else {
                r10.setText(word + " ");
            }
        }
        addBlankRow(doc);
    }

    private static void generateArticleTitle(XWPFDocument doc, List<Article> articlesByInn, int i, String currInn) {
        XWPFParagraph p9 = doc.createParagraph();
        String[] titleArray = articlesByInn.get(i).getTitle().split(" ");
        for (int j = 0; j < titleArray.length; j++) {
        XWPFHyperlinkRun hyperlink2 = p9.createHyperlinkRun(articlesByInn.get(i).getUrl());
            if (titleArray[j].equalsIgnoreCase(currInn)) {
                hyperlink2.setBold(true);
                hyperlink2.setFontSize(13);
            }
            hyperlink2.setUnderline(UnderlinePatterns.SINGLE);
            hyperlink2.setColor("0000ff");
            hyperlink2.setText(titleArray[j] + " ");
        }
    }

    private void generateClippedFromUrlParagraph(String inn, XWPFDocument doc) {
        XWPFParagraph p8 = doc.createParagraph();
        XWPFRun r8 = p8.createRun();
        r8.setText("Clipped from: ");
        String st =queryRepository.findQueriesByInnName(inn).getClientUrl().replaceAll(" ", "+");
        XWPFHyperlinkRun hyperlink = p8.insertNewHyperlinkRun(1, st);
        hyperlink.setUnderline(UnderlinePatterns.SINGLE);
        hyperlink.setColor("0000ff");
        hyperlink.setText(queryRepository.findQueriesByInnName(inn).getClientUrl());
        addBlankRow(doc);
    }

    private static void addNoResultsPicture(XWPFDocument doc) throws IOException {
        try (InputStream in = Main.class.getClassLoader().getResourceAsStream("static/images/nothingFound.png")) {
            doc.createParagraph().createRun().addPicture(in, XWPFDocument.PICTURE_TYPE_PNG, "nothingFound.png", Units.toEMU(450), Units.toEMU(75));
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateSearchResultsCountParagraph(String inn, XWPFDocument doc) {
        XWPFParagraph p6 = doc.createParagraph();
        XWPFRun r6 = p6.createRun();
        r6.setText("Search Results -");
        XWPFRun r7 = p6.createRun();
        r7.setText(" " + articleRepository.findArticlesByInnName(inn).size());
        r6.setFontSize(12);
    }

    private void generateSearchTermsParagraph(String inn, XWPFDocument doc) {
        XWPFParagraph p4 = doc.createParagraph();
        p4.setAlignment(ParagraphAlignment.LEFT);
        p4.setVerticalAlignment(TextAlignment.TOP);
        XWPFRun r4 = p4.createRun();
        r4.setText("Search terms: ");
        r4.setFontSize(13);
        XWPFRun r5 = p4.createRun();
        r5.setText(queryRepository.findQueriesByInnName(inn).getServerQuery());
        r5.setFontSize(12);
    }

    private List<Article> generateSearchTitle(String inn, XWPFDocument doc) {
        XWPFParagraph p3 = doc.createParagraph();
        p3.setAlignment(ParagraphAlignment.RIGHT);
        p3.setVerticalAlignment(TextAlignment.TOP);
        XWPFRun r3 = p3.createRun();
        r3.setText(inn + " search");
        List<Article> articlesByInn = articleRepository.findArticlesByInnName(inn);
        r3.setFontSize(14);
        r3.setBold(true);
        return articlesByInn;
    }

    private static void generateFileTitle(XWPFDocument doc) {
        XWPFParagraph p1 = doc.createParagraph();
        p1.setAlignment(ParagraphAlignment.LEFT);
        p1.setVerticalAlignment(TextAlignment.AUTO);
        XWPFRun r1 = p1.createRun();
        r1.setText(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " - PubMed");
        r1.setFontSize(18);
        r1.setUnderline(UnderlinePatterns.SINGLE);
        r1.addBreak();
//            r1.setUnderlineColor("000000");

        XWPFRun r2 = p1.createRun();
        r2.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE"))
                + ", " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
                + "     " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")));
        r2.setFontSize(9);
    }
}
