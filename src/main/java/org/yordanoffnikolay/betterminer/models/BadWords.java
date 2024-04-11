package org.yordanoffnikolay.betterminer.models;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "bad_words")
public class BadWords {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "word")
    private String word;

    public BadWords() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadWords badWords = (BadWords) o;
        return id == badWords.id && Objects.equals(word, badWords.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word);
    }
}
