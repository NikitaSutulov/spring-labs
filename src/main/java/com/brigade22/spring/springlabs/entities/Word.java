package com.brigade22.spring.springlabs.entities;

import java.util.ArrayList;
import java.util.List;

public class Word {
    private String value;
    private final Language language;
    private final List<Translation> translations = new ArrayList<>();

    public Word(Language language, String value) {
        this.language = language;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Language getLanguage() {
        return language;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
