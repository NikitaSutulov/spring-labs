package com.brigade22.spring.springlabs.controllers.responses;

import com.brigade22.spring.springlabs.entities.Dictionary;
import com.brigade22.spring.springlabs.entities.Translation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetDictionariesResponse {
    private final List<DictionaryResponse> dictionaries;

    public GetDictionariesResponse(List<Dictionary> dictionaries) {
        this.dictionaries = new ArrayList<>();

        for (Dictionary dictionary : dictionaries) {
            List<TranslationResponse> translations = new ArrayList<>();
            for (Translation translation : dictionary.getTranslations()) {
                translations.add(new TranslationResponse(translation.getId(), translation.getWord().getValue(), translation.getTranslatedWord().getValue()));
            }
            this.dictionaries.add(new DictionaryResponse(dictionary.getId(), dictionary.getName(), dictionary.getLanguage1().getCode(), dictionary.getLanguage2().getCode(), translations));
        }
    }

}
