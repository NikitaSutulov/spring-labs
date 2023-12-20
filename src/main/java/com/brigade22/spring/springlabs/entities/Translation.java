package com.brigade22.spring.springlabs.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    private Dictionary dictionary;

    @Setter
    @NotNull
    @ManyToOne
    private Word word;

    @Setter
    @NotNull
    @ManyToOne
    private Word translatedWord;

    public Translation() {
    }

    public Translation(Long id, Dictionary dictionary, Word word, Word translatedWord) {
        this.id = id;
        this.dictionary = dictionary;
        this.word = word;
        this.translatedWord = translatedWord;
    }

    public Translation(Dictionary dictionary, Word word, Word translatedWord) {
        this.dictionary = dictionary;
        this.word = word;
        this.translatedWord = translatedWord;
    }

    public Translation(Word word, Word translatedWord) {
        this.word = word;
        this.translatedWord = translatedWord;
    }
}
