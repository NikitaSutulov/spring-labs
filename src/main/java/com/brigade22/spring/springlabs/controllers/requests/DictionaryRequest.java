package com.brigade22.spring.springlabs.controllers.requests;

import com.brigade22.spring.springlabs.entities.Language;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class DictionaryRequest {

    @NotBlank
    private String name;

    @Valid
    private Language language1;

    @Valid
    private Language language2;

    public Language getLanguage1() {
        return language1;
    }

    public Language getLanguage2() {
        return language2;
    }

    public String getName() {
        return name;
    }

    public void setLanguage1(Language language1) {
        this.language1 = language1;
    }

    public void setLanguage2(Language language2) {
        this.language2 = language2;
    }

    public void setName(String name) {
        this.name = name;
    }
}
