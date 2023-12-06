package com.brigade22.spring.springlabs.controllers.responses;

public class TranslationResponse {
    private String word;
    private String translation;

    public TranslationResponse(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public String getWord() {
        return word;
    }
}
