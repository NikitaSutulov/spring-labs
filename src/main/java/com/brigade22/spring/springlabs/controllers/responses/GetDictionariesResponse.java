package com.brigade22.spring.springlabs.controllers.responses;

import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Translation;

import java.util.ArrayList;
import java.util.List;

public class GetDictionariesResponse {
    private final List<DictionaryResponse> dictionaries;

    public GetDictionariesResponse(List<Dictionary> dictionaries) {
        this.dictionaries = new ArrayList<>();

        for (Dictionary dictionary : dictionaries) {
            List<TranslationResponse> translations = new ArrayList<>();
            for (Translation translation : dictionary.getTranslations()) {
                translations.add(new TranslationResponse(translation.getWord().getValue(), translation.getTranslatedWord().getValue()));
            }
            this.dictionaries.add(new DictionaryResponse(dictionary.getName(), dictionary.getLanguage1().getCode(), dictionary.getLanguage2().getCode(), translations));
        }
    }

    public List<DictionaryResponse> getDictionaries() {
        return dictionaries;
    }
}

class DictionaryResponse {
    private String name;
    private String language;
    private String translatedLanguage;
    private List<TranslationResponse> translations;

    public DictionaryResponse(String name, String language, String translatedLanguage, List<TranslationResponse> translations) {
        this.name = name;
        this.language = language;
        this.translatedLanguage = language;
        this.translations = translations;
    }

    public DictionaryResponse(Dictionary dictionary) {
        List<TranslationResponse> translations = new ArrayList<>();
        for (Translation translation : dictionary.getTranslations()) {
            translations.add(new TranslationResponse(translation.getWord().getValue(), translation.getTranslatedWord().getValue()));
        }
        this.name = dictionary.getName();
        this.language = dictionary.getLanguage1().getCode();
        this.translatedLanguage = dictionary.getLanguage2().getCode();
        this.translations = translations;
    }

    public List<TranslationResponse> getTranslations() {
        return translations;
    }

    public String getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public String getTranslatedLanguage() {
        return translatedLanguage;
    }
}

class TranslationResponse {
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
