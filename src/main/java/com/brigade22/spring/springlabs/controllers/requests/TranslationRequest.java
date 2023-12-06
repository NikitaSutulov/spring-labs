package com.brigade22.spring.springlabs.controllers.requests;

import jakarta.validation.constraints.NotBlank;

public class TranslationRequest {
    @NotBlank
    private String word;
    @NotBlank
    private String translatedWord;

    public void setWord(String word) {
        this.word = word;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

    public String getWord() {
        return word;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

}
