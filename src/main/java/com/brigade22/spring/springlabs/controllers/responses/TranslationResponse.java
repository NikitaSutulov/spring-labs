package com.brigade22.spring.springlabs.controllers.responses;

public class TranslationResponse {
    private int id;
    private String word;
    private String translation;

    public TranslationResponse(int id, String word, String translation) {
        this.id = id;
        this.word = word;
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public String getWord() {
        return word;
    }
    public int getId() {
        return id;
    }
}
