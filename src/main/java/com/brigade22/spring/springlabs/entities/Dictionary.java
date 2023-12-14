package com.brigade22.spring.springlabs.entities;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private int id;
    @NotBlank
    private String name;

    @Valid
    private Language language1;
    @Valid

    private Language language2;

    private final List<Translation> translations = new ArrayList<>();

    public Dictionary() {
    }

    public Dictionary(String name, Language language1, Language language2) {
        this.name = name;
        this.language1 = language1;
        this.language2 = language2;
    }

    public Dictionary(int id, String name, Language language1, Language language2) {
        this.id = id;
        this.name = name;
        this.language1 = language1;
        this.language2 = language2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void addTranslation(Translation translation) {
        this.translations.add(translation);
    }

    public Language getLanguage1() {
        return language1;
    }

    public Language getLanguage2() {
        return language2;
    }

    public void setLanguage1(Language language) {
        this.language1 = language;
    }

    public void setLanguage2(Language language) {
        this.language2 = language;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations.clear();
        this.translations.addAll(translations);
    }
}
