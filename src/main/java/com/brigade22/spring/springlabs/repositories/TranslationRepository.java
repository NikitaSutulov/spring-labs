package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Translation;
import com.brigade22.spring.springlabs.entities.Word;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TranslationRepository {
    private final List<Translation> translations = new ArrayList<>();

    public TranslationRepository() {}

    public void save(Translation translation) {
        this.translations.add(translation);
    }

    public List<Translation> findAll() {
        return this.translations;
    }

    public void delete(Word word, Word translation) {
        this.translations.removeIf((t) -> t.getWord().equals(word) && t.getTranslatedWord().equals(translation));
    }
}
