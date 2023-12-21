package com.brigade22.spring.springlabs.controllers.responses;

import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Translation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DictionaryResponse {
    private final Long id;
    private final String name;
    private final String language;
    private final String translatedLanguage;
    private final List<TranslationResponse> translations;

    public DictionaryResponse(Long id, String name, String language, String translatedLanguage, List<TranslationResponse> translations) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.translatedLanguage = translatedLanguage;
        this.translations = translations;
    }

    public DictionaryResponse(Dictionary dictionary) {
        List<TranslationResponse> translations = new ArrayList<>();
        for (Translation translation : dictionary.getTranslations()) {
            translations.add(new TranslationResponse(translation.getId(), translation.getWord().getValue(), translation.getTranslatedWord().getValue()));
        }
        this.id = dictionary.getId();
        this.name = dictionary.getName();
        this.language = dictionary.getLanguage1().getCode();
        this.translatedLanguage = dictionary.getLanguage2().getCode();
        this.translations = translations;
    }
}
