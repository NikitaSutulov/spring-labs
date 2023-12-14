package com.brigade22.spring.springlabs.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Word {
    @Getter
    private int id;
    @NotBlank
    private String value;
    private final Language language;
    private final List<Translation> translations = new ArrayList<>();

    public Word(int id, Language language, String value) {
        this.id = id;
        this.language = language;
        this.value = value;
    }

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

    public void setId(int id) {
        this.id = id;
    }
}
