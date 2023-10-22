package com.brigade22.spring.springlabs.entities;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private String name;
    private final List<Translation> translations = new ArrayList<>();

    public Dictionary(String name) {
        this.name = name;
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
}
