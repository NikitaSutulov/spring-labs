package com.brigade22.spring.springlabs.entities;

public class Translation {
    private final Dictionary dictionary;
    private Word word;
    private Word translatedWord;

    public Translation(Dictionary dictionary, Word word, Word translatedWord) {
        this.dictionary = dictionary;
        this.word = word;
        this.translatedWord = translatedWord;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public Word getTranslatedWord() {
        return translatedWord;
    }

    public void setTranslatedWord(Word translatedWord) {
        this.translatedWord = translatedWord;
    }
}
